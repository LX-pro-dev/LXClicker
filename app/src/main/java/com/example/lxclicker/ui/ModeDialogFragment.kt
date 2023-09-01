package com.example.lxclicker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.lxclicker.databinding.DialogFragmentModeBinding
import com.example.lxclicker.ui.viewmodels.CountMode

class ModeDialogFragment : DialogFragment() {

    // Define a listener interface to communicate with the parent fragment or activity
    interface ModeDialogListener {
        // Callback method for when a mode and initial value are selected
        fun onModeSelected(mode: CountMode, initialValue: Int)
    }

    // Listener instance to send the selected mode and initial value
    var listener: ModeDialogListener? = null

    private lateinit var binding: DialogFragmentModeBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        // Inflate the layout using data binding
        binding = DialogFragmentModeBinding.inflate(LayoutInflater.from(requireContext()))

        // Set the custom layout as the dialog view
        builder.setView(binding.root)
            .setTitle("Enter Initial Value")
            .setPositiveButton("OK") { _, _ ->
                // Get the initial value from the EditText or use 0 if not valid
                val initialValue = binding.edtCountingValue.text.toString().toIntOrNull() ?: 0
                // Call the listener with the selected mode and initial value
                listener?.onModeSelected(CountMode.DECREMENT, initialValue)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Call the listener with Increment mode and value of 0
                listener?.onModeSelected(CountMode.INCREMENT, 0)
            }

        return builder.create()
    }

    companion object {
        const val TAG = "ModeDialogFragment"
    }
}
