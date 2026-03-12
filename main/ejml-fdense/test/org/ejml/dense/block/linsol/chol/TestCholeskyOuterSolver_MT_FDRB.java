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

package org.ejml.dense.block.linsol.chol;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRBlock;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.linsol.chol.TestCholeskyOuterSolver_MT_DDRB")
class TestCholeskyOuterSolver_MT_FDRB extends EjmlStandardJUnit {
	protected int r = 3;

	@Test
	void compareToSingle() {
		var single = new CholeskyOuterSolver_FDRB();
		var concurrent = new CholeskyOuterSolver_MT_FDRB();

		for (int i = 1; i <= r*3; i++) {
			for (int j = 1; j <= r*3; j++) {
				FMatrixRBlock A = MatrixOps_FDRB.convert(RandomMatrices_FDRM.symmetricPosDef(i, rand),r);
				FMatrixRBlock B = A.copy();
				FMatrixRBlock Y = A.create(i, j);
				FMatrixRBlock X_expected = A.create(i, j);
				FMatrixRBlock X_found = A.create(i, j);

				assertTrue(single.setA(A));
				assertTrue(concurrent.setA(B));
				assertTrue(MatrixOps_FDRB.isEquals(A, B, UtilEjml.TEST_F32));

				single.solve(Y, X_expected);
				concurrent.solve(Y, X_found);

				assertTrue(MatrixOps_FDRB.isEquals(X_expected, X_found, UtilEjml.TEST_F32));
			}
		}
	}
}

