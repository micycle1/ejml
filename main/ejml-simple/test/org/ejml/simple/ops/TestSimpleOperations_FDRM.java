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

package org.ejml.simple.ops;

import javax.annotation.Generated;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.simple.SimpleOperations;

@Generated("org.ejml.simple.ops.TestSimpleOperations_DDRM")
class TestSimpleOperations_FDRM extends BaseSimpleOperationsChecks<FMatrixRMaj> {
    @Override public SimpleOperations<FMatrixRMaj> createOps() {
        return new SimpleOperations_FDRM();
    }

    @Override public FMatrixRMaj randomRect( int numRows, int numCols ) {
        return RandomMatrices_FDRM.rectangle(numRows, numCols, rand);
    }
}