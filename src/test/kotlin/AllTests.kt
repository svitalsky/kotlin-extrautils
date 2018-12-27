/*
 * Copyright © 2017–2018 Marcel Svitalsky
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
package cz.mpts.libs.extrautils.kotlin

import cz.mpts.libs.extrautils.kotlin.collections.*
import cz.mpts.libs.extrautils.kotlin.logging.TaskStopwatchTest
import cz.mpts.libs.extrautils.kotlin.values.*
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses


@RunWith(Suite::class)
@SuiteClasses(
    CollectionFunctionsKtTest::class,
    CursorTest::class,
    DateTimeHelpersKtTest::class,
    LazyVarTest::class,
    NumberHelpersKtTest::class,
    OptionalValueTest::class,
    TaskStopwatchTest::class,
    ListToTableTransformerTest::class
)
class AllTests
