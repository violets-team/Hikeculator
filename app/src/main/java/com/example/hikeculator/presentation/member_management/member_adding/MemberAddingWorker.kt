package com.example.hikeculator.presentation.member_management.member_adding

import android.content.Context
import androidx.work.*
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MemberAddingWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    companion object {

        private const val TRIP_ID_KEY = "trip_id"
        private const val USER_UID_KEY = "user_uids"
        const val UNIQUE_NAME = "member_adding"

        fun makeRequest(tripId: String, userUids: Array<String>): OneTimeWorkRequest {
            val workData = workDataOf(
                TRIP_ID_KEY to tripId,
                USER_UID_KEY to userUids
            )

            return OneTimeWorkRequestBuilder<MemberAddingWorker>()
                .setInputData(workData)
                .build()
        }
    }

    private val memberGroupInteractor: MemberGroupInteractor by inject()
    private val tripInteractor: TripInteractor by inject()

    override suspend fun doWork(): Result {
        val tripId = workerParameters.inputData.getString(TRIP_ID_KEY)
        val usersUids = workerParameters.inputData.getStringArray(USER_UID_KEY)?.toList()

        return if (tripId != null && usersUids != null) {
            usersUids.onEach { userUid ->
                memberGroupInteractor.addTripMember(tripId = tripId, userUid = userUid)
            }

            val members = memberGroupInteractor.fetchTripMembers(tripId)

            updateTrip(tripId = tripId, members = members)

            Result.success()
        } else {
            Result.failure()
        }
    }

    private suspend fun updateTrip(tripId: String, members: Set<User>) {
        tripInteractor.retrieveTrip(tripId = tripId)?.let { trip ->
            val totalCalories = NutritionalCalculator.recalculateTripCalorieNorm(
                trip = trip,
                members = members.toTypedArray()
            )

            tripInteractor.insertTrip(trip = trip.copy(totalCalories = totalCalories))
        }
    }
}