package com.lifequest.app.ui

import com.lifequest.app.testing.FakeAnalyticsTracker
import com.lifequest.app.testing.FakeReminderScheduler
import com.lifequest.app.testing.FakeSettingsRepository
import com.lifequest.app.testing.MainDispatcherRule
import com.lifequest.app.ui.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scheduler = FakeReminderScheduler()
    // by lazy: chỉ tạo ViewModel khi test chạy, SAU KHI MainDispatcherRule đã thay Dispatchers.Main.
    // Tạo ngay khi khởi tạo class test thì viewModelScope sẽ bắt nhầm Main dispatcher thật.
    private val viewModel by lazy { SettingsViewModel(FakeSettingsRepository(), scheduler, FakeAnalyticsTracker()) }

    /**
     * stateIn(WhileSubscribed) chỉ chạy khi có người collect, giống như màn hình đang mở.
     * Collect bằng UnconfinedTestDispatcher(testScheduler) để giá trị mới tới ngay lập tức.
     */
    private fun TestScope.subscribeToSettings() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.settings.collect {} }
    }

    @Test
    fun `enabling the reminder schedules it at the saved hour`() = runTest {
        subscribeToSettings()

        viewModel.setReminderEnabled(true)

        assertEquals(20, scheduler.scheduledHour)
        assertEquals(true, viewModel.settings.value.reminderEnabled)
    }

    @Test
    fun `changing the hour reschedules an enabled reminder`() = runTest {
        subscribeToSettings()
        viewModel.setReminderEnabled(true)

        viewModel.changeReminderHour(+2)

        assertEquals(22, scheduler.scheduledHour)
        assertEquals(22, viewModel.settings.value.reminderHour)
    }

    @Test
    fun `hour wraps around midnight`() = runTest {
        subscribeToSettings()

        repeat(5) { viewModel.changeReminderHour(+1) }

        assertEquals(1, viewModel.settings.value.reminderHour)
    }

    @Test
    fun `disabling the reminder cancels it`() = runTest {
        subscribeToSettings()
        viewModel.setReminderEnabled(true)

        viewModel.setReminderEnabled(false)

        assertNull(scheduler.scheduledHour)
    }
}
