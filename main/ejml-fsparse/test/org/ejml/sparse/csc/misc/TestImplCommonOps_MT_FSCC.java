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

package org.ejml.sparse.csc.misc;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.MatrixFeatures_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.ejml.sparse.csc.mult.Workspace_MT_FSCC;
import org.junit.jupiter.api.Test;
import pabeles.concurrency.GrowArray;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.misc.TestImplCommonOps_MT_DSCC")
class TestImplCommonOps_MT_FSCC extends EjmlStandardJUnit {
    private final GrowArray<Workspace_MT_FSCC> listWork = new GrowArray<>(Workspace_MT_FSCC::new);

    @Test
    void add() {
        float alpha = 1.5f;
        float beta = 2.3f;

        for (int numRows : new int[]{2, 4, 6, 10}) {
            for (int numCols : new int[]{2, 4, 6, 10, 17}) {
                FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(numRows, numCols, 7, -1, 1, rand);
                FMatrixSparseCSC b = RandomMatrices_FSCC.rectangle(numRows, numCols, 8, -1, 1, rand);
                FMatrixSparseCSC c = RandomMatrices_FSCC.rectangle(numRows, numCols, 3, -1, 1, rand);
                FMatrixSparseCSC cc = c.copy();

                ImplCommonOps_FSCC.add(alpha, a, beta, b, c, null, null);
                ImplCommonOps_MT_FSCC.add(alpha, a, beta, b, cc, listWork);

                assertTrue(CommonOps_FSCC.checkStructure(cc));
                assertTrue(MatrixFeatures_FSCC.isEqualsSort(c, cc, UtilEjml.TEST_F32));
            }
        }
    }
}