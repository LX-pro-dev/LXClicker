package com.example.lxclicker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.lxclicker.ui.viewmodels.CountMode
import com.example.lxclicker.ui.viewmodels.CountViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.verify

class CountViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // The ViewModel to be tested
    private lateinit var viewModel: CountViewModel

    // Mock observer for LiveData
    @Mock
    private lateinit var observer: Observer<Int>

    @Before
    fun setup() {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this)

        // Create the ViewModel
        viewModel = CountViewModel()

        // Observe the LiveData with the mock observer
        viewModel.countState.observeForever(observer)
    }

    @Test
    fun testSwitchMode() {
        // Initially, the mode should be INCREMENT and countState should be 0
        verify(observer).onChanged(0) // Check that onChanged was called with 0
        assertEquals(viewModel.mode.value, CountMode.INCREMENT)
        assertEquals(viewModel.countState.value, 0)

        // Trigger the switchMode function
        viewModel.switchMode()

        // After switching mode, the mode should be DECREMENT and countState should still be 0
        verify(observer).onChanged(0) // Check that onChanged was called with 0
        assertEquals(viewModel.mode.value, CountMode.DECREMENT)
        assertEquals(viewModel.countState.value, 0)
    }

    @Test
    fun testIncrementCount() {
        viewModel.incrementCount()
        Mockito.verify(observer).onChanged(1)
    }

    @Test
    fun testDecrementCount() {
        // Initial count value is 0
        // Call decrementCount
        viewModel.decrementCount()

        // Verify that the LiveData was updated with the new value (should be 0)
        Mockito.verify(observer).onChanged(0)
    }

    @Test
    fun testResetCount() {
        // Create a TestableLiveData with an initial value of 15
        val initialLiveData = TestableLiveData(15)
        // Set up your ViewModel here, with an initial value of 15
        viewModel = CountViewModel()
        viewModel.countState.observeForever(observer)

        // Call resetCount
        viewModel.resetCount()

        // Assert that the captured argument is 0
        assertEquals(0, viewModel.countState.value)
    }

    @Test
    fun testSetInitialValueAndMode() {
        // Set up your ViewModel here, and ensure that the initial countState is 1
        viewModel = CountViewModel()
        viewModel.countState.observeForever(observer)

        // Call the method to be tested
        viewModel.setInitialValueAndMode(1,CountMode.INCREMENT)

        // Verify that observer.onChanged is called with the expected value (1)
        verify(observer).onChanged(1)

        // Check the updated countState value
        assertEquals(1, viewModel.countState.value)
    }
}

class TestableLiveData<T>(initialValue: T) : LiveData<T>() {
    init {
        value = initialValue
    }
}