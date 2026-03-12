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
package org.ejml.sparse.csc.mult;

import javax.annotation.Generated;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.masks.Mask;
import org.ejml.masks.FMaskPrimitive;
import org.ejml.masks.FMaskSparse;
import org.ejml.ops.FSemiRing;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * based on MartrixVectorMult_FSCC
 */
@Generated("org.ejml.sparse.csc.mult.MatrixVectorMultWithSemiRing_DSCC")
public class MatrixVectorMultWithSemiRing_FSCC {
    /**
     * c = A*b
     *
     * @param A (Input) Matrix
     * @param b (Input) vector
     * @param offsetB (Input) first index in vector b
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     * @param semiRing Semi-Ring to define + and *
     * @param mask Mask for specifying which entries should be overwritten
     */
    public static void mult( FMatrixSparseCSC A,
                             float[] b, int offsetB,
                             float[] c, int offsetC, FSemiRing semiRing, @Nullable Mask mask ) {
        multAdd(A, b, offsetB, c, offsetC, semiRing, mask);
    }

    public static void mult( FMatrixSparseCSC A, float[] b, float[] c, FSemiRing semiRing, @Nullable Mask mask ) {
        mult(A, b, 0, c, 0, semiRing, mask);
    }

    /**
     * c = c + A*b
     *
     * @param A (Input) Matrix
     * @param b (Input) vector
     * @param offsetB (Input) first index in vector b
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     * @param semiRing Semi-Ring to define + and *
     * @param mask Mask for specifying which entries should be overwritten
     */
    public static void multAdd( FMatrixSparseCSC A,
                                float[] b, int offsetB,
                                float[] c, int offsetC, FSemiRing semiRing, @Nullable Mask mask ) {
        if (b.length - offsetB < A.numCols)
            throw new IllegalArgumentException("Length of 'b' isn't long enough");
        if (c.length - offsetC < A.numRows)
            throw new IllegalArgumentException("Length of 'c' isn't long enough");

        // could also just fill where mask.isSet()
        Arrays.fill(c, semiRing.add.id);

        for (int k = 0; k < A.numCols; k++) {
            int idx0 = A.col_idx[k];
            int idx1 = A.col_idx[k + 1];

            for (int indexA = idx0; indexA < idx1; indexA++) {
                c[offsetC + A.nz_rows[indexA]] = semiRing.add.func.apply(
                        c[offsetC + A.nz_rows[indexA]],
                        semiRing.mult.func.apply(A.nz_values[indexA], b[offsetB + k]));
            }
        }

        if (mask != null) {
            // apply mask at once as computation is not column-wise
            float zeroElement = 0;
            if (mask instanceof FMaskPrimitive) {
                zeroElement = ((FMaskPrimitive)mask).zeroElement;
            } else if ((mask instanceof FMaskSparse)) {
                zeroElement = ((FMaskSparse)mask).zeroElement;
            }

            // in case the mask wasn't applied during computation f.i. reduceRowWise
            for (int i = offsetC; i < (c.length - offsetC); i++) {
                // zero unwanted elements
                if (!mask.isSet(i)) {
                    c[i] = zeroElement;
                }
            }
        }
    }

    /**
     * c = a<sup>T</sup>*B
     *
     * @param a (Input) vector
     * @param offsetA Input) first index in vector a
     * @param B (Input) Matrix
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     * @param semiRing Semi-Ring to define + and *
     * @param mask Mask for specifying which entries should be overwritten
     */
    public static void mult( float[] a, int offsetA,
                             FMatrixSparseCSC B,
                             float[] c, int offsetC, FSemiRing semiRing, @Nullable Mask mask ) {
        if (a.length - offsetA < B.numRows)
            throw new IllegalArgumentException("Length of 'a' isn't long enough");
        if (c.length - offsetC < B.numCols)
            throw new IllegalArgumentException("Length of 'c' isn't long enough");

        for (int k = 0; k < B.numCols; k++) {
            if (mask == null || mask.isSet(k)) {
                int idx0 = B.col_idx[k];
                int idx1 = B.col_idx[k + 1];

                float sum = semiRing.add.id;
                for (int indexB = idx0; indexB < idx1; indexB++) {
                    sum = semiRing.add.func.apply(sum, semiRing.mult.func.apply(a[offsetA + B.nz_rows[indexB]], B.nz_values[indexB]));
                }
                c[offsetC + k] = sum;
            }
        }
    }

    public static void mult( float[] a, FMatrixSparseCSC B, float[] c, FSemiRing semiRing, @Nullable Mask mask ) {
        mult(a, 0, B, c, 0, semiRing, mask);
    }

    /**
     * scalar = A<sup>T</sup>*B*C
     *
     * @param a (Input) vector
     * @param offsetA Input) first index in vector a
     * @param B (Input) Matrix
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     * @param semiRing Semi-Ring to define + and *
     */
    public static float innerProduct( float[] a, int offsetA,
                                       FMatrixSparseCSC B,
                                       float[] c, int offsetC, FSemiRing semiRing ) {
        if (a.length - offsetA < B.numRows)
            throw new IllegalArgumentException("Length of 'a' isn't long enough");
        if (c.length - offsetC < B.numCols)
            throw new IllegalArgumentException("Length of 'c' isn't long enough");

        float output = 0;

        for (int k = 0; k < B.numCols; k++) {
            int idx0 = B.col_idx[k];
            int idx1 = B.col_idx[k + 1];

            float sum = 0;
            for (int indexB = idx0; indexB < idx1; indexB++) {
                sum = semiRing.add.func.apply(sum, semiRing.mult.func.apply(a[offsetA + B.nz_rows[indexB]], B.nz_values[indexB]));
            }
            output = semiRing.add.func.apply(output, semiRing.mult.func.apply(sum, c[offsetC + k]));
        }

        return output;
    }
}
