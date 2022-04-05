package com.example.hikeculator.presentation.member_management.member_adding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberManagementAddingViewModel(
    private val tripId: String,
    private val memberGroupInteractor: MemberGroupInteractor,
    private val workManager: WorkManager
) : ViewModel() {

    private val _searchedMembers = MutableStateFlow<Set<User>>(emptySet())
    val searchedMembers = _searchedMembers.asStateFlow()

    private val _chosenMembers = MutableStateFlow<Set<User>>(emptySet())
    val chosenMembers = _chosenMembers.asStateFlow()

    private val existedMembers = mutableSetOf<User>()

    private val _isAddingMemberRequested = MutableStateFlow(value = false)
    val isAddingMemberRequested = _isAddingMemberRequested.asStateFlow()

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    init {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_member_getting)
        }

        viewModelScope.launch(context = exceptionHandler) {
            existedMembers.addAll(memberGroupInteractor.fetchTripMembers(tripId = tripId))
            _chosenMembers.value = existedMembers
        }
    }

    fun addToChosenMembers(member: User) {
        val updatedChosenMembers = _chosenMembers.value.toMutableList()
            .apply { add(member) }
            .toSet()

        _chosenMembers.value = updatedChosenMembers
    }

    fun removeFromChosenMembers(member: User) {
        if (existedMembers.none { it.uid == member.uid }) {
            val updatedChosenMembers = _chosenMembers.value.toMutableList()
                .apply { remove(member) }
                .toSet()

            _chosenMembers.value = updatedChosenMembers
        }
    }

    fun clearSearchedMemberList() {
        _searchedMembers.value = emptySet()
    }

    fun searchMemberByEmail(text: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_member_search)
        }

        viewModelScope.launch(context = exceptionHandler) {
            _searchedMembers.value = memberGroupInteractor.searchTripMembers(email = text)
        }
    }

    fun addMembersToTrip(tripId: String) {
        workManager.enqueueUniqueWork(
            MemberAddingWorker.UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            MemberAddingWorker.makeRequest(
                tripId = tripId,
                userUids = _chosenMembers.value.map { it.uid }.toTypedArray()
            )
        )

        _isAddingMemberRequested.value = true
    }
}