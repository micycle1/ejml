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

import javax.annotation.Generated;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.data.IGrowArray;

import java.util.Arrays;

/**
 * Symmetric Reverse Cuthill-McKee (RCM) ordering for {@link FMatrixSparseCSC}.
 */
@Generated("org.ejml.sparse.csc.misc.SymmetricRCM")
public final class SymmetricRCM {
    private SymmetricRCM() {}

    /**
     * Computes a reverse Cuthill-McKee permutation for a structurally symmetric sparse matrix.
     *
     * @param A square sparse matrix (assumed structurally symmetric)
     * @param sortByDegree if true, neighbors are ordered by degree before BFS
     * @param perm output permutation (length = A.numCols)
     */
    public static void compute( FMatrixSparseCSC A, boolean sortByDegree, IGrowArray perm ) {
        final int n = A.numCols;
        perm.reshape(n);
        if (n == 0) {
            return;
        }

        final int[] degree = new int[n];
        for (int v = 0; v < n; v++) {
            degree[v] = A.col_idx[v + 1] - A.col_idx[v];
        }

        final int[] colIdx;
        final int[] nzRows;
        if (sortByDegree) {
            colIdx = A.col_idx.clone();
            nzRows = Arrays.copyOf(A.nz_rows, A.nz_length);
            sortAllColumnsByNeighborDegree(n, colIdx, nzRows, degree);
        } else {
            colIdx = A.col_idx;
            nzRows = A.nz_rows;
        }

        final boolean[] label = new boolean[n];
        final int[] queue = new int[n];
        final int[] component = new int[n];
        final int[] order = perm.data;

        int orderLen = 0;

        for (int start = 0; start < n; start++) {
            if (label[start]) {
                continue;
            }

            int compLen = bfsCollectComponent(start, label, queue, component, colIdx, nzRows);
            int root = findMinDegreeVertex(component, compLen, degree);

            for (int i = 0; i < compLen; i++) {
                label[component[i]] = false;
            }

            orderLen = bfsAppendOrder(root, label, queue, order, orderLen, colIdx, nzRows);
        }

        for (int i = 0, j = n - 1; i < j; i++, j--) {
            int tmp = order[i];
            order[i] = order[j];
            order[j] = tmp;
        }

        if (orderLen != n) {
            throw new IllegalStateException("symrcm: internal error. orderLen=" + orderLen + " n=" + n);
        }
    }

    private static int bfsCollectComponent( int root, boolean[] label, int[] queue, int[] component,
                                            int[] colIdx, int[] nzRows ) {
        int head = 0, tail = 0, compLen = 0;

        label[root] = true;
        queue[tail++] = root;

        while (head < tail) {
            int u = queue[head++];
            component[compLen++] = u;

            for (int p = colIdx[u]; p < colIdx[u + 1]; p++) {
                int v = nzRows[p];
                if (!label[v]) {
                    label[v] = true;
                    queue[tail++] = v;
                }
            }
        }
        return compLen;
    }

    private static int bfsAppendOrder( int root, boolean[] label, int[] queue, int[] order, int orderLen,
                                       int[] colIdx, int[] nzRows ) {
        int head = 0, tail = 0;

        label[root] = true;
        queue[tail++] = root;

        while (head < tail) {
            int u = queue[head++];
            order[orderLen++] = u;

            for (int p = colIdx[u]; p < colIdx[u + 1]; p++) {
                int v = nzRows[p];
                if (!label[v]) {
                    label[v] = true;
                    queue[tail++] = v;
                }
            }
        }
        return orderLen;
    }

    private static int findMinDegreeVertex( int[] vertices, int length, int[] degree ) {
        int best = vertices[0];
        int bestDeg = degree[best];

        for (int i = 1; i < length; i++) {
            int v = vertices[i];
            int d = degree[v];
            if (d < bestDeg || (d == bestDeg && v < best)) {
                best = v;
                bestDeg = d;
            }
        }
        return best;
    }

    private static void sortAllColumnsByNeighborDegree( int n, int[] colIdx, int[] nzRows, int[] degree ) {
        for (int col = 0; col < n; col++) {
            int start = colIdx[col];
            int end = colIdx[col + 1] - 1;
            if (end <= start) {
                continue;
            }
            quickSortByDegree(nzRows, start, end, degree);
        }
    }

    private static void quickSortByDegree( int[] a, int lo, int hi, int[] degree ) {
        while (lo < hi) {
            int i = lo, j = hi;
            int pivot = a[lo + ((hi - lo) >> 1)];
            int pivotDeg = degree[pivot];

            while (i <= j) {
                while (lessByDegreeThenIndex(a[i], pivot, degree, pivotDeg)) {
                    i++;
                }
                while (lessByDegreeThenIndex(pivot, a[j], degree, pivotDeg)) {
                    j--;
                }
                if (i <= j) {
                    int tmp = a[i];
                    a[i] = a[j];
                    a[j] = tmp;
                    i++;
                    j--;
                }
            }

            if (j - lo < hi - i) {
                if (lo < j) {
                    quickSortByDegree(a, lo, j, degree);
                }
                lo = i;
            } else {
                if (i < hi) {
                    quickSortByDegree(a, i, hi, degree);
                }
                hi = j;
            }
        }
    }

    private static boolean lessByDegreeThenIndex( int x, int y, int[] degree, int degreeY ) {
        int dx = degree[x];
        if (dx != degreeY) {
            return dx < degreeY;
        }
        return x < y;
    }
}
