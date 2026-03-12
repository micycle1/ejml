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
import org.ejml.EjmlUnitTests;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.row.decomposition.qr.TestQRDecompositionHouseholderColumn_MT_DDRM")
public class TestQRDecompositionHouseholderColumn_MT_FDRM extends EjmlStandardJUnit {
    @Test void compare() {
        FMatrixRMaj A = new FMatrixRMaj(200, 50);
        FMatrixRMaj expQ = new FMatrixRMaj(1, 1);
        FMatrixRMaj expR = new FMatrixRMaj(1, 1);
        FMatrixRMaj fndQ = new FMatrixRMaj(1, 1);
        FMatrixRMaj fndR = new FMatrixRMaj(1, 1);

        var single = new QRDecompositionHouseholderColumn_FDRM();
        var thread = new QRDecompositionHouseholderColumn_MT_FDRM();

        assertFalse(single.inputModified());
        assertFalse(thread.inputModified());

        for (int i = 0; i < 5; i++) {
            RandomMatrices_FDRM.fillUniform(A, -1, 1, rand);
            assertTrue(single.decompose(A));
            assertTrue(thread.decompose(A));

            single.getQ(expQ, true);
            single.getR(expR, true);

            thread.getQ(fndQ, true);
            thread.getR(fndR, true);

            EjmlUnitTests.assertEquals(expQ, fndQ);
            EjmlUnitTests.assertEquals(fndR, fndR);
        }
    }
}
