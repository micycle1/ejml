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

package org.ejml.masks;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Generated("org.ejml.masks.TestDMasksPrimitive")
public class TestFMasksPrimitive extends EjmlStandardJUnit {

    @Test
    void primitiveArray() {
        float[] values = {2, 0, 4, 0, 0, -1};

        FMaskPrimitive.Builder maskBuilder = FMaskFactory.builder(values);
        FMaskPrimitive mask = maskBuilder.withNegated(false).build();
        FMaskPrimitive negated_mask = maskBuilder.withNegated(true).build();
        boolean[] expected = {true, false, true, false, false, true};

        for (int i = 0; i < values.length; i++) {
            assertEquals(mask.isSet(i), expected[i]);
            assertEquals(negated_mask.isSet(i), !expected[i]);
        }
    }

    @Test
    void denseMatrix() {
        int dim = 20;
        FMatrixRMaj matrix = RandomMatrices_FDRM.rectangle(dim, dim, new Random(42));

        FMaskPrimitive.Builder maskBuilder = FMaskFactory.builder(matrix);
        FMaskPrimitive mask = maskBuilder.withNegated(false).build();
        FMaskPrimitive negated_mask = maskBuilder.withNegated(true).build();

        for (int row = 0; row < dim; row++) {
            for (int col = 0; col < dim; col++) {
                boolean expected = (matrix.get(row, col) != 0);
                assertEquals(mask.isSet(row, col), expected);
                assertEquals(negated_mask.isSet(row, col), !expected);
            }
        }
    }

    @Test
    void maxEntries() {
        Mask mask = FMaskFactory.builder(new float[100]).build();
        assertEquals(100, mask.maxMaskedEntries());
    }
}
