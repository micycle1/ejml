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

package org.ejml.dense.row.decomposition.hessenberg;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.hessenberg.TestTridiagonalDecompositionHouseholder_MT_DDRM")
class TestTridiagonalDecompositionHouseholder_MT_FDRM extends EjmlStandardJUnit {
    int size = 100;

    @Test void compareToSingle() {
        FMatrixRMaj A = RandomMatrices_FDRM.symmetric(size,-1,1,rand);
        FMatrixRMaj B = A.copy();

        var algSingle = new TridiagonalDecompositionHouseholder_FDRM();
        var algMT = new TridiagonalDecompositionHouseholder_MT_FDRM();

        assertTrue(algSingle.decompose(A));
        assertTrue(algMT.decompose(B));

        assertTrue(MatrixFeatures_FDRM.isEquals(A,B, UtilEjml.TEST_F32));

        assertTrue(MatrixFeatures_FDRM.isEquals(algSingle.getT(null), algMT.getT(null), UtilEjml.TEST_F32));
        assertTrue(MatrixFeatures_FDRM.isEquals(algSingle.getQ(null,true), algMT.getQ(null,true), UtilEjml.TEST_F32));
        assertTrue(MatrixFeatures_FDRM.isEquals(algSingle.getQ(null,false), algMT.getQ(null,false), UtilEjml.TEST_F32));
    }
}