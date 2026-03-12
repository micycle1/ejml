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

package org.ejml.sparse.csc.mult;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.dense.row.mult.MatrixVectorMult_FDRM;
import org.ejml.dense.row.mult.VectorVectorMult_FDRM;
import org.ejml.ops.FConvertMatrixStruct;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Generated("org.ejml.sparse.csc.mult.TestMatrixVectorMult_DSCC")
public class TestMatrixVectorMult_FSCC extends EjmlStandardJUnit {
    @Test void mult_A_v() {
        FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(6,4,14,rand);

        int offset = 2;
        float[] v = new float[]{0,1,2,3,4,5,6,7};
        float[] found = new float[7];

        FMatrixRMaj Ad = FConvertMatrixStruct.convert(A,(FMatrixRMaj)null);
        FMatrixRMaj vd = new FMatrixRMaj(4,1);
        System.arraycopy(v,2,vd.data,0,4);

        FMatrixRMaj expected = new FMatrixRMaj(6,1);

        MatrixVectorMult_FSCC.mult(A,v,offset,found,1);
        MatrixVectorMult_FDRM.mult(Ad,vd,expected);

        for (int i = 0; i < A.numRows; i++) {
            assertEquals(expected.data[i],found[i+1], UtilEjml.TEST_F32);
        }
    }

    @Test void multAdd_A_v() {
        FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(6,4,14,rand);

        int offset = 2;
        float[] v = new float[]{0,1,2,3,4,5,6,7};
        float[] found = new float[7];
        found[2] = 3;


        FMatrixRMaj Ad = FConvertMatrixStruct.convert(A,(FMatrixRMaj)null);
        FMatrixRMaj vd = new FMatrixRMaj(4,1);
        System.arraycopy(v,2,vd.data,0,4);

        FMatrixRMaj expected = new FMatrixRMaj(6,1);
        expected.data[1] = 3;

        MatrixVectorMult_FSCC.multAdd(A,v,offset,found,1);
        MatrixVectorMult_FDRM.multAdd(Ad,vd,expected);

        for (int i = 0; i < A.numRows; i++) {
            assertEquals(expected.data[i],found[i+1], UtilEjml.TEST_F32);
        }
    }

    @Test void mult_v_A() {
        FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(6,4,14,rand);

        int offset = 1;
        float[] v = new float[]{0,1,2,3,4,5,6,7};
        float[] found = new float[5];

        FMatrixRMaj Ad = FConvertMatrixStruct.convert(A,(FMatrixRMaj)null);
        FMatrixRMaj vd = new FMatrixRMaj(6,1);
        System.arraycopy(v,offset,vd.data,0,A.numRows);

        FMatrixRMaj expected = new FMatrixRMaj(6,1);

        MatrixVectorMult_FSCC.mult(v,offset,A,found,1);
        CommonOps_FDRM.multTransA(vd,Ad,expected);

        for (int i = 0; i < A.numCols; i++) {
            assertEquals(expected.data[i],found[i+1], UtilEjml.TEST_F32);
        }
    }

    @Test void innerProduct_DSD_array() {
        FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(6,4,14,rand);

        int offsetV = 1;
        float[] v = new float[]{0,1,2,3,4,5,6,7};
        int offsetW = 2;
        float[] w = new float[]{2,0,1,9,3,1,6,7};

        float found = MatrixVectorMult_FSCC.innerProduct(v,offsetV,A,w,offsetW);

        FMatrixRMaj Ad = FConvertMatrixStruct.convert(A,(FMatrixRMaj)null);
        FMatrixRMaj vd = new FMatrixRMaj(A.numRows,1);
        FMatrixRMaj wd = new FMatrixRMaj(A.numCols,1);
        System.arraycopy(v,offsetV,vd.data,0,A.numRows);
        System.arraycopy(w,offsetW,wd.data,0,A.numCols);

        float expected = VectorVectorMult_FDRM.innerProdA(vd,Ad,wd);

        assertEquals(expected,found, UtilEjml.TEST_F32);
    }

    @Test void innerProduct_SDS() {
        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(10, 1, 6, rand);
        FMatrixRMaj b = RandomMatrices_FDRM.rectangle(10, 10, rand);
        FMatrixSparseCSC c = RandomMatrices_FSCC.rectangle(10, 1, 6, rand);
        FMatrixRMaj c2 = new FMatrixRMaj(c);

        FMatrixRMaj tmp = new FMatrixRMaj(10, 1);
        CommonOps_FSCC.multTransA(a, b, tmp, null);
        float expected = CommonOps_FDRM.dot(tmp, c2);
        float found = MatrixVectorMult_FSCC.innerProduct(a, b, c);
        assertEquals(expected, found, UtilEjml.TEST_F32);
    }


    @Test void innerProduct_symmetric_SD() {
        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(10, 1, 6, rand);
        FMatrixRMaj a2 = new FMatrixRMaj(a);
        FMatrixRMaj b = RandomMatrices_FDRM.symmetricPosDef(10, rand);

        FMatrixRMaj tmp = new FMatrixRMaj(10, 1);
        CommonOps_FSCC.multTransA(a, b, tmp, null);
        float expected = CommonOps_FDRM.dot(tmp, a2);
        float found = MatrixVectorMult_FSCC.innerProductSelfSymmetrical(a, b);
        assertEquals(expected, found, UtilEjml.TEST_F32);
    }
}