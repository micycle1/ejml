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

package org.ejml.dense.block;

import javax.annotation.Generated;
import org.ejml.EjmlStandardJUnit;
import org.ejml.UtilEjml;
import org.ejml.data.FMatrixRBlock;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.FSubmatrixD1;
import org.ejml.dense.row.RandomMatrices_FDRM;
import org.ejml.generic.GenericMatrixOps_F32;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Generated("org.ejml.dense.block.TestInnerRankUpdate_MT_DDRB")
class TestInnerRankUpdate_MT_FDRB extends EjmlStandardJUnit {
    int N = 10;

    @Test
    void rankNUpdate() {
        // the matrix being updated is a whole block
        checkRankNUpdate(N, N - 2);

        // the matrix being updated is multiple blocks + a fraction
        checkRankNUpdate(N*2 + 1, N - 2);

        // matrix being updated is less than a block
        checkRankNUpdate(N - 1, N - 2);
    }

    private void checkRankNUpdate( int lengthA, int heightB ) {
        float alpha = -2.0f;
        FMatrixRMaj origA = RandomMatrices_FDRM.rectangle(lengthA, lengthA, -1.0f, 1.0f, rand);
        FMatrixRMaj origB = RandomMatrices_FDRM.rectangle(heightB, lengthA, -1.0f, 1.0f, rand);

        FMatrixRBlock expectedA = MatrixOps_FDRB.convert(origA, N);
        FMatrixRBlock foundA = MatrixOps_FDRB.convert(origA, N);

        FMatrixRBlock blockB = MatrixOps_FDRB.convert(origB, N);

        FSubmatrixD1 subExpected = new FSubmatrixD1(expectedA, 0, origA.numRows, 0, origA.numCols);
        FSubmatrixD1 subFound = new FSubmatrixD1(foundA, 0, origA.numRows, 0, origA.numCols);

        FSubmatrixD1 subB = new FSubmatrixD1(blockB, 0, origB.numRows, 0, origB.numCols);

        InnerRankUpdate_FDRB.rankNUpdate(N, alpha, subFound, subB);
        InnerRankUpdate_MT_FDRB.rankNUpdate(N, alpha, subExpected, subB);

        assertTrue(GenericMatrixOps_F32.isEquivalent(expectedA, foundA, UtilEjml.TEST_F32));
    }

    @Test
    void symmRankNMinus_U() {
        // the matrix being updated is a whole block
        checkSymmRankNMinus_U(N, N - 2);

        // the matrix being updated is multiple blocks + a fraction
        checkSymmRankNMinus_U(N*2 + 1, N - 2);

        // matrix being updated is less than a block
        checkSymmRankNMinus_U(N - 1, N - 2);
    }

    private void checkSymmRankNMinus_U( int lengthA, int heightB ) {
        FMatrixRBlock expectA = MatrixOps_FDRB.convert(RandomMatrices_FDRM.symmetricPosDef(lengthA, rand));
        FMatrixRBlock foundA = expectA.copy();
        FMatrixRBlock B = MatrixOps_FDRB.createRandom(heightB, lengthA, -1.0f, 1.0f, rand, N);

        FSubmatrixD1 subExpect = new FSubmatrixD1(expectA, 0, expectA.numRows, 0, expectA.numCols);
        FSubmatrixD1 subFound = new FSubmatrixD1(foundA, 0, expectA.numRows, 0, expectA.numCols);
        FSubmatrixD1 subB = new FSubmatrixD1(B, 0, B.numRows, 0, B.numCols);

        InnerRankUpdate_FDRB.symmRankNMinus_U(N, subExpect, subB);
        InnerRankUpdate_MT_FDRB.symmRankNMinus_U(N, subFound, subB);

        assertTrue(GenericMatrixOps_F32.isEquivalentTriangle(true, expectA, foundA, UtilEjml.TEST_F32));
    }

    @Test
    void symmRankNMinus_L() {
        // the matrix being updated is a whole block
        checkSymmRankNMinus_L(N, N - 2);

        // the matrix being updated is multiple blocks + a fraction
        checkSymmRankNMinus_L(N*2 + 1, N - 2);

        // matrix being updated is less than a block
        checkSymmRankNMinus_L(N - 1, N - 2);
    }

    private void checkSymmRankNMinus_L( int lengthA, int widthB ) {
        FMatrixRBlock expectA = MatrixOps_FDRB.convert(RandomMatrices_FDRM.symmetricPosDef(lengthA, rand));
        FMatrixRBlock foundA = expectA.copy();
        FMatrixRBlock B = MatrixOps_FDRB.createRandom(lengthA, widthB, -1.0f, 1.0f, rand, N);

        FSubmatrixD1 subExpect = new FSubmatrixD1(expectA, 0, expectA.numRows, 0, expectA.numCols);
        FSubmatrixD1 subFound = new FSubmatrixD1(foundA, 0, expectA.numRows, 0, expectA.numCols);
        FSubmatrixD1 subB = new FSubmatrixD1(B, 0, B.numRows, 0, B.numCols);

        InnerRankUpdate_FDRB.symmRankNMinus_L(N, subExpect, subB);
        InnerRankUpdate_MT_FDRB.symmRankNMinus_L(N, subFound, subB);

        assertTrue(GenericMatrixOps_F32.isEquivalentTriangle(false, expectA, foundA, UtilEjml.TEST_F32));
    }
}

