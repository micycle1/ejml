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
import org.ejml.data.CMatrixRMaj;
import org.ejml.dense.row.mult.MatrixMatrixMult_MT_CDRM;

/**
 * Functions from {@link CommonOps_CDRM} with concurrent implementations.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.CommonOps_MT_ZDRM")
public class CommonOps_MT_CDRM {
    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a * b <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { * a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void mult( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        if (b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.mult_reorder(a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.mult_small(a, b, c);
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a * b <br>
     * <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { * a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param realAlpha real component of scaling factor.
     * @param imgAlpha imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void mult( float realAlpha, float imgAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        if (b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.mult_reorder(realAlpha, imgAlpha, a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.mult_small(realAlpha, imgAlpha, a, b, c);
        }
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
    public static void multAdd( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        if (b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.multAdd_reorder(a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.multAdd_small(a, b, c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> +  &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param realAlpha real component of scaling factor.
     * @param imgAlpha imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAdd( float realAlpha, float imgAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        if (b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.multAdd_reorder(realAlpha, imgAlpha, a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.multAdd_small(realAlpha, imgAlpha, a, b, c);
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a<sup>H</sup> * b <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransA( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        if (a.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.multTransA_reorder(a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.multTransA_small(a, b, c);
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a<sup>H</sup> * b <br>
     * <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param realAlpha Real component of scaling factor.
     * @param imagAlpha Imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransA( float realAlpha, float imagAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        // TODO add a matrix vectory multiply here
        if (a.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.multTransA_reorder(realAlpha, imagAlpha, a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.multTransA_small(realAlpha, imagAlpha, a, b, c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = a * b<sup>H</sup> <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransB( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        MatrixMatrixMult_MT_CDRM.multTransB(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c =  &alpha; * a * b<sup>H</sup> <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> {  a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param realAlpha Real component of scaling factor.
     * @param imagAlpha Imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransB( float realAlpha, float imagAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        // TODO add a matrix vectory multiply here
        MatrixMatrixMult_MT_CDRM.multTransB(realAlpha, imagAlpha, a, b, c);
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
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransAB( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        MatrixMatrixMult_MT_CDRM.multTransAB(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = &alpha; * a<sup>H</sup> * b<sup>H</sup><br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param realAlpha Real component of scaling factor.
     * @param imagAlpha Imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransAB( float realAlpha, float imagAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        MatrixMatrixMult_MT_CDRM.multTransAB(realAlpha, imagAlpha, a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a<sup>H</sup> * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransA( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        if (a.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.multAddTransA_reorder(a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.multAddTransA_small(a, b, c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a<sup>H</sup> * b<br>
     * c<sub>ij</sub> =c<sub>ij</sub> +  &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param realAlpha Real component of scaling factor.
     * @param imagAlpha Imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransA( float realAlpha, float imagAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        // TODO add a matrix vectory multiply here
        if (a.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.CMULT_COLUMN_SWITCH) {
            MatrixMatrixMult_MT_CDRM.multAddTransA_reorder(realAlpha, imagAlpha, a, b, c);
        } else {
            MatrixMatrixMult_MT_CDRM.multAddTransA_small(realAlpha, imagAlpha, a, b, c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a * b<sup>H</sup> <br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransB( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        MatrixMatrixMult_MT_CDRM.multAddTransB(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a * b<sup>H</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param realAlpha Real component of scaling factor.
     * @param imagAlpha Imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransB( float realAlpha, float imagAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        // TODO add a matrix vectory multiply here
        MatrixMatrixMult_MT_CDRM.multAddTransB(realAlpha, imagAlpha, a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a<sup>H</sup> * b<sup>H</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not Modified.
     * @param b The right matrix in the multiplication operation. Not Modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransAB( CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        MatrixMatrixMult_MT_CDRM.multAddTransAB(a, b, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a<sup>H</sup> * b<sup>H</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param realAlpha Real component of scaling factor.
     * @param imagAlpha Imaginary component of scaling factor.
     * @param a The left matrix in the multiplication operation. Not Modified.
     * @param b The right matrix in the multiplication operation. Not Modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransAB( float realAlpha, float imagAlpha, CMatrixRMaj a, CMatrixRMaj b, CMatrixRMaj c ) {
        MatrixMatrixMult_MT_CDRM.multAddTransAB(realAlpha, imagAlpha, a, b, c);
    }
}
