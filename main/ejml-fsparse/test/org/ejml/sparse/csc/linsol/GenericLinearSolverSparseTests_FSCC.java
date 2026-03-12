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

package org.ejml.sparse.csc.linsol;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.EjmlUnitTests;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.interfaces.linsol.LinearSolverSparse;
import org.ejml.sparse.FillReducing;
import org.ejml.sparse.csc.CommonOps_FSCC;
import org.ejml.sparse.csc.RandomMatrices_FSCC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Generic tests for linear solvers
 *
 * @author Peter Abeles
 */
// TODO Add a test that makes sure identify permutation produces identical results to no permutation
@Generated("org.ejml.sparse.csc.linsol.GenericLinearSolverSparseTests_DSCC")
public abstract class GenericLinearSolverSparseTests_FSCC extends EjmlStandardJUnit {
    protected FillReducing[] permutationTests = new FillReducing[]{FillReducing.NONE, FillReducing.IDENTITY};

    // used to adjust tolerance threshold
    protected float equalityTolerance = UtilEjml.TEST_F32;

    protected boolean canHandleTall = true;
    protected boolean canHandleWide = true;
    protected boolean canDecomposeZeros = true;
    protected boolean canLockStructure = true;

    public abstract LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> createSolver( FillReducing permutation );

    /**
     * Create a random matrix. The exact shape is determined by the implementation but all allowable shapes for this
     * size should be randomized
     */
    public abstract FMatrixSparseCSC createA( int size );

    public FMatrixRMaj create( int rows, int cols ) {
        return RandomMatrices_FDRM.rectangle(rows, cols, rand);
    }

    public FMatrixSparseCSC createSparse( int rows, int cols ) {
        return RandomMatrices_FSCC.rectangle(rows, cols, rows*cols, rand);
    }

    @Test void randomSolveable() {
        for (FillReducing perm : permutationTests) {
//            System.out.println("perm = "+perm);

            LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> solver = createSolver(perm);

            for (int N : new int[]{10, 20}) {
                for (int mc = 0; mc < 30; mc++) {
//                    System.out.println("-=-=-=-=-=-=-=-=      "+N+" mc "+mc);
                    FMatrixSparseCSC A = createA(N);
                    FMatrixSparseCSC A_cpy = A.copy();
                    FMatrixRMaj X = create(A.numCols, 3);
                    FMatrixRMaj B = new FMatrixRMaj(1, 1);

                    // create B from X so that there is a valid solution in the tall case
                    CommonOps_FSCC.mult(A, X, B);
                    FMatrixRMaj foundB = B.createLike();
                    FMatrixRMaj B_cpy = B.copy();

                    assertTrue(solver.setA(A));
                    // it should resize X, so let's mess it up
                    X.reshape(1, 1);
                    solver.solve(B, X);
                    CommonOps_FSCC.mult(A, X, foundB);

                    EjmlUnitTests.assertEquals(B_cpy, foundB, equalityTolerance);

                    if (!solver.modifiesA()) {
                        EjmlUnitTests.assertEquals(A, A_cpy, equalityTolerance);
                    }
                    if (!solver.modifiesB()) {
                        EjmlUnitTests.assertEquals(B, B_cpy, equalityTolerance);
                    }
                }
            }
        }
    }

    @Test void randomSolveable_Sparse() {
        for (FillReducing perm : permutationTests) {
//            System.out.println("perm = "+perm);

            LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> solver = createSolver(perm);

            for (int N : new int[]{1, 2, 5, 10, 20}) {
                for (int mc = 0; mc < 20; mc++) {
//                    System.out.println("-=-=-=-=-=-=-=-=      "+N+" mc "+mc);
                    FMatrixSparseCSC A = createA(N);
                    FMatrixSparseCSC A_cpy = A.copy();
                    FMatrixSparseCSC X = createSparse(A.numCols, 3);
                    FMatrixSparseCSC B = new FMatrixSparseCSC(1, 1, 1);
                    FMatrixSparseCSC foundX = X.createLike();

                    // compute the solution
                    CommonOps_FSCC.mult(A, X, B);
                    FMatrixSparseCSC B_cpy = B.copy();

//                    System.out.println("--- A");
//                    A.print();
                    assertTrue(solver.setA(A));
                    // it should resize X, so let's mess it up
                    foundX.reshape(1, 1);
                    solver.solveSparse(B, foundX);
                    assertTrue(CommonOps_FSCC.checkStructure(foundX));

                    EjmlUnitTests.assertEquals(X, foundX, equalityTolerance);

                    // should never be modified
                    EjmlUnitTests.assertEquals(A, A_cpy, equalityTolerance);
                    EjmlUnitTests.assertEquals(B, B_cpy, equalityTolerance);
                }
            }
        }
    }

    /**
     * Give it a matrix that's all zeros and see if it fails.
     */
    @Test void handleAllZeros() {
        FMatrixSparseCSC A = new FMatrixSparseCSC(10, 10, 0);

        LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> solver = createSolver(FillReducing.NONE);

        assertEquals(canDecomposeZeros, solver.setA(A));
    }

    /**
     * Provides wide or tall matrices and see if it throws an exception
     */
    @Test void checkFailByShape_Tall() {
        if (canHandleTall) {
            return;
        }
        try {
            LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> solver = createSolver(FillReducing.NONE);
            FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(10, 5, 50, rand);
            solver.setA(A);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test void checkFailByShape_Wide() {
        if (canHandleWide) {
            return;
        }
        try {
            LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> solver = createSolver(FillReducing.NONE);
            FMatrixSparseCSC A = RandomMatrices_FSCC.rectangle(5, 10, 50, rand);
            solver.setA(A);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException ignore) {
        }
    }

    @Test void quality() {
        FMatrixSparseCSC A_good = CommonOps_FSCC.diag(4, 3, 2, 1);
        FMatrixSparseCSC A_bad = CommonOps_FSCC.diag(4, 3, 2, 0.1f);

        LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> solver = createSolver(FillReducing.NONE);

        assertTrue(solver.setA(A_good));
        float q_good;
        try {
            q_good = (float)solver.quality();
        } catch (IllegalArgumentException e) {
            // quality is not supported
            return;
        }

        assertTrue(solver.setA(A_bad));
        float q_bad = (float)solver.quality();

        assertTrue(q_bad < q_good);
    }

    @Test void ifCanNotLockThrowException() {
        LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> d = createSolver(FillReducing.NONE);
        if (canLockStructure) {
            d.setStructureLocked(true);
        } else {
            try {
                d.setStructureLocked(true);
                fail("RuntimeException should have been thrown");
            } catch (RuntimeException ignore) {
            }
        }
    }

    @Test void lockingDoesNotChangeSolution() {
        if (!canLockStructure)
            return;

        LinearSolverSparse<FMatrixSparseCSC, FMatrixRMaj> d = createSolver(FillReducing.NONE);

        FMatrixSparseCSC A = createA(10);
        FMatrixRMaj B = RandomMatrices_FDRM.rectangle(A.numRows, 5, rand);
        FMatrixRMaj X0 = new FMatrixRMaj(A.numCols, 5);
        FMatrixRMaj X1 = new FMatrixRMaj(A.numCols, 5);

        assertTrue(d.setA((FMatrixSparseCSC)A.copy()));
        d.solve(B.copy(), X0);

        assertFalse(d.isStructureLocked());
        d.setStructureLocked(true);
        assertTrue(d.isStructureLocked());

        assertTrue(d.setA(A));
        d.solve(B, X1);

        EjmlUnitTests.assertEquals(X0, X1, UtilEjml.TEST_F32);
    }
}
