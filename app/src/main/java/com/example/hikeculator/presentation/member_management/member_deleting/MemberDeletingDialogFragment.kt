package com.example.hikeculator.presentation.member_management.member_deleting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.DialogFragmentMemberDeletingBinding
import com.example.hikeculator.presentation.common.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.viewModel

class MemberDeletingDialogFragment : DialogFragment(R.layout.dialog_fragment_member_deleting) {

    private val binding by viewBinding(DialogFragmentMemberDeletingBinding::bind)

    private val args by navArgs<MemberDeletingDialogFragmentArgs>()

    private val viewModel by viewModel<MemberDeletingViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDeletingQuestion()
        observeMemberDeletingState()
        setListeners()
    }

    private fun setDeletingQuestion() {
        binding.textViewMemberDeletionTitle.text = getString(
            R.string.member_deleting_question, args.memberName
        )
    }

    private fun observeMemberDeletingState() {
        viewModel.isDeletingMemberRequested.collectWhenStarted(lifecycleScope) { isMemberDeleted ->
            if (isMemberDeleted) {
                findNavController().popBackStack()
            }
        }

        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { stringId: Int ->
            getString(stringId)
        }
    }

    private fun setListeners() {
        binding.buttonDelete.setOnClickListener {
            viewModel.deleteMember(tripId = args.tripId, memberUid = args.memberUdi)
        }

        binding.buttonDeny.setOnClickListener { findNavController().popBackStack() }
    }
}