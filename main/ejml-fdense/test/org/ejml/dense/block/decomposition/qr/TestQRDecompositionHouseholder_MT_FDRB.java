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
import org.ejml.dense.block.MatrixOps_FDRB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.decomposition.qr.TestQRDecompositionHouseholder_MT_DDRB")
class TestQRDecompositionHouseholder_MT_FDRB extends EjmlStandardJUnit {
    int r = 3;

    @Test
    void compareToSingle() {
        var single = new QRDecompositionHouseholder_FDRB();
        var concurrent = new QRDecompositionHouseholder_MT_FDRB();

        for (int rows = 1; rows < 27; rows += 4) {
            int cols = rows/2 + 1;

            FMatrixRBlock A = MatrixOps_FDRB.createRandom(rows, cols, -1, 1, rand, r);
            FMatrixRBlock B = A.copy();

            assertTrue(single.decompose(A));
            assertTrue(concurrent.decompose(B));

            assertTrue(MatrixOps_FDRB.isEquals(A, B, UtilEjml.TEST_F32));

            for (boolean compact : new boolean[]{false, true}) {
                assertTrue(MatrixOps_FDRB.isEquals(single.getQ(null, compact),
                        concurrent.getQ(null, compact), UtilEjml.TEST_F32));
                assertTrue(MatrixOps_FDRB.isEquals(single.getR(null, compact),
                        concurrent.getR(null, compact), UtilEjml.TEST_F32));
            }
        }
    }
}

