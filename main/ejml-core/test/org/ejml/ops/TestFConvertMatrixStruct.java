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

package org.ejml.ops;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.EjmlUnitTests;
import org.ejml.UtilEjml;
import org.ejml.data.*;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.ejml.dense.row.MatrixFeatures_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.MatrixFeatures_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.ejml.sparse.triplet.MatrixFeatures_FSTL;
import org.ejml.sparse.triplet.RandomMatrices_FSTL;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
@SuppressWarnings({"ClassNewInstance", "rawtypes"})
@Generated("org.ejml.ops.TestDConvertMatrixStruct")
public class TestFConvertMatrixStruct extends EjmlStandardJUnit {
    @Test
    public void any_to_any() {
        FMatrixRMaj a = new FMatrixRMaj(2,3,true,1,2,3,4,5,6);
        FMatrixRMaj b = new FMatrixRMaj(2,3);

        FConvertMatrixStruct.convert((FMatrix)a,(FMatrix)b);

        assertTrue(MatrixFeatures_FDRM.isIdentical(a,b,UtilEjml.TEST_F32));
    }

    @Test
    public void checkAll_Fixed_to_DM() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Method[] methods = FConvertMatrixStruct.class.getMethods();

        int numFound = 0;

        for( Method m : methods ) {
            if( !m.getName().equals("convert") )
                continue;
            Class[]param = m.getParameterTypes();

            if( !FMatrixFixed.class.isAssignableFrom(param[0]) ) {
                continue;
            }

            FMatrixFixed a = (FMatrixFixed)param[0].newInstance();
            FMatrixRMaj b = new FMatrixRMaj(a.getNumRows(),a.getNumCols());

            for( int i = 0; i < b.numRows; i++ ) {
                for( int j = 0; j < b.numCols; j++ ) {
                    a.set(i, j, rand.nextFloat());
                }
            }

            Object[] input = new Object[param.length];
            input[0] = a;
            input[1] = b;

            m.invoke(null,input);

            checkIdentical(a,b);

            numFound++;
        }

