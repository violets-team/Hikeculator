package com.example.hikeculator.presentation.trip_creating.member_search_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.DialogFragmentMemberSearchingBinding
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.presentation.common.collectWhenStarted
import com.example.hikeculator.presentation.trip_creating.TripCreatingViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemberSearchDialogFragment : DialogFragment(R.layout.dialog_fragment_member_searching) {

    private val binding by viewBinding(DialogFragmentMemberSearchingBinding::bind)

    private val viewModel by sharedViewModel<TripCreatingViewModel>()

    private val memberAdapter = TripMemberSearchAdapter(
        onItemClick = ::manageSearchedMemberSelection
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMemberRecyclerView()

        viewModel.searchedMembers.collectWhenStarted(lifecycleScope) { searchedMembers ->
            memberAdapter.submitList(searchedMembers.toList())
        }

        viewModel.addedMembers.collectWhenStarted(lifecycleScope) { addedMembers ->
            memberAdapter.updateAddedMembers(date = addedMembers.toList())
        }

        binding.editTextMemberSearching.addTextChangedListener { text ->
            manageMemberSearch(emailPrefix = text)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearSearchedMemberList()
    }

    private fun manageSearchedMemberSelection(member: User) {
        with(viewModel) {
            if (addedMembers.value.contains(member)) {
                removeTripMember(member)
            } else {
                addTripMember(member)
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

    private fun manageMemberSearch(emailPrefix: Editable?) {
        if (emailPrefix != null && emailPrefix.isNotEmpty()) {
            viewModel.searchByEmail(emailPrefix.toString())
        } else {
            viewModel.clearSearchedMemberList()
        }
    }
}