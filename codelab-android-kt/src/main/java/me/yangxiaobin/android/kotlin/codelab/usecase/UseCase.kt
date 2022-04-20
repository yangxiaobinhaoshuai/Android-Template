/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yangxiaobin.android.kotlin.codelab.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Executes business logic synchronously or asynchronously using Coroutines.
 *
 *
 * Intention for `USE CASE`
 *
 * 1. Use case ONLY accomplish ONE thing.
 * 2. Use case is smallest biz unit, is more granular than others.
 * 3. Use case is used for separating repository and dataSource from viewModels.
 *    According to `Google clean architecture` ( data -> domain -> presentation),
 *    they become a new layer called `domain`.
 * 4. Decouple the functionality of some super `Repository`.
 *
 *
 */
abstract class UseCase<P, R>(private val coroutineDispatcher: CoroutineDispatcher) {


    /** Executes the use case asynchronously and returns a [Result].
     *
     * @return a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            // Moving all use case's executions to the injected dispatcher
            // In production code, this is usually the Default dispatcher (background thread)
            // In tests, this becomes a TestCoroutineDispatcher
            withContext(coroutineDispatcher) { execute(parameters).let { Result.success(it) } }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}
