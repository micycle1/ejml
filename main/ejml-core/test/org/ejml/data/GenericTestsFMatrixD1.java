/*
 * Copyright (c) 2022, Peter Abeles. All Rights Reserved.
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

package org.ejml.data;

import javax.annotation.Generated;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.data.GenericTestsDMatrixD1")
public abstract class GenericTestsFMatrixD1 extends GenericTestsFMatrix {

    @Override
    protected abstract FMatrixD1 createMatrix( int numRows, int numCols );

    @Override
    public void allTests() {
        super.allTests();
        testReshape();
        testReshape_Negative();
        testReshape_Zero();
        testSetAndGet_1D();
    }

    public void testReshape() {
        FMatrixD1 mat = createMatrix(3, 2);

        float[] origData = mat.getData();

        mat.reshape(6, 1, false);

        assertSame(origData, mat.getData());
        assertEquals(1, mat.getNumCols());
        assertEquals(6, mat.getNumRows());
    }

    public void testReshape_Negative() {
        ReshapeMatrix mat = createMatrix(2, 2);

        // set each axis to negative independently to ensure they are both checked
        try {
            mat.reshape(-1, 2);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException ignore) {
            // it should throw an exception. Now stop bothering me error prone!
        }

        try {
            mat.reshape(2, -1);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException ignore) {
            // it should throw an exception. Now stop bothering me error prone!
        }

        // Turns out it was only throwing an exception because the array size is negative when rows*cols
        try {
            mat.reshape(-1, -1);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException ignore) {
            // it should throw an exception. Now stop bothering me error prone!
        }
    }

    /**
     * 0x0 matrix should be allowed.
     */
    public void testReshape_Zero() {
        ReshapeMatrix mat = createMatrix(2, 3);
        mat.reshape(0, 0);
        assertEquals(0, mat.getNumRows());
        assertEquals(0, mat.getNumCols());
    }

    public void testSetAndGet_1D() {
        FMatrixD1 mat = createMatrix(3, 4);

        int indexA = mat.getIndex(1, 2);
        int indexB = mat.getIndex(2, 1);

        assertTrue(indexA != indexB);

        mat.set(indexA, 2.0f);

        assertEquals(0, mat.get(indexB), 1e-6);
        assertEquals(2, mat.get(indexA), 1e-6);
    }
}