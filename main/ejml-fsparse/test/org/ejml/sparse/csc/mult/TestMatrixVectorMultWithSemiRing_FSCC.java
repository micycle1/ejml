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
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.masks.FMaskFactory;
import org.ejml.masks.Mask;
import org.ejml.masks.MaskBuilder;
import org.ejml.ops.FSemiRing;
import org.ejml.ops.FSemiRings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.ejml.TestFMaskUtil.assertMaskedResult;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("UnusedMethod")
@Generated("org.ejml.sparse.csc.mult.TestMatrixVectorMultWithSemiRing_DSCC")
public class TestMatrixVectorMultWithSemiRing_FSCC extends EjmlStandardJUnit {
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
    @MethodSource("vectorMatrixMultSources")
    void mult_v_A( String desc, FSemiRing semiRing, float[] expected ) {
        // graphblas == following outgoing edges of source nodes
        float[] v = new float[7];
        Arrays.fill(v, semiRing.add.id);
        v[3] = 0.5f;
        v[5] = 0.6f;

        float[] found = new float[7];

        MatrixVectorMultWithSemiRing_FSCC.mult(v, inputMatrix, found, semiRing, null);

        assertTrue(Arrays.equals(found, expected));
    }

    @ParameterizedTest
    @MethodSource("maskedInputSources")
    void mult_v_A_masked( float[] vector, Mask mask ) {
        var semiRing = FSemiRings.OR_AND;

        float[] found = new float[7];
        float[] foundMasked = new float[7];
        MatrixVectorMultWithSemiRing_FSCC.mult(vector, inputMatrix, found, semiRing, null);

        MatrixVectorMultWithSemiRing_FSCC.mult(vector, inputMatrix, foundMasked, semiRing, mask);

        float[] expected = {1, 1, 1, 1, 0, 0, 0};
        assertArrayEquals(found, expected);
        assertMaskedResult(found, foundMasked, mask);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matrixVectorMultSources")
    void mult_A_v( String desc, FSemiRing semiRing, float[] expected ) {
        // graphblas == following incoming edges of source nodes
        float[] v = new float[7];
        Arrays.fill(v, semiRing.add.id);
        v[3] = 0.5f;
        v[4] = 0.6f;

        float[] found = new float[7];

        MatrixVectorMultWithSemiRing_FSCC.mult(inputMatrix, v, found, semiRing, null);

        assertTrue(Arrays.equals(found, expected));
    }

    @ParameterizedTest
    @MethodSource("maskedInputSources")
    void mult_A_v_masked( float[] vector, Mask mask ) {
        var semiRing = FSemiRings.OR_AND;

        float[] found = new float[7];
        float[] foundMasked = new float[7];

        MatrixVectorMultWithSemiRing_FSCC.mult(inputMatrix, vector, found, semiRing, null);
        MatrixVectorMultWithSemiRing_FSCC.mult(inputMatrix, vector, foundMasked, semiRing, mask);

        float[] expected = {1, 0, 0, 1, 0, 0, 1};
        assertArrayEquals(found, expected);
        assertMaskedResult(found, foundMasked, mask);
    }

    private static Stream<Arguments> vectorMatrixMultSources() {
        return Stream.of(
                Arguments.of("Plus, Times", FSemiRings.PLUS_TIMES, new float[]{0.1f, 0, 0.5f, 0, 0, 0, 0}),
                Arguments.of("OR, AND", FSemiRings.OR_AND, new float[]{1, 0, 1, 0, 0, 0, 0}),
                Arguments.of("MIN, PLUS", FSemiRings.MIN_PLUS,
                        new float[]{0.7f, Float.MAX_VALUE, 0.9f, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE}),
                Arguments.of("MIN, TIMES", FSemiRings.MIN_TIMES,
                        new float[]{0.1f, Float.MAX_VALUE, 0.2f, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE}),
                Arguments.of("MAX, MIN", FSemiRings.MAX_MIN,
                        new float[]{0.2f, -Float.MAX_VALUE, 0.5f, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE})
        );
    }

    private static Stream<Arguments> matrixVectorMultSources() {
        return Stream.of(
                Arguments.of("PLUS, TIMES", FSemiRings.PLUS_TIMES, new float[]{0.5f, 0.6f, 0, 0, 0, 0, 1.1f}),
                Arguments.of("OR, AND", FSemiRings.OR_AND, new float[]{1, 1, 0, 0, 0, 0, 1}),
                Arguments.of("MIN, PLUS", FSemiRings.MIN_PLUS,
                        new float[]{1.5f, 1.6f, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, 1.5f})
        );
    }

    private static Stream<Arguments> maskedInputSources() {
        int vectorLength = 7;
        float[] v = new float[vectorLength];
        v[0] = 0.5f;
        v[3] = 0.6f;

        FMatrixSparseCSC sparseVector = new FMatrixSparseCSC(vectorLength, 1);
        sparseVector.set(0, 0, 0.5f);
        sparseVector.set(3, 0, 0.6f);

        Stream<MaskBuilder> maskBuilders = Stream.of(
                FMaskFactory.builder(v),
                FMaskFactory.builder(sparseVector, true),
                FMaskFactory.builder(sparseVector, false)
        );

        return maskBuilders.map(builder -> Arguments.of(v, builder.withNegated(true).build()));
    }
}
