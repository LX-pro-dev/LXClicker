package com.example.lxclicker.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lxclicker.R
import com.example.lxclicker.databinding.FragmentCountingBinding
import com.example.lxclicker.ui.viewmodels.CountMode
import com.example.lxclicker.ui.viewmodels.CountViewModel

class CountingFragment : Fragment() {

    private lateinit var viewModel: CountViewModel
    private lateinit var binding: FragmentCountingBinding // DataBinding instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using DataBindingUtil
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_counting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel using ViewModelProvider
        viewModel = ViewModelProvider(requireActivity())[CountViewModel::class.java]

        // Observe count state changes and update UI using DataBinding
        setupUI()

        // Update UI based on the initial mode
        updateSwitchModeBtnIcon(viewModel.mode.value!!)
    }

    private fun setupUI() {

        // Observe the mode LiveData and update the UI accordingly
        viewModel.mode.observe(viewLifecycleOwner) { mode ->

            // Set up the click listener for the count button based on the current mode
            binding.btnCount.setOnClickListener {
                if (mode == CountMode.INCREMENT) {
                    viewModel.incrementCount()
                } else {
                    viewModel.decrementCount()
                }
                // Vibrate the device
                vibrate(100)
            }

            // Update the count button text immediately after mode change
            binding.btnCount.text = viewModel.countState.value.toString()

            // Update the switch mode button icon
            val iconDrawable = if (mode == CountMode.INCREMENT) {
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.baseline_add_circle_outline_24,
                    null
                )
            } else {
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.baseline_remove_circle_outline_24,
                    null
                )
            }
            // Set the icon drawable for the mode switch button
            binding.btnSwitchMode.icon = iconDrawable
        }

        // Set up the long click listener for the reset button
        binding.btnReset.setOnLongClickListener {
            viewModel.resetCount()
            // Vibrate the device
            vibrate(300)
            true// Indicate that the long click is handled
        }

        binding.btnSwitchMode.setOnLongClickListener {
            if (viewModel.mode.value == CountMode.INCREMENT) {
                // Open the dialog fragment for initial value
                openModeDialog()
            } else {
                // Switch to increment mode and reset count
                viewModel.switchMode()
                viewModel.resetCount()
                // Update UI, including icon and count value
                updateSwitchModeBtnIcon(viewModel.mode.value!!)
                // Vibrate the device
                vibrate(300)
                // Optionally, change the theme of the buttons
                // changeTheme()
            }
            true // Indicate that the long click is handled
        }

        viewModel.countState.observe(viewLifecycleOwner) { count ->
            binding.btnCount.text = count.toString()
        }
    }

    private fun openModeDialog() {
        val modeDialogFragment = ModeDialogFragment()
        modeDialogFragment.listener = object : ModeDialogFragment.ModeDialogListener {
            override fun onModeSelected(mode: CountMode, initialValue: Int) {
                viewModel.setInitialValueAndMode(initialValue, mode)
                binding.btnCount.text =
                    viewModel.countState.value.toString() // Update count button text
            }
        }
        modeDialogFragment.show(childFragmentManager, ModeDialogFragment.TAG)
    }

    private fun updateSwitchModeBtnIcon(mode: CountMode) {
        // Update the switch mode button icon
        val iconDrawable = if (mode == CountMode.INCREMENT) {
            ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.baseline_add_circle_outline_24,
                null
            )
        } else {
            ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.baseline_remove_circle_outline_24,
                null
            )
        }
        binding.btnSwitchMode.icon = iconDrawable

        // Update the count button text
        binding.btnCount.text = viewModel.countState.value.toString()
    }

    // Vibrate the device with the specified duration
    private fun vibrate(duration: Long) {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(duration)
        }
    }
}