        assertEquals(5+5,numFound);
    }

    @Test
    public void checkAll_DM_to_Fixed() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Method[] methods = FConvertMatrixStruct.class.getMethods();

        int numFound = 0;

        for( Method m : methods ) {
            if( !m.getName().equals("convert") )
                continue;
            Class[] param = m.getParameterTypes();

            if( !FMatrixFixed.class.isAssignableFrom(param[1]) ) {
                continue;
            }

            FMatrixFixed b = (FMatrixFixed)param[1].newInstance();
            FMatrixRMaj a = new FMatrixRMaj(b.getNumRows(),b.getNumCols());

            for( int i = 0; i < a.numRows; i++ ) {
                for( int j = 0; j < a.numCols; j++ ) {
                    a.set(i, j, rand.nextFloat());
                }
            }

            Object[] input = new Object[param.length];
            input[0] = a;
            input[1] = b;

            m.invoke(null,input);

            checkIdentical(a,b);

            numFound++;
        }

        assertEquals(5+5,numFound);
    }

    @Test
    public void BM_to_DM() {
        for( int rows = 1; rows <= 8; rows++ ) {
            for( int cols = 1; cols <= 8; cols++ ) {
                FMatrixRBlock a = MatrixOps_FDRB.createRandom(rows,cols,-1,2,rand);
                FMatrixRMaj b = new FMatrixRMaj(rows,cols);

                FConvertMatrixStruct.convert(a,b);

                checkIdentical(a,b);
            }
        }
    }

    @Test
    public void DM_to_BM() {
        for( int rows = 1; rows <= 8; rows++ ) {
            for( int cols = 1; cols <= 8; cols++ ) {
                FMatrixRMaj a = RandomMatrices_FDRM.rectangle(rows,cols,rand);
                FMatrixRBlock b = new FMatrixRBlock(rows,cols,3);

                FConvertMatrixStruct.convert(a,b);

                checkIdentical(a,b);
            }
        }
    }


    private void checkIdentical(FMatrix a , FMatrix b ) {
        for( int i = 0; i < a.getNumRows(); i++  ) {
            for( int j = 0; j < a.getNumCols(); j++ ) {
                assertEquals(a.get(i,j),b.get(i,j), UtilEjml.TEST_F32);
            }
        }
    }

    @Test
    public void FMatrixRow_SMatrixTriplet() {
        FMatrixRMaj a = RandomMatrices_FDRM.rectangle(5,6,-1,1,rand);

        a.set(4,3, 0);
        a.set(1,3, 0);
        a.set(2,3, 0);
        a.set(2,0, 0);

        FMatrixRow_SMatrixTriplet(a,null);
        FMatrixRow_SMatrixTriplet(a, new FMatrixSparseTriplet(1,1,2));
    }

    public void FMatrixRow_SMatrixTriplet(FMatrixRMaj a , @Nullable FMatrixSparseTriplet b ) {
        b = FConvertMatrixStruct.convert(a,b, UtilEjml.F_EPS);

        assertEquals(a.numRows, b.numRows);
        assertEquals(a.numCols, b.numCols);
        assertEquals(5*6-4, b.nz_length);
        for (int row = 0; row < a.numRows; row++) {
            for (int col = 0; col < a.numCols; col++) {
                int index = b.nz_index(row,col);

                if( a.get(row,col) == 0.0f ) {
                    assertEquals(index, -1);
                } else {
                    assertEquals( a.get(row,col), b.nz_value.data[index], UtilEjml.TEST_F32);
                }
            }
        }

        // now try it the other direction
        FMatrixRMaj c = FConvertMatrixStruct.convert(b,(FMatrixRMaj)null);
        assertTrue(MatrixFeatures_FDRM.isEquals(a,c, UtilEjml.TEST_F32));

        c = FConvertMatrixStruct.convert(b,new FMatrixRMaj(1,1));
        assertTrue(MatrixFeatures_FDRM.isEquals(a,c, UtilEjml.TEST_F32));
    }

    @Test
    public void FMatrix_SMatrixTriplet() {
        FMatrixRMaj a = RandomMatrices_FDRM.rectangle(5,6,-1,1,rand);

        a.set(4,3, 0);
        a.set(1,3, 0);
        a.set(2,3, 0);
        a.set(2,0, 0);

        FMatrix_SMatrixTriplet(a,null);
        FMatrix_SMatrixTriplet(a, new FMatrixSparseTriplet(1,1,2));
    }

    public void FMatrix_SMatrixTriplet(FMatrix a , @Nullable FMatrixSparseTriplet b ) {
        b = FConvertMatrixStruct.convert(a,b, UtilEjml.F_EPS);

        assertEquals(a.getNumRows(), b.numRows);
        assertEquals(a.getNumCols(), b.numCols);
        assertEquals(5*6-4, b.nz_length);
        for (int row = 0; row < a.getNumRows(); row++) {
            for (int col = 0; col < a.getNumCols(); col++) {
                int index = b.nz_index(row,col);

                if( a.get(row,col) == 0.0f ) {
                    assertEquals(index, -1);
                } else {
                    assertEquals( a.get(row,col), b.nz_value.data[index], UtilEjml.TEST_F32);
                }
            }
        }

        // now try it the other direction
        FMatrixRMaj c = FConvertMatrixStruct.convert(b,(FMatrixRMaj)null);
        EjmlUnitTests.assertEquals(a,c, UtilEjml.TEST_F32);

        c = FConvertMatrixStruct.convert(b,new FMatrixRMaj(1,1));
        EjmlUnitTests.assertEquals(a,c, UtilEjml.TEST_F32);
    }

    @Test
    public void FMatrixRow_SparseCSC() {
        FMatrixRMaj a = RandomMatrices_FDRM.rectangle(5,6,-1,1,rand);

        a.set(4,3, 0);
        a.set(1,3, 0);
        a.set(2,3, 0);
        a.set(2,0, 0);

        FMatrixRow_SparseCSC(a,null);
        FMatrixRow_SparseCSC(a, new FMatrixSparseCSC(1,1,2));
    }

    public void FMatrixRow_SparseCSC(FMatrixRMaj a , @Nullable FMatrixSparseCSC b ) {
        b = FConvertMatrixStruct.convert(a,b, UtilEjml.F_EPS);

        assertEquals(a.numRows, b.numRows);
        assertEquals(a.numCols, b.numCols);
        assertEquals(5*6-4, b.nz_length);
        for (int row = 0; row < a.numRows; row++) {
            for (int col = 0; col < a.numCols; col++) {
                int index = b.nz_index(row,col);

                if( a.get(row,col) == 0.0f ) {
                    assertEquals(index, -1);
                } else {
                    assertEquals( a.get(row,col), b.nz_values[index], UtilEjml.TEST_F32);
                }
            }
        }

        // now try it the other direction
        FMatrixRMaj c = FConvertMatrixStruct.convert(b,(FMatrixRMaj)null);
        assertTrue(MatrixFeatures_FDRM.isEquals(a,c, UtilEjml.TEST_F32));

        c = FConvertMatrixStruct.convert(b,new FMatrixRMaj(1,1));
        assertTrue(MatrixFeatures_FDRM.isEquals(a,c, UtilEjml.TEST_F32));
    }

    @Test
    public void SMatrixCC_FMatrixRow() {
        FMatrixSparseCSC a = RandomMatrices_FSCC.rectangle(5,6,10,-1,1,rand);

        SMatrixCC_FMatrixRow(a,null);
        SMatrixCC_FMatrixRow(a,new FMatrixRMaj(1,1));
    }

    public void SMatrixCC_FMatrixRow(FMatrixSparseCSC a , @Nullable FMatrixRMaj b ) {
        b = FConvertMatrixStruct.convert(a,b);

        assertEquals(a.numRows, b.numRows);
        assertEquals(a.numCols, b.numCols);

        int found = MatrixFeatures_FDRM.countNonZero(b);
        assertEquals(a.nz_length, found);
        EjmlUnitTests.assertEquals(a, b);

        // now try it the other direction
        FMatrixSparseCSC c = FConvertMatrixStruct.convert(b,(FMatrixSparseCSC)null, UtilEjml.F_EPS);
        assertTrue(MatrixFeatures_FSCC.isEqualsSort(a,c, UtilEjml.TEST_F32));
        assertTrue(CommonOps_FSCC.checkIndicesSorted(c));

        c = FConvertMatrixStruct.convert(b,new FMatrixSparseCSC(1,1,1), UtilEjml.F_EPS);
        assertTrue(MatrixFeatures_FSCC.isEqualsSort(a,c, UtilEjml.TEST_F32));
        assertTrue(CommonOps_FSCC.checkIndicesSorted(c));
    }

    @Test
    public void SMatrixTriplet_SMatrixCC() {
        FMatrixSparseTriplet a = RandomMatrices_FSTL.uniform(5,6,10,-1,1,rand);

        SMatrixTriplet_SMatrixCC(a,(FMatrixSparseCSC)null);
        SMatrixTriplet_SMatrixCC(a,new FMatrixSparseCSC(1,1,2));
    }

    public void SMatrixTriplet_SMatrixCC(FMatrixSparseTriplet a , @Nullable FMatrixSparseCSC b ) {
        b = FConvertMatrixStruct.convert(a,b);

        assertEquals(a.numRows, b.numRows);
        assertEquals(a.numCols, b.numCols);
        assertEquals(a.nz_length, b.nz_length);
        for (int i = 0; i < a.nz_length; i++) {
            int row = a.nz_rowcol.data[i*2];
            int col = a.nz_rowcol.data[i*2+1];
            float value = a.nz_value.data[i];

            assertEquals(value, b.get(row, col), UtilEjml.TEST_F32);
        }
        assertTrue(CommonOps_FSCC.checkSortedFlag(b));

        // now try it the other direction
        FMatrixSparseTriplet c = FConvertMatrixStruct.convert(b,(FMatrixSparseTriplet)null);
        assertTrue(MatrixFeatures_FSTL.isEquals(a,c, UtilEjml.TEST_F32));

        c = FConvertMatrixStruct.convert(b,new FMatrixSparseTriplet(1,1,1));
        assertTrue(MatrixFeatures_FSTL.isEquals(a,c, UtilEjml.TEST_F32));
    }

}
