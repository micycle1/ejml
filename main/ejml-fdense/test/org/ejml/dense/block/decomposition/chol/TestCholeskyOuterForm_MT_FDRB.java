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

package org.ejml.dense.block.decomposition.chol;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRBlock;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.dense.row.factory.DecompositionFactory_FDRM;
import org.ejml.generic.GenericMatrixOps_F32;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.decomposition.chol.TestCholeskyOuterForm_MT_DDRB")
class TestCholeskyOuterForm_MT_FDRB extends EjmlStandardJUnit {
	// size of a block
	int bl = 5;

	/**
	 * Test upper cholesky decomposition for upper triangular.
	 */
	@Test
	void compareToSingle() {
		compareToSingle(true);
		compareToSingle(false);
	}

	void compareToSingle(boolean lower) {
		// test against various different sizes
		for( int N = bl-2; N <= 41; N += 6 ) {
			FMatrixRBlock A = MatrixOps_FDRB.convert(RandomMatrices_FDRM.symmetricPosDef(N, rand),bl);
			FMatrixRBlock B = A.copy();

			var single = new CholeskyOuterForm_FDRB(lower);
			var concurrent = new CholeskyOuterForm_MT_FDRB(lower);

			assertTrue(DecompositionFactory_FDRM.decomposeSafe(single,A));
			assertTrue(DecompositionFactory_FDRM.decomposeSafe(concurrent,B));

			assertTrue(GenericMatrixOps_F32.isEquivalent(single.getT(null),concurrent.getT(null), UtilEjml.TEST_F32));

			float expectedDet = single.computeDeterminant().real;
			float foundDet = concurrent.computeDeterminant().real;

			assertEquals(expectedDet,foundDet,UtilEjml.TEST_F32);
		}
	}
}

