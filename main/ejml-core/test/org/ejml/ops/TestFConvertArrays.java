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
import org.ejml.UtilEjml;
import org.ejml.data.FMatrix4;
import org.ejml.data.FMatrixRMaj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Generated("org.ejml.ops.TestDConvertArrays")
public class TestFConvertArrays extends EjmlStandardJUnit {
    @Test
    public void dd_to_ddrm() {
        FMatrixRMaj m = FConvertArrays.convert(new float[][]{{0, 1}, {2, 3}}, (FMatrixRMaj)null);

        assertEquals(0, m.get(0, 0), UtilEjml.TEST_F32);
        assertEquals(1, m.get(0, 1), UtilEjml.TEST_F32);
        assertEquals(2, m.get(1, 0), UtilEjml.TEST_F32);
        assertEquals(3, m.get(1, 1), UtilEjml.TEST_F32);
    }

    @Test
    public void ddrm_to_dd() {
        float[][] expected = {{0, 1}, {2, 3}};
        float[][] dd = FConvertArrays.convert(new FMatrixRMaj(expected));

        assertArrayEquals(expected, dd);
        assertEquals(0, dd[0][0], UtilEjml.TEST_F32);
        assertEquals(1, dd[0][1], UtilEjml.TEST_F32);
        assertEquals(2, dd[1][0], UtilEjml.TEST_F32);
        assertEquals(3, dd[1][1], UtilEjml.TEST_F32);
    }

//    @Test
//    public void dd_to_dscc() {
//        FMatrixSparseCSC m = ConvertDArrays.convert(new float[][]{{0,1},{2,3}},(FMatrixSparseCSC) null);
//
//        assertEquals(3,m.nz_length);
//        assertTrue(CommonOps_FSCC.checkStructure(m));
//
//        assertEquals(0,m.get(0,0), UtilEjml.TEST_F32);
//        assertEquals(1,m.get(0,1), UtilEjml.TEST_F32);
//        assertEquals(2,m.get(1,0), UtilEjml.TEST_F32);
//        assertEquals(3,m.get(1,1), UtilEjml.TEST_F32);
//    }

    @Test
    public void dd_to_d4() {
        FMatrix4 m = FConvertArrays.convert(new float[][]{{0, 1, 2, 3}}, (FMatrix4)null);

        assertEquals(0, m.a1, UtilEjml.TEST_F32);
        assertEquals(1, m.a2, UtilEjml.TEST_F32);
        assertEquals(2, m.a3, UtilEjml.TEST_F32);
        assertEquals(3, m.a4, UtilEjml.TEST_F32);
    }
}
