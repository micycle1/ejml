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
import org.ejml.UtilEjml;
import org.ejml.data.FMatrix1Row;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.dense.row.MatrixFeatures_FDRM;

import java.util.Arrays;

/**
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.mult.MatrixVectorMult_DSCC")
public class MatrixVectorMult_FSCC {
    /**
     * c = A*b
     *
     * @param A (Input) Matrix
     * @param b (Input) vector
     * @param offsetB (Input) first index in vector b
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     */
    public static void mult( FMatrixSparseCSC A,
                             float[] b, int offsetB,
                             float[] c, int offsetC ) {
        Arrays.fill(c, offsetC, offsetC + A.numRows, 0);
        multAdd(A, b, offsetB, c, offsetC);
    }

    /**
     * c = c + A*b
     *
     * @param A (Input) Matrix
     * @param b (Input) vector
     * @param offsetB (Input) first index in vector b
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     */
    public static void multAdd( FMatrixSparseCSC A,
                                float[] b, int offsetB,
                                float[] c, int offsetC ) {
        if (b.length - offsetB < A.numCols)
            throw new IllegalArgumentException("Length of 'b' isn't long enough");
        if (c.length - offsetC < A.numRows)
            throw new IllegalArgumentException("Length of 'c' isn't long enough");

        for (int k = 0; k < A.numCols; k++) {
            int idx0 = A.col_idx[k];
            int idx1 = A.col_idx[k + 1];

            for (int indexA = idx0; indexA < idx1; indexA++) {
                c[offsetC + A.nz_rows[indexA]] += A.nz_values[indexA]*b[offsetB + k];
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
     */
    public static void mult( float[] a, int offsetA,
                             FMatrixSparseCSC B,
                             float[] c, int offsetC ) {
        if (a.length - offsetA < B.numRows)
            throw new IllegalArgumentException("Length of 'a' isn't long enough");
        if (c.length - offsetC < B.numCols)
            throw new IllegalArgumentException("Length of 'c' isn't long enough");

        for (int k = 0; k < B.numCols; k++) {
            int idx0 = B.col_idx[k];
            int idx1 = B.col_idx[k + 1];

            float sum = 0;
            for (int indexB = idx0; indexB < idx1; indexB++) {
                sum += a[offsetA + B.nz_rows[indexB]]*B.nz_values[indexB];
            }
            c[offsetC + k] = sum;
        }
    }

    /**
     * scalar = A<sup>T</sup>*B*C
     *
     * @param a (Input) vector
     * @param offsetA Input) first index in vector a
     * @param B (Input) Matrix
     * @param c (Output) vector
     * @param offsetC (Output) first index in vector c
     */
    public static float innerProduct( float[] a, int offsetA,
                                       FMatrixSparseCSC B,
                                       float[] c, int offsetC ) {
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
                sum += a[offsetA + B.nz_rows[indexB]]*B.nz_values[indexB];
            }
            output += sum*c[offsetC + k];
        }

        return output;
    }

    private static void checkInnerProductArguments( FMatrixSparseCSC A, FMatrix1Row B, FMatrixSparseCSC C ) {
        UtilEjml.assertTrue(MatrixFeatures_FDRM.isVector(A), "'A' must be a vector");
        UtilEjml.assertEq(1, A.numCols, "'A' must be a column vector");
        UtilEjml.assertShape(A.getNumElements(), B.numRows, "Length of 'A' vector not equal to number of rows in 'B' matrix");
        UtilEjml.assertTrue(MatrixFeatures_FDRM.isVector(C), "'C' must be a vector");
        UtilEjml.assertEq(1, C.numCols, "'C' must be a column vector");
        UtilEjml.assertShape(C.getNumElements(), B.numCols, "Length of 'C' vector not equal to number of columns in 'B' matrix");
    }

    /**
     * scalar = A<sup>T</sup>*B*C
     *
     * @param A (Input) A column vector that has length m.
     * @param B (Input) A matrix that is m by n.
     * @param C (Input) A column vector that has length n.
     */
    public static float innerProduct( FMatrixSparseCSC A, FMatrixRMaj B, FMatrixSparseCSC C ) {
        checkInnerProductArguments(A, B, C);

        float output = 0.0f;
        for (int i = 0; i < A.nz_length; i++) {
            int b_offset = A.nz_rows[i]*B.numCols;
            float sum = 0.0f;
            for (int j = 0; j < C.nz_length; j++) {
                sum += C.nz_values[j]*B.data[b_offset + C.nz_rows[j]];
            }
            output += A.nz_values[i]*sum;
        }
        return output;
    }

    private static void checkInnerProductSelfSymmetricalArguments( FMatrixSparseCSC A, FMatrix1Row B ) {
        UtilEjml.assertTrue(MatrixFeatures_FDRM.isVector(A), "'A' must be a vector");
        UtilEjml.assertEq(1, A.numCols, "'A' must be a column vector");
        UtilEjml.assertTrue(MatrixFeatures_FDRM.isSquare(B), "'B' must be a square matrix");
        UtilEjml.assertShape(A.getNumElements(), B.numRows, "Length of 'A' vector not equal to number of rows / columns in 'B' matrix");
    }

    /**
     * scalar = A<sup>T</sup>*B*A
     *
     * @param A (Input) A column vector that has length n.
     * @param B (Input) A matrix that is n by n and symmetrical.
     */
    public static float innerProductSelfSymmetrical( FMatrixSparseCSC A, FMatrixRMaj B ) {
        checkInnerProductSelfSymmetricalArguments(A, B);

        float output = 0.0f;
        for (int i = 0; i < A.nz_length; i++) {
            int index1 = A.nz_rows[i];
            float value1 = A.nz_values[i];
            int b_offset = index1*B.numCols;
            float diagonalValue = B.data[b_offset + index1];
            float sum = 0.0f;
            for (int j = i + 1; j < A.nz_length; j++) {
                sum += A.nz_values[j]*B.data[b_offset + A.nz_rows[j]];
            }
            output += value1*value1*diagonalValue + value1*(sum + sum);
        }
        return output;
    }
}
