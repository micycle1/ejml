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

package org.ejml.dense.block;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRBlock;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.CommonOps_FDRM;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.generic.GenericMatrixOps_F32;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.TestMatrixOps_MT_DDRB")
class TestMatrixOps_MT_FDRB extends EjmlStandardJUnit {
    final static int BLOCK_LENGTH = 10;

    /**
     * Tests for correctness multiplication of an entire matrix for all multiplication operations.
     */
    @Test
    void multiplication() {
        Method[] methods = MatrixOps_MT_FDRB.class.getDeclaredMethods();

        int numFound = 0;
        for (Method m : methods) {
            String name = m.getName();

            if (!name.contains("mult"))
                continue;

//            System.out.println("name = "+name);

            boolean transA = false;
            boolean transB = false;

            if (name.contains("TransA"))
                transA = true;

            if (name.contains("TransB"))
                transB = true;

            checkMult(m, transA, transB);
            numFound++;
        }

        // make sure all the functions were in fact tested
        assertEquals(3, numFound);
    }

    /**
     * Test the method against various matrices of different sizes and shapes which have partial
     * blocks.
     */
    private void checkMult( Method func, boolean transA, boolean transB ) {
        // trivial case
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH, BLOCK_LENGTH);

        // stuff larger than the block size
        checkMult(func, transA, transB, BLOCK_LENGTH + 1, BLOCK_LENGTH, BLOCK_LENGTH);
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH + 1, BLOCK_LENGTH);
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH, BLOCK_LENGTH + 1);
        checkMult(func, transA, transB, BLOCK_LENGTH + 1, BLOCK_LENGTH + 1, BLOCK_LENGTH + 1);

        // stuff smaller than the block size
        checkMult(func, transA, transB, BLOCK_LENGTH - 1, BLOCK_LENGTH, BLOCK_LENGTH);
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH - 1, BLOCK_LENGTH);
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH, BLOCK_LENGTH - 1);
        checkMult(func, transA, transB, BLOCK_LENGTH - 1, BLOCK_LENGTH - 1, BLOCK_LENGTH - 1);

        // stuff multiple blocks
        checkMult(func, transA, transB, BLOCK_LENGTH*2, BLOCK_LENGTH, BLOCK_LENGTH);
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH*2, BLOCK_LENGTH);
        checkMult(func, transA, transB, BLOCK_LENGTH, BLOCK_LENGTH, BLOCK_LENGTH*2);
        checkMult(func, transA, transB, BLOCK_LENGTH*2, BLOCK_LENGTH*2, BLOCK_LENGTH*2);
        checkMult(func, transA, transB, BLOCK_LENGTH*2 + 4, BLOCK_LENGTH*2 + 3, BLOCK_LENGTH*2 + 2);
    }

    private void checkMult( Method funcThreads, boolean transA, boolean transB,
                            int m, int n, int o ) {
        FMatrixRMaj A_d = RandomMatrices_FDRM.rectangle(m, n, rand);
        FMatrixRMaj B_d = RandomMatrices_FDRM.rectangle(n, o, rand);
        FMatrixRMaj C_d = new FMatrixRMaj(m, o);

        FMatrixRBlock A_b = MatrixOps_FDRB.convert(A_d, BLOCK_LENGTH);
        FMatrixRBlock B_b = MatrixOps_FDRB.convert(B_d, BLOCK_LENGTH);
        FMatrixRBlock C_b = MatrixOps_FDRB.createRandom(m, o, -1, 1, rand, BLOCK_LENGTH);

        if (transA)
            A_b = MatrixOps_FDRB.transpose(A_b, null);

        if (transB)
            B_b = MatrixOps_FDRB.transpose(B_b, null);

        CommonOps_FDRM.mult(A_d, B_d, C_d);
        try {
            funcThreads.invoke(null, A_b, B_b, C_b);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

//        C_d.print();
//        C_b.print();
        assertTrue(GenericMatrixOps_F32.isEquivalent(C_d, C_b, UtilEjml.TEST_F32));
    }
}

