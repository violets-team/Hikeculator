package com.example.hikeculator.presentation.member_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberManagementViewModel(
    tripId: String,
    private val memberGroupInteractor: MemberGroupInteractor,
    tripInteractor: TripInteractor
) : ViewModel() {

    private val trip = tripInteractor.fetchTrip(tripId = tripId).shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 0
    )

    private val _members = MutableSharedFlow<Set<User>>()
    val members = _members.asSharedFlow()

    private val _problemMessage = MutableSharedFlow<Int>()
    val problemMessage = _problemMessage.asSharedFlow()

    init {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _problemMessage.tryEmit(R.string.problem_with_member_getting)
        }

        viewModelScope.launch(context = exceptionHandler) {
            trip.collect { trip ->
                if (trip == null) {
                    _problemMessage.emit(R.string.problem_with_member_getting)
                } else {
                    _members.emit(memberGroupInteractor.fetchTripMembers(tripId = trip.id))
                }
            }
        }
    }
}