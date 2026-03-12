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
import org.ejml.EjmlUnitTests;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.data.FMatrixSparseTriplet;
import org.ejml.data.IGrowArray;
import org.ejml.ops.FConvertMatrixStruct;
import org.ejml.ops.IPredicateBinary;
import org.ejml.ops.IPredicatesBinary;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.MatrixFeatures_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.ejml.sparse.triplet.RandomMatrices_FSTL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.misc.TestImplCommonOps_DSCC")
public class TestImplCommonOps_FSCC extends EjmlStandardJUnit {
    @Test
    public void transpose() {
        for (int rows = 1; rows <= 10; rows++) {
            for (int cols = 1; rows <= 10; rows++) {
                for (int mc = 0; mc < 20; mc++) {
                    int N = (int)Math.round(rows*cols*rand.nextFloat());
                    FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(rows, cols, N, -1, 1, rand);
                    FMatrixSparseCSC b = new FMatrixSparseCSC(0, 0, 0);

                    ImplCommonOps_FSCC.transpose(a, b, null);
                    assertEquals(cols, b.numRows);
                    assertEquals(rows, b.numCols);

                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < cols; col++) {
                            float expected = a.get(row, col);
                            float found = b.get(col, row);

                            assertEquals(expected, found, UtilEjml.TEST_F32, row + " " + col);
                        }
                    }
                    assertTrue(CommonOps_FSCC.checkSortedFlag(b));
                }
            }
        }
    }

    @Test
    public void add() {
        float alpha = 1.5f;
        float beta = 2.3f;

        for (int numRows : new int[]{2, 4, 6, 10}) {
            for (int numCols : new int[]{2, 4, 6, 10}) {
                FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(numRows, numCols, 7, -1, 1, rand);
                FMatrixSparseCSC b = RandomMatrices_FSCC.rectangle(numRows, numCols, 8, -1, 1, rand);
                FMatrixSparseCSC c = RandomMatrices_FSCC.rectangle(numRows, numCols, 3, -1, 1, rand);

                ImplCommonOps_FSCC.add(alpha, a, beta, b, c, null, null);

                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        float valA = a.get(row, col);
                        float valB = b.get(row, col);
                        float found = alpha*valA + beta*valB;

                        float expected = c.get(row, col);

                        assertEquals(expected, found, UtilEjml.TEST_F32, row + " " + col);
                    }
                }
                assertTrue(CommonOps_FSCC.checkStructure(c));
            }
        }
    }

    @Test
    public void addColAppend() {
        float alpha = 1.5f;
        float beta = 2.3f;

        for (int numRows : new int[]{2, 4, 6, 10}) {
            FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(numRows, 3, 15, -1, 1, rand);
            FMatrixSparseCSC b = RandomMatrices_FSCC.rectangle(numRows, 4, 15, -1, 1, rand);
            FMatrixSparseCSC c = new FMatrixSparseCSC(numRows, 0, 0);


            ImplCommonOps_FSCC.addColAppend(alpha, a, 1, beta, b, 0, c, null);
            assertTrue(CommonOps_FSCC.checkStructure(c));
            assertEquals(1, c.numCols);

            for (int row = 0; row < numRows; row++) {
                float valA = a.get(row, 1);
                float valB = b.get(row, 0);
                float found = alpha*valA + beta*valB;

                float expected = c.get(row, 0);
                assertEquals(expected, found, UtilEjml.TEST_F32);
            }

            ImplCommonOps_FSCC.addColAppend(alpha, a, 2, beta, b, 1, c, null);
            assertTrue(CommonOps_FSCC.checkStructure(c));
            assertEquals(2, c.numCols);

            for (int row = 0; row < numRows; row++) {
                float valA = a.get(row, 2);
                float valB = b.get(row, 1);
                float found = alpha*valA + beta*valB;

                float expected = c.get(row, 1);
                assertEquals(expected, found, UtilEjml.TEST_F32);
            }
        }
    }

    @Test
    public void removeZeros_two() {
        FMatrixSparseCSC A = new FMatrixSparseCSC(2, 3, 2);
        A.indicesSorted = true;
        A.set(0, 1, 0.1f);
        A.set(1, 1, 0.0f);
        A.set(1, 2, 0.3f);
        FMatrixSparseCSC A_orig = A.copy();

        FMatrixSparseCSC B = new FMatrixSparseCSC(1, 1, 1);

        ImplCommonOps_FSCC.removeZeros(A, B, 0);
        assertTrue(CommonOps_FSCC.checkStructure(B));
        assertEquals(A.numRows, B.numRows);
        assertEquals(A.numCols, B.numCols);
        assertEquals(2, B.nz_length);
        EjmlUnitTests.assertEquals(A, B, UtilEjml.TEST_F32);
        assertTrue(MatrixFeatures_FSCC.isEquals(A, A_orig, UtilEjml.TEST_F32));

        ImplCommonOps_FSCC.removeZeros(A, B, 0.1f);
        assertEquals(1, B.nz_length);
        assertEquals(0, B.get(0, 1), UtilEjml.TEST_F32);
        assertEquals(A.get(1, 1), B.get(1, 1), UtilEjml.TEST_F32);
        assertTrue(MatrixFeatures_FSCC.isEquals(A, A_orig, UtilEjml.TEST_F32));
    }

    @Test
    public void removeZeros_one() {
        FMatrixSparseCSC A = new FMatrixSparseCSC(2, 3, 2);
        A.set(0, 1, 0.1f);
        A.set(1, 1, 0.0f);
        A.set(1, 2, 0.3f);

        ImplCommonOps_FSCC.removeZeros(A, 0);
        assertTrue(CommonOps_FSCC.checkStructure(A));
        assertEquals(A.numRows, 2);
        assertEquals(A.numCols, 3);
        assertEquals(2, A.nz_length);
        assertEquals(0.1f, A.get(0, 1), UtilEjml.TEST_F32);
        assertEquals(0.3f, A.get(1, 2), UtilEjml.TEST_F32);

        ImplCommonOps_FSCC.removeZeros(A, 0.1f);
        assertTrue(CommonOps_FSCC.checkStructure(A));
        assertEquals(A.numRows, 2);
        assertEquals(A.numCols, 3);
        assertEquals(1, A.nz_length);
        assertEquals(0.3f, A.get(1, 2), UtilEjml.TEST_F32);
    }

    @Test
    public void removeZeros_one_random() {
        for (int i = 0; i < 40; i++) {
            int rows = rand.nextInt(10) + 1;
            int cols = rand.nextInt(10) + 1;

            int nz = RandomMatrices_FSCC.nonzero(rows, cols, 0.02f, 0.5f, rand);
            FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(rows, cols, nz, -1, 1, rand);

            ImplCommonOps_FSCC.removeZeros(A, 0.2f);
            assertTrue(CommonOps_FSCC.checkStructure(A));
            assertEquals(A.numRows, rows);
            assertEquals(A.numCols, cols);
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < cols; k++) {
                    float val = A.get(j, k);
                    assertTrue(Math.abs(val) > 0.2f || val == 0, "val = " + val);
                }
            }
        }
    }

    @Test
    public void symmLowerToFull() {
        IGrowArray gw = new IGrowArray();
        int[] sizes = {1, 2, 5, 10};

        for (int i = 0; i < 20; i++) {
            for (int N : sizes) {
                int nz = RandomMatrices_FSCC.nonzero(N, N/2, 0.1f, 1.0f, rand);
                FMatrixSparseCSC A = RandomMatrices_FSCC.triangleLower(N, 0, nz, -1, 1, rand);
                FMatrixSparseCSC B = new FMatrixSparseCSC(0, 0);

                ImplCommonOps_FSCC.symmLowerToFull(A, B, gw);
                assertTrue(CommonOps_FSCC.checkStructure(B));

                for (int row = 0; row < N; row++) {
                    for (int col = 0; col <= row; col++) {
//                        System.out.println(row+" "+col);
                        assertEquals(A.get(row, col), B.get(row, col), UtilEjml.TEST_F32);
                        assertEquals(A.get(row, col), B.get(col, row), UtilEjml.TEST_F32);
                    }
                }
            }
        }
    }

    @Test
    void duplicatesAdd() {
        IGrowArray gw = new IGrowArray();
        int[] sizes = {1, 2, 5, 10};
        for (int i = 0; i < 20; i++) {
            for (int N : sizes) {
                FMatrixSparseTriplet triplet = RandomMatrices_FSTL.uniform(N, N, (N*N)/2, -1, 1, rand);
                FMatrixSparseCSC A = FConvertMatrixStruct.convert(triplet, (FMatrixSparseCSC)null);
                FMatrixSparseCSC B = A.copy();

                // first pass there should be no change
                ImplCommonOps_FSCC.duplicatesAdd(B, null);
                assertTrue(CommonOps_FSCC.checkStructure(B));
                A.sortIndices(null);
                B.sortIndices(null);
                assertTrue(MatrixFeatures_FSCC.isEquals(A, B));

                // Second pass there will be a lot of duplicates
                int nz = triplet.nz_length;
                for (int j = 0; j < nz; j++) {
                    int row = triplet.nz_rowcol.data[j*2];
                    int col = triplet.nz_rowcol.data[j*2 + 1];
                    float value = triplet.nz_value.data[j];
                    triplet.addItem(row, col, value);
                }
                FConvertMatrixStruct.convert(triplet, B);
                // Remove duplicates and see if B has elements that are twice the size of elements in A
                ImplCommonOps_FSCC.duplicatesAdd(B, gw);
                assertTrue(CommonOps_FSCC.checkStructure(B));
                CommonOps_FSCC.divide(B, 2.0f, B);
                B.sortIndices(null);
                assertTrue(MatrixFeatures_FSCC.isEquals(A, B, UtilEjml.TEST_F32));
            }
        }
    }

    @Test
    public void select() {
        int dim = 5;
        FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(dim, dim, 10, 1, 2, rand);
        FMatrixSparseCSC B = A.copy();

        IPredicateBinary selector = IPredicatesBinary.higherTriangle;
        ImplCommonOps_FSCC.select(B, B, selector);

        for (int row = 0; row < dim; row++) {
            for (int col = 0; col < dim; col++) {
                if (selector.apply(row, col)) {
                    assertEquals(A.get(row, col), B.get(row, col));
                } else {
                    assertFalse(B.isAssigned(row, col));
                }
            }
        }
    }
}
