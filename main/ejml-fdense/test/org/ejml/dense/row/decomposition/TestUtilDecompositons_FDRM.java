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

package org.ejml.dense.row.decomposition;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.TestUtilDecompositons_DDRM")
public class TestUtilDecompositons_FDRM extends EjmlStandardJUnit {
    @Test void checkIdentity_null() {
        FMatrixRMaj A = UtilDecompositons_FDRM.ensureIdentity(null,4,3);
        assertTrue(MatrixFeatures_FDRM.isIdentity(A, UtilEjml.TEST_F32));
        assertEquals(4,A.numRows);
        assertEquals(3,A.numCols);
    }

    @Test void checkIdentity_random() {
        FMatrixRMaj orig = RandomMatrices_FDRM.rectangle(2,5,rand);
        FMatrixRMaj A = UtilDecompositons_FDRM.ensureIdentity(orig,4,3);
        assertTrue(MatrixFeatures_FDRM.isIdentity(A, UtilEjml.TEST_F32));
        assertEquals(4,orig.numRows);
        assertEquals(3,orig.numCols);
        assertSame(A, orig);
    }

    @Test void checkZeros_null() {
        FMatrixRMaj A = UtilDecompositons_FDRM.ensureZeros(null,4,3);
        assertTrue(MatrixFeatures_FDRM.isZeros(A, UtilEjml.TEST_F32));
        RandomMatrices_FDRM.fillUniform(A,rand);
        assertEquals(4,A.numRows);
        assertEquals(3,A.numCols);
    }

    @Test void checkZeros_random() {
        FMatrixRMaj orig = RandomMatrices_FDRM.rectangle(2,5,rand);
        FMatrixRMaj A = UtilDecompositons_FDRM.ensureZeros(orig,4,3);
        assertTrue(MatrixFeatures_FDRM.isZeros(A, UtilEjml.TEST_F32));
        assertSame(A, orig);
        assertEquals(4,A.numRows);
        assertEquals(3,A.numCols);
    }

    @Test void checkZerosLT_null() {
        FMatrixRMaj A = UtilDecompositons_FDRM.checkZerosLT(null,4,3);
        assertTrue(MatrixFeatures_FDRM.isUpperTriangle(A,0, UtilEjml.TEST_F32));
    }

    @Test void checkZerosLT_random() {
        FMatrixRMaj orig = RandomMatrices_FDRM.rectangle(4,3,rand);
        FMatrixRMaj A = UtilDecompositons_FDRM.checkZerosLT(orig,4,3);
        assertTrue(MatrixFeatures_FDRM.isUpperTriangle(A, 0, UtilEjml.TEST_F32));
        assertSame(A, orig);
    }

    @Test void checkZerosUT_null() {
        FMatrixRMaj A = UtilDecompositons_FDRM.checkZerosUT(null,4,3);
        assertTrue(MatrixFeatures_FDRM.isLowerTriangle(A,0, UtilEjml.TEST_F32));
    }

    @Test void checkZerosUT_random() {
        FMatrixRMaj orig = RandomMatrices_FDRM.rectangle(4,3,rand);
        FMatrixRMaj A = UtilDecompositons_FDRM.checkZerosUT(orig,4,3);
        assertTrue(MatrixFeatures_FDRM.isLowerTriangle(A, 0, UtilEjml.TEST_F32));
        assertSame(A, orig);
    }
}