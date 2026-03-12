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
import org.ejml.data.FGrowArray;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.ops.FConvertMatrixStruct;
import org.junit.jupiter.api.Test;
import pabeles.concurrency.GrowArray;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.TestCommonOps_MT_DSCC")
class TestCommonOps_MT_FSCC extends EjmlStandardJUnit {
    private GrowArray<FGrowArray> growArray = new GrowArray<>(FGrowArray::new);

    @Test void mult_s_s_shapes() {
        // multiple trials to test more sparse structures
        for (int trial = 0; trial < 50; trial++) {
            check_s_s_mult(
                    RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand),
                    RandomMatrices_FSCC.rectangle(5, 4, 7, rand), false);

            check_s_s_mult(
                    RandomMatrices_FSCC.rectangle(5, 7, 5, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand),
                    RandomMatrices_FSCC.rectangle(5, 5, 7, rand), true);
            check_s_s_mult(
                    RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand),
                    RandomMatrices_FSCC.rectangle(5, 5, 7, rand), false);
            check_s_s_mult(
                    RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand), false);
            check_s_s_mult(
                    RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand),
                    RandomMatrices_FSCC.rectangle(6, 4, 7, rand), false);
        }
    }

    private void check_s_s_mult( FMatrixSparseCSC A, FMatrixSparseCSC B, FMatrixSparseCSC C, boolean exception ) {
        FMatrixSparseCSC expected = C.createLike();

        try {
            CommonOps_MT_FSCC.mult(A, B, C, null);
            assertTrue(CommonOps_FSCC.checkStructure(C));

            if (exception)
                fail("exception expected");

            CommonOps_FSCC.mult(A, B, expected);

            assertTrue(MatrixFeatures_FSCC.isEqualsSort(expected, C, UtilEjml.TEST_F32));
        } catch (RuntimeException e) {
            if (!exception)
                fail("no exception expected. " + e.getMessage());
        }
    }

    @Test void add_shapes() {
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand), false);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 5*6, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5*6, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5*6, rand), false);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 0, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 0, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 0, rand), false);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 20, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 16, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 0, rand), false);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 5, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand), true);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 5, 5, rand), false);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(4, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand), true);
        check_add(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FSCC.rectangle(4, 6, 5, rand), false);
    }

    private void check_add( FMatrixSparseCSC A, FMatrixSparseCSC B, FMatrixSparseCSC C, boolean exception ) {
        float alpha = 1.5f;
        float beta = -0.6f;
        try {
            CommonOps_MT_FSCC.add(alpha, A, beta, B, C, null);
            assertTrue(CommonOps_FSCC.checkStructure(C));

            if (exception)
                fail("exception expected");

            FMatrixSparseCSC expected = C.createLike();

            CommonOps_FSCC.add(alpha, A, beta, B, expected, null, null);

            assertTrue(MatrixFeatures_FSCC.isEqualsSort(expected, C, UtilEjml.TEST_F32));
        } catch (RuntimeException ignore) {
            if (!exception)
                fail("no exception expected");
        }
    }

    @Test void mult_s_d_shapes() {
        check_s_d_mult(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FDRM.rectangle(6, 4, rand),
                RandomMatrices_FDRM.rectangle(5, 4, rand), false);

        check_s_d_mult(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FDRM.rectangle(7, 4, rand),
                RandomMatrices_FDRM.rectangle(5, 4, rand), true);

        // Matrix C is resized
        check_s_d_mult(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FDRM.rectangle(6, 4, rand),
                RandomMatrices_FDRM.rectangle(5, 5, rand), false);
        check_s_d_mult(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FDRM.rectangle(6, 4, rand),
                RandomMatrices_FDRM.rectangle(6, 4, rand), false);
        check_s_d_mult(
                RandomMatrices_FSCC.rectangle(5, 6, 5, rand),
                RandomMatrices_FDRM.rectangle(6, 4, rand),
                RandomMatrices_FDRM.rectangle(6, 4, rand), false);
    }

    private void check_s_d_mult( FMatrixSparseCSC A, FMatrixRMaj B, FMatrixRMaj C, boolean exception ) {
        FMatrixRMaj denseA = FConvertMatrixStruct.convert(A, (FMatrixRMaj)null);
        FMatrixRMaj expected = C.copy();

        FMatrixSparseCSC A_t = CommonOps_FSCC.transpose(A, null, null);
        FMatrixRMaj B_t = CommonOps_FDRM.transpose(B, null);
        FMatrixRMaj denseA_t = CommonOps_FDRM.transpose(denseA, null);

        for (int i = 0; i < 2; i++) {
            boolean transA = i == 1;
            for (int j = 0; j < 2; j++) {
                boolean transB = j == 1;
                for (int k = 0; k < 2; k++) {
                    boolean add = k == 1;
                    try {
                        if (add) {
                            if (transA) {
                                if (transB) {
                                    CommonOps_MT_FSCC.multAddTransAB(A_t, B_t, C);
                                    CommonOps_FDRM.multAddTransAB(denseA_t, B_t, expected);
                                } else {
                                    CommonOps_MT_FSCC.multAddTransA(A_t, B, C, growArray);
                                    CommonOps_FDRM.multAddTransA(denseA_t, B, expected);
                                }
                            } else if (transB) {
                                CommonOps_MT_FSCC.multAddTransB(A, B_t, C, growArray);
                                CommonOps_FDRM.multAddTransB(denseA, B_t, expected);
                            } else {
                                CommonOps_MT_FSCC.multAdd(A, B, C, growArray);
                                CommonOps_FDRM.multAdd(denseA, B, expected);
                            }
                        } else {
                            if (transA) {
                                if (transB) {
                                    CommonOps_MT_FSCC.multTransAB(A_t, B_t, C);
                                    CommonOps_FDRM.multTransAB(denseA_t, B_t, expected);
                                } else {
                                    CommonOps_MT_FSCC.multTransA(A_t, B, C, growArray);
                                    CommonOps_FDRM.multTransA(denseA_t, B, expected);
                                }
                            } else if (transB) {
                                CommonOps_MT_FSCC.multTransB(A, B_t, C, growArray);
                                CommonOps_FDRM.multTransB(denseA, B_t, expected);
                            } else {
                                CommonOps_MT_FSCC.mult(A, B, C, growArray);
                                CommonOps_FDRM.mult(denseA, B, expected);
                            }
                        }

                        if (exception)
                            fail("exception expected");

                        assertTrue(MatrixFeatures_FDRM.isIdentical(expected, C, UtilEjml.TEST_F32));
                    } catch (RuntimeException e) {
                        if (!exception) {
                            e.printStackTrace();
                            fail("no exception expected");
                        }
                    }
                }
            }
        }
    }
}