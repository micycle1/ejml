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

package org.ejml.dense.row.decomposition.eig;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.dense.row.decomposition.eig.watched.WatchedDoubleStepQREigen_FDRM;
import org.ejml.dense.row.decomposition.hessenberg.HessenbergSimilarDecomposition_FDRM;
import org.ejml.dense.row.decomposition.hessenberg.HessenbergSimilarDecomposition_MT_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Technically a class by this name doesn't exist. This is just the same class with concurrent algorithms being used
 * inside
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.eig.TestWatchedDoubleStepQRDecomposition_MT_DDRM")
public class TestWatchedDoubleStepQRDecomposition_MT_FDRM extends EjmlStandardJUnit {
    int size = 100;

    @Test void compareToSingle() {
        compareToSingle(false);
        compareToSingle(true);
    }

    void compareToSingle( boolean vectors ) {
        FMatrixRMaj A = RandomMatrices_FDRM.symmetric(size, -1, 1, rand);
        FMatrixRMaj B = A.copy();

        var single = new WatchedDoubleStepQRDecomposition_FDRM(new HessenbergSimilarDecomposition_FDRM(),
                new WatchedDoubleStepQREigen_FDRM(), vectors);
        var concurrent = new WatchedDoubleStepQRDecomposition_FDRM(new HessenbergSimilarDecomposition_MT_FDRM(),
                new WatchedDoubleStepQREigen_FDRM(), vectors);

        assertTrue(single.decompose(A));
        assertTrue(concurrent.decompose(B));

        assertTrue(MatrixFeatures_FDRM.isEquals(A, B, UtilEjml.TEST_F32));

        assertEquals(single.getNumberOfEigenvalues(), concurrent.getNumberOfEigenvalues());
        int numEigen = single.getNumberOfEigenvalues();
        for (int i = 0; i < numEigen; i++) {
            assertEquals(single.getEigenvalue(i).real, concurrent.getEigenvalue(i).real, UtilEjml.TEST_F32);
            assertEquals(single.getEigenvalue(i).imaginary, concurrent.getEigenvalue(i).imaginary, UtilEjml.TEST_F32);

            if (!vectors)
                continue;

            FMatrixRMaj singleVec = single.getEigenVector(i);
            FMatrixRMaj concurVec = concurrent.getEigenVector(i);
            assertTrue(MatrixFeatures_FDRM.isIdentical(singleVec, concurVec, UtilEjml.TEST_F32));
        }
    }
}
