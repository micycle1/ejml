/*
 * Copyright (c) 2026, Peter Abeles. All Rights Reserved.
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

package org.ejml.sparse.csc.misc;

import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.data.IGrowArray;
import org.ejml.ops.DConvertMatrixStruct;
import org.ejml.sparse.ComputePermutation;
import org.ejml.sparse.FillReducing;
import org.ejml.sparse.csc.factory.FillReductionFactory_DSCC;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestSymmetricRCM {
    @Test
    public void pathGraphProducesExpectedOrder() {
        DMatrixSparseCSC A = pathGraph(4);
        IGrowArray perm = new IGrowArray();

        SymmetricRCM.compute(A, true, perm);

        assertArrayEquals(new int[]{3, 2, 1, 0}, Arrays.copyOf(perm.data, perm.length));
    }

    @Test
    public void noSortProducesPermutation() {
        DMatrixSparseCSC A = pathGraph(5);
        IGrowArray perm = new IGrowArray();

        SymmetricRCM.compute(A, false, perm);

        assertValidPermutation(perm, 5);
    }

    @Test
    public void factoryCopiesRowToColumn() {
        ComputePermutation<DMatrixSparseCSC> cp = FillReductionFactory_DSCC.create(FillReducing.SYMRCM);
        assertNotNull(cp);

        DMatrixSparseCSC A = pathGraph(4);
        cp.process(A);

        assertArrayEquals(Arrays.copyOf(cp.getRow().data, cp.getRow().length),
                Arrays.copyOf(cp.getColumn().data, cp.getColumn().length));
    }

    @Test
    public void factoryRejectsNonSquare() {
        ComputePermutation<DMatrixSparseCSC> cp = FillReductionFactory_DSCC.create(FillReducing.SYMRCM_NO_SORT);
        assertNotNull(cp);

        assertThrows(IllegalArgumentException.class, () -> cp.process(new DMatrixSparseCSC(2, 3, 0)));
    }

    private static DMatrixSparseCSC pathGraph( int length ) {
        DMatrixSparseTriplet triplet = new DMatrixSparseTriplet(length, length, (length - 1)*2);
        for (int i = 0; i < length - 1; i++) {
            triplet.addItem(i, i + 1, 1.0);
            triplet.addItem(i + 1, i, 1.0);
        }
        DMatrixSparseCSC out = new DMatrixSparseCSC(length, length, triplet.nz_length);
        DConvertMatrixStruct.convert(triplet, out);
        return out;
    }

    private static void assertValidPermutation( IGrowArray perm, int n ) {
        assertEquals(n, perm.length);
        boolean[] seen = new boolean[n];
        for (int i = 0; i < perm.length; i++) {
            int v = perm.data[i];
            assertTrue(0 <= v && v < n);
            assertFalse(seen[v]);
            seen[v] = true;
        }
    }
}
