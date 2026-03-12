/*
 * Copyright (c) 2020, Peter Abeles. All Rights Reserved.
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

package org.ejml.sparse.csc.linsol.lu;

import javax.annotation.Generated;
import org.ejml.UtilEjml;
import org.ejml.data.FGrowArray;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.data.IGrowArray;
import org.ejml.interfaces.decomposition.DecompositionInterface;
import org.ejml.interfaces.linsol.LinearSolverSparse;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.decomposition.lu.LuUpLooking_FSCC;
import org.ejml.sparse.csc.misc.TriangularSolver_FSCC;

import static org.ejml.UtilEjml.adjust;

/**
 * LU Decomposition based solver for square matrices. Uses {@link LuUpLooking_FSCC} internally.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.linsol.lu.LinearSolverLu_DSCC")
public class LinearSolverLu_FSCC implements LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> {

    LuUpLooking_FSCC decomposition;

    private final FGrowArray gx = new FGrowArray();
    private final FGrowArray gb = new FGrowArray();
    private final IGrowArray gperm = new IGrowArray();

    FMatrixSparseCSC Bp = new FMatrixSparseCSC(1, 1, 1);
    FMatrixSparseCSC tmp = new FMatrixSparseCSC(1, 1, 1);

    // Number of rows in A
    int AnumRows,AnumCols;

    public LinearSolverLu_FSCC( LuUpLooking_FSCC decomposition ) {
        this.decomposition = decomposition;
    }

    @Override
    public boolean setA( FMatrixSparseCSC A ) {
        this.AnumRows = A.numRows;
        this.AnumCols = A.numCols;
        return decomposition.decompose(A);
    }

    @Override
    public /**/double quality() {
        return TriangularSolver_FSCC.qualityTriangular(decomposition.getU());
    }

    @Override
    public void solveSparse( FMatrixSparseCSC B, FMatrixSparseCSC X ) {
        X.reshape(AnumCols, B.numCols, X.numRows);

        FMatrixSparseCSC L = decomposition.getL();
        FMatrixSparseCSC U = decomposition.getU();

        final int[] reducePinv = decomposition.getReducePermutationRowInv();
        int[] Pinv = decomposition.getPinv();
        final int[] q = decomposition.isReduceFill() ? decomposition.getReducePermutation() : null;

        FMatrixSparseCSC rhs = B;
        if (reducePinv != null) {
            Bp.reshape(B.numRows, B.numCols, B.nz_length);
            CommonOps_FSCC.permute(reducePinv, B, null, Bp);
            rhs = Bp;
        }

        tmp.reshape(B.numRows, B.numCols, rhs.nz_length);
        CommonOps_FSCC.permute(Pinv, rhs, null, tmp);

        IGrowArray gw = decomposition.getGw();
        IGrowArray gw1 = decomposition.getGxi();

        Bp.reshape(L.numRows, B.numCols, 1);
        TriangularSolver_FSCC.solve(L, true, tmp, Bp, null, gx, gw, gw1);

        if (q != null) {
            TriangularSolver_FSCC.solve(U, false, Bp, tmp, null, gx, gw, gw1);
            int[] qinv = UtilEjml.adjust(gperm, q.length);
            CommonOps_FSCC.permutationInverse(q, qinv, q.length);
            CommonOps_FSCC.permuteRowInv(qinv, tmp, X);
        } else {
            TriangularSolver_FSCC.solve(U, false, Bp, X, null, gx, gw, gw1);
        }
    }

    @Override
    public void setStructureLocked( boolean locked ) {
        decomposition.setStructureLocked(locked);
    }

    @Override
    public boolean isStructureLocked() {
        return decomposition.isStructureLocked();
    }

    @Override
    @SuppressWarnings("NullAway") // Compiler isn't smart enough to realize null condition is impossible
    public void solve( FMatrixRMaj B, FMatrixRMaj X ) {
        UtilEjml.checkReshapeSolve(AnumRows, AnumCols, B, X);

        int[] pinv = decomposition.getPinv();
        float[] x = adjust(gx, X.numRows);
        float[] b = adjust(gb, B.numRows);

        FMatrixSparseCSC L = decomposition.getL();
        FMatrixSparseCSC U = decomposition.getU();

        final boolean reduceFill = decomposition.isReduceFill();
        final int[] q = reduceFill ? decomposition.getReducePermutation() : null;
        final int[] reducePinv = reduceFill ? decomposition.getReducePermutationRowInv() : null;

        // process each column in X and B individually
        for (int colX = 0; colX < X.numCols; colX++) {
            int index = colX;
            for (int i = 0; i < B.numRows; i++, index += X.numCols) b[i] = B.data[index];

            float[] work = x;
            if (reducePinv != null) {
                CommonOps_FSCC.permuteInv(reducePinv, b, x, X.numRows);
                CommonOps_FSCC.permuteInv(pinv, x, b, X.numRows);
                work = b;
            } else {
                CommonOps_FSCC.permuteInv(pinv, b, x, X.numRows);
            }

            TriangularSolver_FSCC.solveL(L, work);
            TriangularSolver_FSCC.solveU(U, work);
            float[] d;
            if (reduceFill) {
                float[] output = work == x ? b : x;
                CommonOps_FSCC.permute(q, work, output, X.numRows);
                d = output;
            } else {
                d = work;
            }
            index = colX;
            for (int i = 0; i < X.numRows; i++, index += X.numCols) X.data[index] = d[i];
        }
    }

    @Override
    public boolean modifiesA() {
        return decomposition.inputModified();
    }

    @Override
    public boolean modifiesB() {
        return false;
    }

    @Override
    public <D extends DecompositionInterface> D getDecomposition() {
        return (D)decomposition;
    }
}
