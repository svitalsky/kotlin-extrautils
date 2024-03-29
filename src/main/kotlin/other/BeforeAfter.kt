/*
 * Copyright © 2017–2021 Marcel Svitalsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.mpts.libs.extrautils.kotlin.other

inline fun <T> beforeCommandIf(command: () -> T, condition: Boolean, before: () -> Unit) =
    condition.let { cond ->
        if (cond) before()
    }.run { command() }

inline fun <T> afterCommandIf(command: () -> T, condition: Boolean, after: () -> Unit) =
    command().also {
        if (condition) after()
    }

inline fun <T> afterCommandIf(command: () -> T, condition: Boolean, after: (T) -> Unit) =
    command().also {
        if (condition) after(it)
    }


inline fun runIf(condition: Boolean, condCommand: () -> Unit) : Unit =
    run {
        if (condition) condCommand()
    }

inline infix fun <T> Unit.andThen(command: () -> T): T = command()
