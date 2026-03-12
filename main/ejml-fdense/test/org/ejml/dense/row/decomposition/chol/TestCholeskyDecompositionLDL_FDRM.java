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
import org.ejml.EjmlUnitTests;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.junit.jupiter.api.Test;

import static org.ejml.dense.row.decomposition.CheckDecompositionInterface_FDRM.checkModifiedInput;
import static org.junit.jupiter.api.Assertions.*;

@Generated("org.ejml.dense.row.decomposition.chol.TestCholeskyDecompositionLDL_DDRM")
public class TestCholeskyDecompositionLDL_FDRM extends EjmlStandardJUnit {
    @Test void checkModifyInput() {
        checkModifiedInput(new CholeskyDecompositionLDL_FDRM());
    }

    @Test void testDecompose() {
        var A = new FMatrixRMaj(3, 3, true, 1, 2, 4, 2, 7, 23, 4, 23, 98);
        var L = new FMatrixRMaj(3, 3, true, 1, 0, 0, 2, 1, 0, 4, 5, 1);

        float[] D = new float[]{1, 3, 7};

        var cholesky = new CholeskyDecompositionLDL_FDRM();
        assertTrue(cholesky.decompose(A));

        FMatrixRMaj foundL = cholesky.getL();

        EjmlUnitTests.assertEquals(L, foundL, UtilEjml.TEST_F32);
        for (int i = 0; i < D.length; i++) {
            assertEquals(D[i], cholesky.getDiagonal()[i], UtilEjml.TEST_F32);
        }
    }

    /**
     * If it is not positive definite it should fail
     */
    @Test void testNotPositiveDefinate() {
        var A = new FMatrixRMaj(2, 2, true, 1, -1, -1, -2);

        var alg = new CholeskyDecompositionLDL_FDRM();
        assertFalse(alg.decompose(A));
    }
}