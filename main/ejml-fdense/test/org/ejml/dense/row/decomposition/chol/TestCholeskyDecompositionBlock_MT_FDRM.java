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

package org.ejml.dense.row.decomposition.chol;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.row.decomposition.chol.TestCholeskyDecompositionBlock_MT_DDRM")
class TestCholeskyDecompositionBlock_MT_FDRM extends EjmlStandardJUnit {
    @Test void compare() {
        int blockLength = 13;
        FMatrixRMaj A = RandomMatrices_FDRM.symmetricPosDef(150, new Random(234));
        FMatrixRMaj B = A.copy();

        var single = new CholeskyDecompositionBlock_FDRM(blockLength);
        var concurrent = new CholeskyDecompositionBlock_FDRM(blockLength);

        assertTrue(single.decompose(A));
        assertTrue(concurrent.decompose(B));

        assertTrue(MatrixFeatures_FDRM.isIdentical(A, B, UtilEjml.TEST_F32));

        assertTrue(MatrixFeatures_FDRM.isIdentical(single.getT(), concurrent.getT(), UtilEjml.TEST_F32));
    }
}