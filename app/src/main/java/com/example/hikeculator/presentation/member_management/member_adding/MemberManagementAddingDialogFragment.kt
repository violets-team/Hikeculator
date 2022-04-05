package com.example.hikeculator.presentation.member_management.member_adding

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.DialogFragmentMemberManagementSearchBinding
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.common.showToast
import com.example.hikeculator.presentation.trip_creating.member_search_dialog.TripMemberSearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MemberManagementAddingDialogFragment :
    DialogFragment(R.layout.dialog_fragment_member_management_search) {

    private val binding by viewBinding(DialogFragmentMemberManagementSearchBinding::bind)

    private val args by navArgs<MemberManagementAddingDialogFragmentArgs>()

    private val viewModel by viewModel<MemberManagementAddingViewModel> { parametersOf(args.tripId) }

    private val memberAdapter =
        TripMemberSearchAdapter(onItemClick = ::manageSearchedMemberSelection)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMemberRecyclerView()
        observeAddingMemberState()
        setListeners()
    }

    private fun manageSearchedMemberSelection(member: User) {
        with(viewModel) {
            if (chosenMembers.value.contains(member)) {
                removeFromChosenMembers(member)
            } else {
                addToChosenMembers(member)
            }
        }
    }

    private fun initializeMemberRecyclerView() {
        binding.recyclerViewMembers.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = memberAdapter
        }
    }

    private fun setListeners() {
        binding.editTextMemberSearching.addTextChangedListener { text ->
            manageMemberSearch(emailPrefix = text)
        }

        binding.buttonAddMember.setOnClickListener {
            viewModel.addMembersToTrip(tripId = args.tripId)
        }
    }

    private fun observeAddingMemberState() {
        viewModel.searchedMembers.collectWhenStarted(lifecycleScope) { searchedMembers ->
            memberAdapter.submitList(searchedMembers.toList())
        }

        viewModel.chosenMembers.collectWhenStarted(lifecycleScope) { chosenMembers ->
            memberAdapter.updateAddedMembers(date = chosenMembers.toList())
        }

        viewModel.isAddingMemberRequested.collectWhenStarted(lifecycleScope) { isAddingRequested ->
            if (isAddingRequested) {
                findNavController().popBackStack()
            }
        }

        viewModel.problemMessage.collectWhenStarted(lifecycleScope) { massageId ->
            requireContext().showToast(messageId = massageId)
        }
    }

    private fun manageMemberSearch(emailPrefix: Editable?) {
        if (emailPrefix != null && emailPrefix.isNotEmpty()) {
            viewModel.searchMemberByEmail(emailPrefix.toString())
        } else {
            viewModel.clearSearchedMemberList()
        }
    }
}