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

package org.ejml.dense.row.decomposition.qr;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.row.decomposition.qr.TestQrHelperFunctions_MT_DDRM")
class TestQrHelperFunctions_MT_FDRM extends EjmlStandardJUnit {
    final int N = 200;
    FMatrixRMaj Q;
    public float[] u = new float[N];
    public float[] temp = new float[N];

    @BeforeEach
    void init() {
        Q = RandomMatrices_FDRM.rectangle(N, N, -1, 1, rand);
        for (int i = 0; i < N; i++) {
            u[i] = (float)rand.nextGaussian();
        }
    }

    @Test void rank1UpdateMultR_u0() {
        FMatrixRMaj expected = Q.copy();

        QrHelperFunctions_MT_FDRM.rank1UpdateMultR_u0(Q, u, 0.9f, 1.2f, 1, 0, N, temp);
        QrHelperFunctions_FDRM.rank1UpdateMultR_u0(expected, u, 0.9f, 1.2f, 1, 0, N, temp);

        assertTrue(MatrixFeatures_FDRM.isEquals(expected, Q, UtilEjml.TEST_F32));
    }

    @Test void rank1UpdateMultR() {
        FMatrixRMaj expected = Q.copy();

        QrHelperFunctions_MT_FDRM.rank1UpdateMultR(Q, u, 1.2f, 1, 0, N, temp);
        QrHelperFunctions_FDRM.rank1UpdateMultR(expected, u, 1.2f, 1, 0, N, temp);

        assertTrue(MatrixFeatures_FDRM.isEquals(expected, Q, UtilEjml.TEST_F32));
    }

    @Test void rank1UpdateMultR_offU() {
        FMatrixRMaj expected = Q.copy();

        QrHelperFunctions_MT_FDRM.rank1UpdateMultR(Q, u, 0, 1.2f, 0, 1, N, temp);
        QrHelperFunctions_FDRM.rank1UpdateMultR(expected, u, 0, 1.2f, 0, 1, N, temp);

        assertTrue(MatrixFeatures_FDRM.isEquals(expected, Q, UtilEjml.TEST_F32));
    }

    @Test void rank1UpdateMultL() {
        FMatrixRMaj expected = Q.copy();

        QrHelperFunctions_MT_FDRM.rank1UpdateMultL(Q, u, 1.2f, 1, 0, N);
        QrHelperFunctions_FDRM.rank1UpdateMultL(expected, u, 1.2f, 1, 0, N);

        assertTrue(MatrixFeatures_FDRM.isEquals(expected, Q, UtilEjml.TEST_F32));
    }
}

