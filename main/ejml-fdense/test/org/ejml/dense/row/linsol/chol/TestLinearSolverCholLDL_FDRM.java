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

package org.ejml.dense.row.linsol.chol;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.EjmlUnitTests;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.row.linsol.chol.TestLinearSolverCholLDL_DDRM")
public class TestLinearSolverCholLDL_FDRM extends EjmlStandardJUnit {
    @Test void testInverseAndSolve() {
        FMatrixRMaj A = new FMatrixRMaj(3, 3, true, 1, 2, 4, 2, 13, 23, 4, 23, 90);
        FMatrixRMaj b = new FMatrixRMaj(3, 1, true, 17, 97, 320);
        FMatrixRMaj x = RandomMatrices_FDRM.rectangle(3, 1, rand);

        LinearSolverCholLDL_FDRM solver = new LinearSolverCholLDL_FDRM();
        assertTrue(solver.setA(A));
        solver.invert(A);
        solver.solve(b, x);


        FMatrixRMaj A_inv = new FMatrixRMaj(3, 3, true, 1.453515f, -0.199546f, -0.013605f, -0.199546f, 0.167800f, -0.034014f, -0.013605f, -0.034014f, 0.020408f);
        FMatrixRMaj x_expected = new FMatrixRMaj(3, 1, true, 1, 2, 3);

        EjmlUnitTests.assertEquals(A_inv, A, UtilEjml.TEST_F32_SQ);
        EjmlUnitTests.assertEquals(x_expected, x, UtilEjml.TEST_F32_SQ);
    }
}
