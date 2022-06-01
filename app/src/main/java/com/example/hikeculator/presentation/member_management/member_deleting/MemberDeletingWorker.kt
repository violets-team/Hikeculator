package com.example.hikeculator.presentation.member_management.member_deleting

import android.content.Context
import androidx.work.*
import com.example.hikeculator.domain.common.NutritionalCalculator
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MemberDeletingWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    companion object {

        private const val TRIP_ID_KEY = "tripId"
        private const val USER_UID_KEY = "userUid"
        private const val UNIQUE_NAME = "member_deleting"

        fun WorkManager.enqueueMemberDeletingWork(memberUid: String, tripId: String) {
            enqueueUniqueWork(
                UNIQUE_NAME,
                ExistingWorkPolicy.KEEP,
                makeRequest(memberUid = memberUid, tripId = tripId)
            )
        }

        private fun makeRequest(memberUid: String, tripId: String): OneTimeWorkRequest {
            val workData = workDataOf(
                USER_UID_KEY to memberUid,
                TRIP_ID_KEY to tripId
            )

            return OneTimeWorkRequestBuilder<MemberDeletingWorker>()
                .setInputData(workData)
                .build()
        }
    }

    private val memberGroupInteractor: MemberGroupInteractor by inject()
    private val tripInteractor: TripInteractor by inject()

    override suspend fun doWork(): Result {
        val tripId = workerParameters.inputData.getString(TRIP_ID_KEY)
        val userUid = workerParameters.inputData.getString(USER_UID_KEY)

        return if (tripId != null && userUid != null) {
            memberGroupInteractor.removeTripMember(tripId = tripId, userUid = userUid)

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