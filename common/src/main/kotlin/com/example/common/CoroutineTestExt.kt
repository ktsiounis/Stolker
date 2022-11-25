package com.example.quotes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
fun runTestWithDispatcher(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    test: suspend TestScope.() -> Unit,
): TestResult {
    Dispatchers.setMain(testDispatcher)

    return kotlinx.coroutines.test.runTest {
        test()
        Dispatchers.resetMain()
    }
}