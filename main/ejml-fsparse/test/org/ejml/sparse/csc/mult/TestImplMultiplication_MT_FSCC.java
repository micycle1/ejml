/*
 * Copyright (c) 2023, Peter Abeles. All Rights Reserved.
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
import org.ejml.UtilEjml;
import org.ejml.data.FGrowArray;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.ops.FConvertMatrixStruct;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.MatrixFeatures_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.junit.jupiter.api.Test;
import pabeles.concurrency.GrowArray;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.mult.TestImplMultiplication_MT_DSCC")
class TestImplMultiplication_MT_FSCC extends EjmlStandardJUnit {
    private final GrowArray<FGrowArray> workArrays = new GrowArray<>(FGrowArray::new);
    private final GrowArray<Workspace_MT_FSCC> workSpaceMT = new GrowArray<>(Workspace_MT_FSCC::new);

    @Test void mult_s_s() {
        for (int i = 0; i < 50; i++) {
            mult_s_s(5, 5, 5);
            mult_s_s(10, 5, 5);
            mult_s_s(5, 10, 5);
            mult_s_s(5, 5, 10);
        }

        // See comment in mult_s_s. This triggered a bug
        mult_s_s(10, 10, 0);
    }


    private void mult_s_s( int rowsA, int colsA, int colsB ) {
        int nz_a = RandomMatrices_FSCC.nonzero(rowsA, colsA, 0.05f, 0.7f, rand);
        int nz_b = RandomMatrices_FSCC.nonzero(colsA, colsB, 0.05f, 0.7f, rand);
        int nz_c = RandomMatrices_FSCC.nonzero(rowsA, colsB, 0.05f, 0.7f, rand);

        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(rowsA, colsA, nz_a, -1, 1, rand);
        FMatrixSparseCSC b = RandomMatrices_FSCC.rectangle(colsA, colsB, nz_b, -1, 1, rand);
        FMatrixSparseCSC expected = RandomMatrices_FSCC.rectangle(rowsA, colsB, nz_c, -1, 1, rand);
        FMatrixSparseCSC found = expected.copy();

        // Make sure the work space is cleaned up. There was a bug where if b has zero columns stitching would
        // throw an exception if this wasn't empty
        workSpaceMT.grow();

        ImplMultiplication_FSCC.mult(a, b, expected, null, null);
        ImplMultiplication_MT_FSCC.mult(a, b, found, workSpaceMT);
        assertTrue(CommonOps_FSCC.checkStructure(found));

        assertTrue(MatrixFeatures_FSCC.isEqualsSort(expected, found, UtilEjml.TEST_F32));
    }

    @Test void mult_s_d() {
        for (int i = 0; i < 10; i++) {
            mult_s_d(24, false);
            mult_s_d(15, false);
            mult_s_d(4, false);
            mult_s_d(24, true);
            mult_s_d(15, true);
            mult_s_d(4, true);
        }
    }

    private void mult_s_d( int elementsA, boolean add ) {
        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(4, 6, elementsA, -1, 1, rand);
        FMatrixRMaj b = RandomMatrices_FDRM.rectangle(6, 5, -1, 1, rand);
        FMatrixRMaj c = RandomMatrices_FDRM.rectangle(4, 5, -1, 1, rand);
        FMatrixRMaj expected_c = c.copy();
        FMatrixRMaj dense_a = FConvertMatrixStruct.convert(a, (FMatrixRMaj)null);

        GrowArray<FGrowArray> work = new GrowArray<>(FGrowArray::new);

        if (add) {
            ImplMultiplication_MT_FSCC.multAdd(a, b, c, work);
            CommonOps_FDRM.multAdd(dense_a, b, expected_c);
        } else {
            ImplMultiplication_MT_FSCC.mult(a, b, c, work);
            CommonOps_FDRM.mult(dense_a, b, expected_c);
        }

        for (int row = 0; row < c.numRows; row++) {
            for (int col = 0; col < c.numCols; col++) {
                assertEquals(expected_c.get(row, col), c.get(row, col), UtilEjml.TEST_F32, row + " " + col);
            }
        }
    }

    @Test void multTransA_s_d() {
        multTransA_s_d(5, 5, 5);
        multTransA_s_d(10, 5, 5);
        multTransA_s_d(5, 10, 5);
        multTransA_s_d(5, 5, 10);
    }

    private void multTransA_s_d( int rowsA, int colsA, int colsB ) {
        int nz_a = RandomMatrices_FSCC.nonzero(rowsA, colsA, 0.05f, 0.7f, rand);

        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(colsA, rowsA, nz_a, -1, 1, rand);
        FMatrixRMaj b = RandomMatrices_FDRM.rectangle(colsA, colsB, -1, 1, rand);
        FMatrixRMaj expected = RandomMatrices_FDRM.rectangle(rowsA, colsB, -1, 1, rand);
        FMatrixRMaj found = expected.copy();

        ImplMultiplication_FSCC.multTransA(a, b, expected, workArrays.grow());
        ImplMultiplication_MT_FSCC.multTransA(a, b, found, workArrays);

        assertTrue(MatrixFeatures_FDRM.isEquals(expected, found, UtilEjml.TEST_F32));
    }

    @Test void multAddTransA_s_d() {
        multAddTransA_s_d(5, 5, 5);
        multAddTransA_s_d(10, 5, 5);
        multAddTransA_s_d(5, 10, 5);
        multAddTransA_s_d(5, 5, 10);
    }

    private void multAddTransA_s_d( int rowsA, int colsA, int colsB ) {
        int nz_a = RandomMatrices_FSCC.nonzero(rowsA, colsA, 0.05f, 0.7f, rand);

        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(colsA, rowsA, nz_a, -1, 1, rand);
        FMatrixRMaj b = RandomMatrices_FDRM.rectangle(colsA, colsB, -1, 1, rand);
        FMatrixRMaj expected = RandomMatrices_FDRM.rectangle(rowsA, colsB, -1, 1, rand);
        FMatrixRMaj found = expected.copy();

        ImplMultiplication_FSCC.multAddTransA(a, b, expected, workArrays.grow());
        ImplMultiplication_MT_FSCC.multAddTransA(a, b, found, workArrays);

        assertTrue(MatrixFeatures_FDRM.isEquals(expected, found, UtilEjml.TEST_F32));
    }

    @Test void multTransB_s_d() {
        for (int i = 0; i < 10; i++) {
            multTransB_s_d(24, false);
            multTransB_s_d(15, false);
            multTransB_s_d(4, false);

            multTransB_s_d(24, true);
            multTransB_s_d(15, true);
            multTransB_s_d(4, true);
        }
    }

    private void multTransB_s_d( int elementsA, boolean add ) {
        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(4, 6, elementsA, -1, 1, rand);
        FMatrixRMaj b = RandomMatrices_FDRM.rectangle(5, 6, -1, 1, rand);
        FMatrixRMaj c = RandomMatrices_FDRM.rectangle(4, 5, -1, 1, rand);
        FMatrixRMaj expected_c = c.copy();
        FMatrixRMaj dense_a = FConvertMatrixStruct.convert(a, (FMatrixRMaj)null);

        GrowArray<FGrowArray> work = new GrowArray<>(FGrowArray::new);

        if (add) {
            ImplMultiplication_MT_FSCC.multAddTransB(a, b, c, work);
            CommonOps_FDRM.multAddTransB(dense_a, b, expected_c);
        } else {
            ImplMultiplication_MT_FSCC.multTransB(a, b, c, false, work);
            CommonOps_FDRM.multTransB(dense_a, b, expected_c);
        }
        for (int row = 0; row < c.numRows; row++) {
            for (int col = 0; col < c.numCols; col++) {
                assertEquals(expected_c.get(row, col), c.get(row, col), UtilEjml.TEST_F32, row + " " + col);
            }
        }
    }

    @Test void multTransAB_s_d() {
        for (int i = 0; i < 10; i++) {
            multTransAB_s_d(24, false);
            multTransAB_s_d(15, false);
            multTransAB_s_d(4, false);

            multTransAB_s_d(24, true);
            multTransAB_s_d(15, true);
            multTransAB_s_d(4, true);
        }
    }

    private void multTransAB_s_d( int elementsA, boolean add ) {
        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(6, 4, elementsA, -1, 1, rand);
        FMatrixRMaj b = RandomMatrices_FDRM.rectangle(5, 6, -1, 1, rand);
        FMatrixRMaj c = RandomMatrices_FDRM.rectangle(4, 5, -1, 1, rand);
        FMatrixRMaj expected_c = c.copy();
        FMatrixRMaj dense_a = FConvertMatrixStruct.convert(a, (FMatrixRMaj)null);

        if (add) {
            ImplMultiplication_MT_FSCC.multAddTransAB(a, b, c);
            CommonOps_FDRM.multAddTransAB(dense_a, b, expected_c);
        } else {
            ImplMultiplication_MT_FSCC.multTransAB(a, b, c);
            CommonOps_FDRM.multTransAB(dense_a, b, expected_c);
        }

        for (int row = 0; row < c.numRows; row++) {
            for (int col = 0; col < c.numCols; col++) {
                assertEquals(expected_c.get(row, col), c.get(row, col), UtilEjml.TEST_F32, row + " " + col);
            }
        }
    }
}