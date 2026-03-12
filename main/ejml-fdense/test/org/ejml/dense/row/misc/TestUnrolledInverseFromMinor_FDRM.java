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

package org.ejml.dense.row.misc;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.EjmlUnitTests;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.dense.row.decomposition.lu.LUDecompositionAlt_FDRM;
import org.ejml.dense.row.linsol.lu.LinearSolverLu_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.misc.TestUnrolledInverseFromMinor_DDRM")
public class TestUnrolledInverseFromMinor_FDRM extends EjmlStandardJUnit {
    /**
     * Compare it against LU decomposition
     */
    @Test void compareToLU() {

        for(int N = 2; N <= UnrolledInverseFromMinor_FDRM.MAX; N++ ) {
            FMatrixRMaj A = RandomMatrices_FDRM.rectangle(N,N,rand);

            FMatrixRMaj expected = new FMatrixRMaj(N,N);
            FMatrixRMaj found = new FMatrixRMaj(N,N);

            // first compute inverse by LU
            LUDecompositionAlt_FDRM alg = new LUDecompositionAlt_FDRM();
            LinearSolverLu_FDRM solver = new LinearSolverLu_FDRM(alg);

            assertTrue( solver.setA(A));
            solver.invert(expected);

            // compute the result from the algorithm being tested
            UnrolledInverseFromMinor_FDRM.inv(A,found);

            EjmlUnitTests.assertEquals(expected,found, UtilEjml.TEST_F32);
        }

    }
}
