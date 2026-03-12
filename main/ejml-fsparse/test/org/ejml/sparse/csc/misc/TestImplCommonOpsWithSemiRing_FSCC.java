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

package org.ejml.sparse.csc.misc;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.masks.FMaskFactory;
import org.ejml.ops.FSemiRing;
import org.ejml.ops.FSemiRings;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.ejml.TestFMaskUtil.assertMaskedResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"UnusedMethod"})
@Generated("org.ejml.sparse.csc.misc.TestImplCommonOpsWithSemiRing_DSCC")
public class TestImplCommonOpsWithSemiRing_FSCC extends EjmlStandardJUnit {

    @ParameterizedTest
    @MethodSource("elementWiseAddSemiringSource")
    public void add( FSemiRing semiRing, float[] expected ) {
        // == graph unions
        FMatrixSparseCSC a = new FMatrixSparseCSC(3, 3);
        FMatrixSparseCSC b = a.copy();
        FMatrixSparseCSC c = a.copy();

        a.set(1, 1, 2);
        b.set(1, 1, 3);
        b.set(0, 0, 4);

        ImplCommonOpsWithSemiRing_FSCC.add(1, a, 1, b, c, semiRing, null, null, null);

        float[] found = new float[]{c.get(0, 0), c.get(1, 1)};

        assertTrue(c.getNonZeroLength() == 2);
        assertTrue(Arrays.equals(expected, found));
    }

    @Test
    public void maskedAdd() {
        var random = new Random(42);
        var a = RandomMatrices_FSCC.rectangle(10, 10, 30, random);
        var b = RandomMatrices_FSCC.rectangle(10, 10, 30, random);
        var mask = FMaskFactory.builder(RandomMatrices_FSCC.rectangle(10, 10, 30, random), true).build();

        var unmasked = new FMatrixSparseCSC(10, 10, 0);
        var masked = new FMatrixSparseCSC(10, 10, 0);

        ImplCommonOpsWithSemiRing_FSCC.add(1, a, 1, b, unmasked, FSemiRings.PLUS_TIMES, null, null, null);
        ImplCommonOpsWithSemiRing_FSCC.add(1, a, 1, b, masked, FSemiRings.PLUS_TIMES, mask, null, null);


        assertMaskedResult(unmasked, masked, mask);
    }

    @Test
    public void useMaskForResultExpansionInAdd() {
        var random = new Random(42);
        var a = RandomMatrices_FSCC.rectangle(10, 10, 100, random);
        var b = RandomMatrices_FSCC.rectangle(10, 10, 100, random);
        int expectedResultEntries = 50;
        var mask = FMaskFactory.builder(RandomMatrices_FSCC.rectangle(10, 10, expectedResultEntries, random), false).build();

        var result = new FMatrixSparseCSC(10, 10, 1);

        ImplCommonOpsWithSemiRing_FSCC.add(1, a, 1, b, result, FSemiRings.PLUS_TIMES, mask, null, null);

        assertEquals(expectedResultEntries, result.nz_length);
        assertEquals(50, result.nz_rows.length);
        assertEquals(50, result.nz_values.length);
    }

    @Test
    public void useMaskForResultExpansionInMult() {
        var random = new Random(42);
        var a = RandomMatrices_FSCC.rectangle(10, 10, 100, random);
        var b = RandomMatrices_FSCC.rectangle(10, 10, 100, random);
        int expectedResultEntries = 50;
        var mask = FMaskFactory.builder(RandomMatrices_FSCC.rectangle(10, 10, expectedResultEntries, random), false).build();

        var result = new FMatrixSparseCSC(10, 10, 1);

        ImplCommonOpsWithSemiRing_FSCC.elementMult(a, b, result, FSemiRings.PLUS_TIMES, mask, null, null);

        assertEquals(expectedResultEntries, result.nz_length);
        assertEquals(50, result.nz_rows.length);
        assertEquals(50, result.nz_values.length);
    }

    @ParameterizedTest
    @MethodSource("elementWiseMultSemiringSource")
    public void elementWiseMult( FSemiRing semiRing, float[] expected ) {
        // == graph intersection
        FMatrixSparseCSC matrix = new FMatrixSparseCSC(3, 3, 4);
        matrix.set(1, 1, 4);
        matrix.set(1, 2, -2);

        FMatrixSparseCSC otherMatrix = matrix.copy();
        otherMatrix.set(1, 1, 3);
        otherMatrix.set(1, 2, 1);

        matrix.set(0, 2, 1);
        otherMatrix.set(2, 0, 1);

        FMatrixSparseCSC result = new FMatrixSparseCSC(3, 3, 0);

        ImplCommonOpsWithSemiRing_FSCC.elementMult(matrix, otherMatrix, result, semiRing, null, null, null);

        assertEquals(2, result.getNonZeroLength());
        assertTrue(expected[0] == result.get(1, 1));
        assertTrue(expected[1] == result.get(1, 2));
    }

    @Test
    public void maskedeWiseMult() {
        var random = new Random(42);
        var a = RandomMatrices_FSCC.rectangle(10, 10, 30, random);
        var b = RandomMatrices_FSCC.rectangle(10, 10, 30, random);
        var mask = FMaskFactory.builder(RandomMatrices_FSCC.rectangle(10, 10, 30, random), true).build();

        var unmasked = new FMatrixSparseCSC(10, 10, 0);
        var masked = new FMatrixSparseCSC(10, 10, 0);

        ImplCommonOpsWithSemiRing_FSCC.elementMult(a, b, unmasked, FSemiRings.PLUS_TIMES, null, null, null);
        ImplCommonOpsWithSemiRing_FSCC.elementMult(a, b, masked, FSemiRings.PLUS_TIMES, mask, null, null);


        assertMaskedResult(unmasked, masked, mask);
    }

    private static Stream<Arguments> elementWiseAddSemiringSource() {
        return Stream.of(
                Arguments.of(FSemiRings.PLUS_TIMES, new float[]{4, 5}),
                Arguments.of(FSemiRings.MIN_MAX, new float[]{4, 2}),
                Arguments.of(FSemiRings.OR_AND, new float[]{1, 1})
        );
    }

    private static Stream<Arguments> elementWiseMultSemiringSource() {
        return Stream.of(
                Arguments.of(FSemiRings.PLUS_TIMES, new float[]{12, -2}),
                Arguments.of(FSemiRings.PLUS_MIN, new float[]{3, -2}),
                Arguments.of(FSemiRings.MIN_MAX, new float[]{4, 1}),
                Arguments.of(FSemiRings.OR_AND, new float[]{1, 1})
        );
    }
}
