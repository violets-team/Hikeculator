package com.example.hikeculator.presentation.member_management

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.hikeculator.R
import com.example.hikeculator.databinding.FragmentMemberManagmentBinding
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.presentation.common.RecyclerViewAnimator
import com.example.hikeculator.presentation.common.collectWhenStarted
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MemberManagementFragment : Fragment(R.layout.fragment_member_managment) {

    private val binding by viewBinding(FragmentMemberManagmentBinding::bind)

    private val args by navArgs<MemberManagementFragmentArgs>()

    private val viewModel by viewModel<MemberManagementViewModel> { parametersOf(args.tripId) }

    private val memberAdapter =
        MemberManagementAdapter(onDeleteItemClick = ::navigateToMemberDeletingDialog)

    private val recyclerViewAnimator by lazy {
        RecyclerViewAnimator(recyclerView = binding.recyclerViewMembers)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMemberRecyclerView()

        viewModel.members.collectWhenStarted(lifecycleScope) { users ->
            memberAdapter.submitList(users.toList())

            recyclerViewAnimator.animateOnlyOnce(
                layoutAnimationId = R.anim.recycler_view_member_managment_layout_animation
            )

            binding.textViewMemberQuantityValue.text = users.size.toString()
        }

        binding.actionButtonAddMember.setOnClickListener {
            MemberManagementFragmentDirections
                .actionMemberManagementFragmentToMemberManagementSearchDialogFragment(
                    tripId = args.tripId
                ).also { findNavController().navigate(directions = it) }
        }
    }

    private fun initializeMemberRecyclerView() {
        binding.recyclerViewMembers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = memberAdapter
        }
    }

    private fun navigateToMemberDeletingDialog(member: User) {
        MemberManagementFragmentDirections
            .actionMemberManagementFragmentToMemberDeletingDialogFragment(
                memberUdi = member.uid,
                memberName = member.name,
                tripId = args.tripId
            ).also { findNavController().navigate(directions = it) }
    }
}