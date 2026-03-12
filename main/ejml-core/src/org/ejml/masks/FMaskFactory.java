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
import org.ejml.data.FMatrixD1;
import org.ejml.data.FMatrixSparseCSC;

/**
 * Utility class to get the corresponding mask builder based on a matrix or primitive array
 */
@Generated("org.ejml.masks.DMaskFactory")
public class FMaskFactory {
    public static FMaskPrimitive.Builder builder( float[] values ) {
        return new FMaskPrimitive.Builder(values);
    }

    public static FMaskPrimitive.Builder builder( FMatrixD1 matrix ) {
        return new FMaskPrimitive.Builder(matrix.data).withNumCols(matrix.numCols);
    }

    /**
     * @param matrix Matrix to be used as a Mask
     * @param structural Whether only the structure of the matrix is relevant or the actual value are considered
     */
    public static MaskBuilder builder( FMatrixSparseCSC matrix, boolean structural ) {
        if (structural) {
            return new FMaskSparseStructural.Builder(matrix);
        } else {
            return new FMaskSparse.Builder(matrix);
        }
    }
}
