package com.example.hikeculator.presentation.member_management.member_deleting

import android.content.Context
import androidx.work.*
import com.example.hikeculator.domain.interactors.MemberGroupInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MemberDeletingWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    companion object {

        const val TRIP_ID_KEY = "tripId"
        const val USER_UID_KEY = "userUid"
        const val UNIQUE_NAME = "member_deleting"

        fun makeRequest(memberUid: String, tripId: String): OneTimeWorkRequest {
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

    override suspend fun doWork(): Result {
        val tripId = workerParameters.inputData.getString(TRIP_ID_KEY)
        val userUid = workerParameters.inputData.getString(USER_UID_KEY)

        return if (tripId != null && userUid != null) {
            memberGroupInteractor.removeTripMember(tripId = tripId, userUid = userUid)
            Result.success()
        } else {
            Result.failure()
        }
    }
}