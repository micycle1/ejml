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

package org.ejml.dense.block.decomposition.hessenberg;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRBlock;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.decomposition.hessenberg.TestTridiagonalDecompositionHouseholder_MT_DDRB")
class TestTridiagonalDecompositionHouseholder_MT_FDRB extends EjmlStandardJUnit {
    int r = 3;

    @Test
    void compareToSingle() {

        var single = new TridiagonalDecompositionHouseholder_FDRB();
        var concurrent = new TridiagonalDecompositionHouseholder_MT_FDRB();

        for (int width = 1; width <= r*10; width += 4) {
//        for (int width = 500; width <= 520; width += 4) {
            FMatrixRBlock A = MatrixOps_FDRB.convert(RandomMatrices_FDRM.symmetric(width, -1, 1, rand), r);
            FMatrixRBlock AA = A.copy();

            assertTrue(single.decompose(A));
            assertTrue(concurrent.decompose(AA));

            assertTrue(MatrixOps_FDRB.isEquals(A, AA, UtilEjml.TEST_F32));
            assertTrue(MatrixOps_FDRB.isEquals(single.getT(null), concurrent.getT(null), UtilEjml.TEST_F32));
            assertTrue(MatrixOps_FDRB.isEquals(
                    single.getQ(null,true), concurrent.getQ(null,true), UtilEjml.TEST_F32));
        }
    }
}

