package com.example.juicetracker

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.juicetracker.data.Juice
import com.example.juicetracker.databinding.FragmentEntryDialogBinding
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EntryDialogFragment : DialogFragment() {

    private val args: EntryDialogFragmentArgs by navArgs()
    private val viewModel: EntryViewModel by viewModels { AppViewModelProvider.Factory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentEntryDialogBinding.inflate(LayoutInflater.from(requireContext()))

        if (args.juiceId > 0) {
            viewLifecycleOwnerLiveData.observe(this) { owner ->
                owner?.lifecycleScope?.launch {
                    viewModel.getJuiceStream(args.juiceId).collectLatest { juice ->
                        if (juice != null) {
                            binding.juiceName.setText(juice.name)
                            binding.juiceDescription.setText(juice.description)
                            binding.juiceColor.setText(juice.color)
                            binding.juiceRating.setText(juice.rating.toString())
                        }
                    }
                }
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        binding.saveButton.setOnClickListener {
            val colorText = binding.juiceColor.text.toString()
            val colorName = try { Color.parseColor(colorText); colorText } catch (_: IllegalArgumentException) { "gray" }
            val rating = binding.juiceRating.text.toString().toIntOrNull() ?: 0
            val juice = Juice(
                id = args.juiceId,
                name = binding.juiceName.text.toString(),
                description = binding.juiceDescription.text.toString(),
                color = colorName,
                rating = rating
            )
            viewModel.saveJuice(juice)
            dismissAllowingStateLoss()
        }

        return dialog
    }
}
