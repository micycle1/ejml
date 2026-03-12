/*
 * Copyright (c) 2021, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml.sparse.csc.mult;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.EjmlUnitTests;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.masks.FMaskFactory;
import org.ejml.ops.FSemiRing;
import org.ejml.ops.FSemiRings;
import org.ejml.sparse.csc.CommonOpsWithSemiRing_FSCC;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.ejml.TestFMaskUtil.assertMaskedResult;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"UnusedMethod"})
@Generated("org.ejml.sparse.csc.mult.TestMatrixMatrixMultWithSemiRing_DSCC")
public class TestMatrixMatrixMultWithSemiRing_FSCC extends EjmlStandardJUnit {
    FMatrixSparseCSC inputMatrix;

    @BeforeEach
    public void setUp() {
        // based on example in http://mit.bme.hu/~szarnyas/grb/graphblas-introduction.pdf
        inputMatrix = new FMatrixSparseCSC(7, 7, 12);
        inputMatrix.set(0, 1, 1);
        inputMatrix.set(0, 3, 1);
        inputMatrix.set(1, 4, 1);
        inputMatrix.set(1, 6, 1);
        inputMatrix.set(2, 5, 1);
        inputMatrix.set(3, 0, 0.2f);
        inputMatrix.set(3, 2, 0.4f);
        inputMatrix.set(4, 5, 1);
        inputMatrix.set(5, 2, 0.5f);
        inputMatrix.set(6, 2, 1);
        inputMatrix.set(6, 3, 1);
        inputMatrix.set(6, 4, 1);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("sparseVectorMatrixMultSources")
    void mult_v_A( String desc, FSemiRing semiRing, float[] expected ) {
        // graphblas == following outgoing edges of source nodes
        FMatrixSparseCSC vector = new FMatrixSparseCSC(1, 7);
        vector.set(0, 3, 0.5f);
        vector.set(0, 5, 0.6f);

        FMatrixSparseCSC found = CommonOpsWithSemiRing_FSCC.mult(vector, inputMatrix, null, semiRing);

        assertEquals(expected[0], found.get(0, 0));
        assertEquals(expected[1], found.get(0, 2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("sparseMatrixSources")
    void elementMult( String desc, FMatrixSparseCSC matrix, FMatrixSparseCSC otherMatrix ) {
        FSemiRing semiRing = FSemiRings.PLUS_TIMES;

        FMatrixSparseCSC found = CommonOpsWithSemiRing_FSCC.elementMult(matrix, otherMatrix, null, semiRing, null, null, null);
        FMatrixSparseCSC expected = CommonOps_FSCC.elementMult(matrix, otherMatrix, null, null, null);

        EjmlUnitTests.assertEquals(expected, found);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("sparseMatrixSources")
    void add( String desc, FMatrixSparseCSC matrix, FMatrixSparseCSC otherMatrix ) {
        FSemiRing semiRing = FSemiRings.PLUS_TIMES;

        FMatrixSparseCSC found = CommonOpsWithSemiRing_FSCC.add(1, matrix, 1, otherMatrix, null, semiRing, null, null, null);
        FMatrixSparseCSC expected = CommonOps_FSCC.add(1, matrix, 1, otherMatrix, null, null, null);

        EjmlUnitTests.assertEquals(expected, found);
    }

    @Test
    public void maskedMult() {
        var random = new Random(1337);
        var a = RandomMatrices_FSCC.rectangle(10, 10, 30, random);
        var b = RandomMatrices_FSCC.rectangle(10, 10, 30, random);
        var mask = FMaskFactory.builder(RandomMatrices_FSCC.rectangle(10, 10, 30, random), true).build();

        var unmasked = new FMatrixSparseCSC(10, 10, 0);
        var masked = new FMatrixSparseCSC(10, 10, 0);

        CommonOpsWithSemiRing_FSCC.mult(a, b, unmasked, FSemiRings.PLUS_TIMES, null, null, null);
        CommonOpsWithSemiRing_FSCC.mult(a, b, masked, FSemiRings.PLUS_TIMES, mask, null, null);

        assertMaskedResult(unmasked, masked, mask);
    }

    private static Stream<Arguments> sparseVectorMatrixMultSources() {
        return Stream.of(
                // expected entries for (0, 0) and (0, 2)
                Arguments.of("PLUS, TIMES", FSemiRings.PLUS_TIMES, new float[]{0.1f, 0.5f}),
                Arguments.of("OR, AND", FSemiRings.OR_AND, new float[]{1, 1}),
                Arguments.of("MIN, PLUS", FSemiRings.MIN_PLUS, new float[]{0.7f, 0.9f}),
                Arguments.of("MAX, PLUS", FSemiRings.MAX_PLUS, new float[]{0.7f, 1.1f}),
                Arguments.of("MIN, TIMES", FSemiRings.MIN_TIMES, new float[]{0.1f, 0.2f}),
                Arguments.of("MAX, MIN", FSemiRings.MAX_MIN, new float[]{0.2f, 0.5f})
        );
    }

    private static Stream<Arguments> sparseMatrixSources() {
        Random rand = new Random(42);
        Random otherRandom = new Random(1337);
        FMatrixSparseCSC sparseMatrix = RandomMatrices_FSCC.rectangle(10, 10, 15, rand);
        FMatrixSparseCSC denseMatrix = RandomMatrices_FSCC.rectangle(10, 10, 90, rand);

        return Stream.of(
                Arguments.of("Both really sparse", sparseMatrix, RandomMatrices_FSCC.rectangle(10, 10, 15, otherRandom)),
                Arguments.of("Sparse, denseCSC", sparseMatrix, denseMatrix),
                Arguments.of("dense, sparse", denseMatrix, sparseMatrix),
                Arguments.of("Both denseCSC", denseMatrix, RandomMatrices_FSCC.rectangle(10, 10, 90, otherRandom))
        );
    }
}
