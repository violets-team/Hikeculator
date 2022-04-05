package com.example.hikeculator.presentation.member_management.member_deleting

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberDeletingViewModel(
    private val workManager: WorkManager
) : ViewModel() {

    private val _isDeletingMemberRequested = MutableStateFlow(value = false)
    val isDeletingMemberRequested = _isDeletingMemberRequested.asStateFlow()

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    fun deleteMember(tripId: String, memberUid: String) {
        workManager.enqueueUniqueWork(
            MemberDeletingWorker.UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            MemberDeletingWorker.makeRequest(memberUid = memberUid, tripId = tripId)
        )
        _isDeletingMemberRequested.value = true
    }
}