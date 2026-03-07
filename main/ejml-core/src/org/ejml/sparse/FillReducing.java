/*
 * Copyright (c) 2009-2019, Peter Abeles. All Rights Reserved.
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

package org.ejml.sparse;

/**
 * Different types of fill in reducing techniques that can be selected
 *
 * @author Peter Abeles
 */
public enum FillReducing {
    /**
     * No fill reduction permutation will be applied
     */
    NONE,
    /**
     * TESTING ONLY. Completely random permutation
     */
    RANDOM,
    /**
     * TESTING ONLY. Doesn't change the input.
     */
    IDENTITY,
    /**
     * Symmetric Reverse Cuthill-McKee ordering.
     *
     * Graph-based permutation that tends to reduce matrix bandwidth/profile, often reducing fill-in and
     * speeding up sparse factorizations. This variant sorts each node's neighbors by degree, which can
     * improve ordering quality but adds noticeable preprocessing cost on high-degree graphs.
     *
     * Requirements: matrix must be square and structurally symmetric (pattern symmetric; values do not need
     * to be symmetric).
     */
    SYMRCM,
    /**
     * Symmetric Reverse Cuthill-McKee ordering without neighbor sorting.
     *
     * Faster preprocessing (often near-linear in nnz) and often similar quality to {@link #SYMRCM}. Useful
     * when permutation time matters on large problems.
     */
    SYMRCM_NO_SORT
}
