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

package org.ejml.dense.block.decomposition.qr;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRBlock;
import org.ejml.data.FSubmatrixD1;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.decomposition.qr.TestBlockHouseHolder_MT_DDRB")
class TestBlockHouseHolder_MT_FDRB extends EjmlStandardJUnit {
    int r = 3;

    @Test
    void decomposeQR_block_col() {
        FMatrixRBlock A = MatrixOps_FDRB.createRandom(r*2 + r - 1, r, -1, 1, rand, r);
        FMatrixRBlock AA = A.copy();

        float[] gammas = new float[A.numCols];
        BlockHouseHolder_FDRB.decomposeQR_block_col(r, new FSubmatrixD1(A), gammas);

        float[] gammasC = new float[A.numCols];
        BlockHouseHolder_MT_FDRB.decomposeQR_block_col(r, new FSubmatrixD1(AA), gammasC);

        for (int i = 0; i < gammas.length; i++) {
            assertEquals(gammas[i], gammasC[i]);
        }

        assertTrue(MatrixOps_FDRB.isEquals(A, AA, UtilEjml.TEST_F32));
    }

    @Test
    void rank1UpdateMultR_Col() {
        float gamma = 2.5f;
        FMatrixRBlock A = MatrixOps_FDRB.createRandom(r*2 + r - 1, r*2 - 1, -1, 1, rand, r);
        FMatrixRBlock AA = A.copy();

        BlockHouseHolder_FDRB.rank1UpdateMultR_Col(r, new FSubmatrixD1(A), 1, gamma);
        BlockHouseHolder_MT_FDRB.rank1UpdateMultR_Col(r, new FSubmatrixD1(AA), 1, gamma);

        assertTrue(MatrixOps_FDRB.isEquals(A, AA, UtilEjml.TEST_F32));
    }
}

