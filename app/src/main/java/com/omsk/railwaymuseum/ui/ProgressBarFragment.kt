package com.omsk.railwaymuseum.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.omsk.railwaymuseum.databinding.FragmentProgressBarBinding
import com.omsk.railwaymuseum.viewmodels.ProgressBarViewModel

class ProgressBarFragment(private val progressBarViewModel: ProgressBarViewModel) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = FragmentProgressBarBinding.inflate(layoutInflater)
            binding.layoutViewModel = progressBarViewModel
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "progressBarFragment"
    }
}