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
package org.ejml.dense.row;

import javax.annotation.Generated;
import org.ejml.EjmlParameters;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrix1Row;
import org.ejml.data.FMatrixD1;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.misc.TransposeAlgs_MT_FDRM;
import org.ejml.dense.row.mult.MatrixMatrixMult_MT_FDRM;
import org.ejml.dense.row.mult.MatrixMultProduct_FDRM;
import org.ejml.dense.row.mult.MatrixMultProduct_MT_FDRM;
import org.ejml.dense.row.mult.VectorVectorMult_FDRM;
import org.jetbrains.annotations.Nullable;

import static org.ejml.UtilEjml.reshapeOrDeclare;

/**
 * Functions from {@link CommonOps_FDRM} with concurrent implementations.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.CommonOps_MT_DDRM")
public class CommonOps_MT_FDRM {
    private CommonOps_MT_FDRM(){}

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a * b <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T mult( T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numRows, b.numCols);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.mult_reorder(a, b, output);

        return output;
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a * b <br>
     * <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { * a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T mult( float alpha, T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numRows, b.numCols);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.mult_reorder(alpha, a, b, output);

        return output;
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a<sup>T</sup> * b <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multTransA( T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numCols, b.numCols);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.multTransA_reorder(a, b, output);

        return output;
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a<sup>T</sup> * b <br>
     * <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multTransA( float alpha, T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numCols, b.numCols);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.multTransA_reorder(alpha, a, b, output);

        return output;
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = a * b<sup>T</sup> <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multTransB( T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numRows, b.numRows);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.multTransB(a, b, output);

        return output;
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c =  &alpha; * a * b<sup>T</sup> <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> {  a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multTransB( float alpha, T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numRows, b.numRows);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.multTransB(alpha, a, b, output);

        return output;
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multTransAB( T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numCols, b.numRows);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.multTransAB(a, b, output);

        return output;
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = &alpha; * a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multTransAB( float alpha, T a, T b, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numCols, b.numRows);
        UtilEjml.checkSameInstance(a, output);
        UtilEjml.checkSameInstance(b, output);

        MatrixMatrixMult_MT_FDRM.multTransAB(alpha, a, b, output);

        return output;
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAdd( FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAdd_reorder(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> +  &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAdd( float alpha, FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAdd_reorder(alpha, a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a<sup>T</sup> * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransA( FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAddTransA_reorder(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a<sup>T</sup> * b<br>
     * c<sub>ij</sub> =c<sub>ij</sub> +  &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha scaling factor
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransA( float alpha, FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAddTransA_reorder(alpha, a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a * b<sup>T</sup> <br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransB( FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAddTransB(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a * b<sup>T</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransB( float alpha, FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAddTransB(alpha, a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not Modified.
     * @param b The right matrix in the multiplication operation. Not Modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransAB( FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAddTransAB(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not Modified.
     * @param b The right matrix in the multiplication operation. Not Modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransAB( float alpha, FMatrix1Row a, FMatrix1Row b, FMatrix1Row c ) {
        MatrixMatrixMult_MT_FDRM.multAddTransAB(alpha, a, b, c);
    }

    /**
     * <p>Computes the matrix multiplication inner product:<br>
     * <br>
     * c = a<sup>T</sup> * a <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * a<sub>kj</sub>}
     * </p>
     *
     * <p>
     * Is faster than using a generic matrix multiplication by taking advantage of symmetry. For
     * vectors there is an even faster option, see {@link VectorVectorMult_FDRM#innerProd(FMatrixD1, FMatrixD1)}
     * </p>
     *
     * @param a The matrix being multiplied. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multInner( T a, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numCols, a.numCols);
        UtilEjml.checkSameInstance(a, output);

        if (a.numCols >= EjmlParameters.MULT_INNER_SWITCH) {
            MatrixMultProduct_MT_FDRM.inner_reorder(a, output);
        } else {
            MatrixMultProduct_FDRM.inner_small(a, output);
        }
        return output;
    }

    /**
     * <p>Computes the matrix multiplication outer product:<br>
     * <br>
     * c = a * a<sup>T</sup> <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:m</sub> { a<sub>ik</sub> * a<sub>jk</sub>}
     * </p>
     *
     * <p>
     * Is faster than using a generic matrix multiplication by taking advantage of symmetry.
     * </p>
     *
     * @param a The matrix being multiplied. Not modified.
     * @param output Where the results of the operation are stored. Modified.
     */
    public static <T extends FMatrix1Row> T multOuter( T a, @Nullable T output ) {
        output = reshapeOrDeclare(output, a, a.numRows, a.numRows);
        UtilEjml.checkSameInstance(a, output);
        MatrixMultProduct_MT_FDRM.outer(a, output);
        return output;
    }

    /**
     * <p>Performs an "in-place" transpose.</p>
     *
     * <p>
     * For square matrices the transpose is truly in-place and does not require
     * additional memory. For non-square matrices, internally a temporary matrix is declared and
     * {@link #transpose(FMatrixRMaj, FMatrixRMaj)} is invoked.
     * </p>
     *
     * @param mat The matrix that is to be transposed. Modified.
     */
    public static void transpose( FMatrixRMaj mat ) {
        if (mat.numCols == mat.numRows) {
            TransposeAlgs_MT_FDRM.square(mat);
        } else {
            FMatrixRMaj b = new FMatrixRMaj(mat.numCols, mat.numRows);
            transpose(mat, b);
            mat.setTo(b);
        }
    }

    /**
     * <p>
     * Transposes matrix 'a' and stores the results in 'b':<br>
     * <br>
     * b<sub>ij</sub> = a<sub>ji</sub><br>
     * where 'b' is the transpose of 'a'.
     * </p>
     *
     * @param A The original matrix. Not modified.
     * @param A_tran Where the transpose is stored. If null a new matrix is created. Modified.
     * @return The transposed matrix.
     */
    public static FMatrixRMaj transpose( FMatrixRMaj A, @Nullable FMatrixRMaj A_tran ) {
        A_tran = reshapeOrDeclare(A_tran, A.numCols, A.numRows);

        if (A.numRows > EjmlParameters.TRANSPOSE_SWITCH &&
                A.numCols > EjmlParameters.TRANSPOSE_SWITCH)
            TransposeAlgs_MT_FDRM.block(A, A_tran, EjmlParameters.BLOCK_WIDTH);
        else
            TransposeAlgs_MT_FDRM.standard(A, A_tran);

        return A_tran;
    }
}
