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

package org.ejml.sparse.csc;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixSparseCSC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@SuppressWarnings("UnusedMethod")
@Generated("org.ejml.sparse.csc.TestRandomMatrices_DSCC")
public class TestRandomMatrices_FSCC extends EjmlStandardJUnit {
    @Test
    void uniform() {
        int numRows = 6;
        int numCols = 7;

        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(numRows, numCols, 10, -1, 1, rand);

        assertEquals(numRows, a.numRows);
        assertEquals(numCols, a.numCols);
        assertEquals(10, a.nz_length);
        assertTrue(CommonOps_FSCC.checkStructure(a));

        int count = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                float value = a.get(row, col);

                if (value == 0)
                    continue;
                if (value > 1 || value < -1)
                    fail("Out of expected range");
                count++;
            }
        }

        assertEquals(10, count);
        assertTrue(CommonOps_FSCC.checkSortedFlag(a));
    }

    /**
     * There was a bug where the rows and columns multiplied together caused an overflow
     */
    @Test
    void rectangle_large() {
        assertThrows(IllegalArgumentException.class, () ->
                RandomMatrices_FSCC.rectangle(1_000_000, 1_000_000, 10, -1, 1, rand));
    }

    private static Stream<Arguments> randomMatrixDimensions() {
        int[] rowCounts = {1, 10, 15, 100, 1000};
        int[] colCounts = {1, 10, 15, 100, 1000};
        float[] densities = {1, 0.8f, 0.2f, 0.01f};

        Stream.Builder<Arguments> streamBuilder = Stream.builder();

        for (int rowCount : rowCounts) {
            for (int colCount : colCounts) {
                for (float density : densities) {
                    streamBuilder.accept(Arguments.of(rowCount, colCount, (int)Math.round(density*rowCount)));
                }
            }
        }

        return streamBuilder.build();
    }

    @ParameterizedTest
    @MethodSource("randomMatrixDimensions")
    void generateUniform( int numRows, int numCols, int entriesPerColumn ) {
        FMatrixSparseCSC a = RandomMatrices_FSCC.generateUniform(numRows, numCols, entriesPerColumn, -1, 1, rand);

        assertEquals(entriesPerColumn*numCols, a.nz_length);
        assertTrue(CommonOps_FSCC.checkStructure(a));
    }

    @Test
    void createLowerTriangular() {
        FMatrixSparseCSC L;
        for (int trial = 0; trial < 20; trial++) {
            for (int length : new int[]{0, 2, 6, 12, 20}) {
                L = RandomMatrices_FSCC.triangleLower(6, 0, length, -1, 1, rand);

                assertEquals(Math.max(6, length), L.nz_length);
                assertTrue(CommonOps_FSCC.checkStructure(L));
                assertTrue(MatrixFeatures_FSCC.isLowerTriangle(L, 0, 0.0f));

                L = RandomMatrices_FSCC.triangleLower(6, 1, length, -1, 1, rand);
                assertEquals(Math.max(5, length), L.nz_length);
                assertTrue(CommonOps_FSCC.checkStructure(L));
                assertTrue(MatrixFeatures_FSCC.isLowerTriangle(L, 1, 0.0f));

                assertFalse(CommonOps_FSCC.checkDuplicateElements(L));
            }
        }
    }

    @Test
    void symmetric() {
        for (int N = 1; N <= 10; N++) {
            for (int mc = 0; mc < 30; mc++) {
                int nz = (int)(N*N*0.5f*(rand.nextFloat()*0.5f + 0.1f) + 0.5f);
                nz = Math.max(1, nz);
                FMatrixSparseCSC A = RandomMatrices_FSCC.symmetric(N, nz, -1, 1, rand);

                assertTrue(CommonOps_FSCC.checkStructure(A));

                // Sanity check to see if it's obeying the requested number of non-zero elements
                assertTrue(A.nz_length >= nz && A.nz_length <= 2*nz);

                // Check the matrix properties
                assertTrue(MatrixFeatures_FSCC.isSymmetric(A, UtilEjml.TEST_F32));
            }
        }
    }

    /**
     * There was a bug where the rows and columns multiplied together caused an overflow
     */
    @Test
    void symmetric_large() {
        assertThrows(IllegalArgumentException.class, () ->
                RandomMatrices_FSCC.symmetric(1_000_000, 10, -1, 1, rand));
    }

    @Test
    void symmetricPosDef() {
        float probabilityZero = 0.25f;

        for (int N = 1; N <= 10; N++) {
            for (int mc = 0; mc < 30; mc++) {
                FMatrixSparseCSC A = RandomMatrices_FSCC.symmetricPosDef(N, probabilityZero, rand);

                assertTrue(CommonOps_FSCC.checkStructure(A));

                // The upper limit is  bit fuzzy. This really just checks to see if it's exceeded by an extreme amount
                assertTrue(A.nz_length <= (int)Math.ceil(N*N*(1.0f - probabilityZero)) + N);

                // Extremely crude check to see if the size is above a lower limit. In theory it could be full of
                // zeros and that would still be valid.
                assertTrue(A.nz_length >= N*N*(probabilityZero/5.0f));

                assertTrue(MatrixFeatures_FSCC.isPositiveDefinite(A));
            }
        }
    }
}
