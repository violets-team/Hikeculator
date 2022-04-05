package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.getTripDocument
import com.example.hikeculator.data.common.getUserCollection
import com.example.hikeculator.data.common.mapToUser
import com.example.hikeculator.data.fiebase.USER_COLLECTION_NAME
import com.example.hikeculator.data.fiebase.entities.FirestoreTrip
import com.example.hikeculator.data.fiebase.entities.FirestoreUser
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.MemberGroupRepository
import com.example.hikeculator.domain.repositories.UserProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class MemberGroupRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val userProfileRepository: UserProfileRepository
) : MemberGroupRepository {

    companion object {

        private const val UNICODE_RANGE = "\uf8ff"
    }

    override suspend fun addTripMember(tripId: String, userUid: String) {
        userProfileRepository.addTripIdToUserProfile(userUid = userUid, tripId = tripId)

        firestore.getTripDocument(tripId = tripId).let { documentReference ->
            documentReference.get()
                .await()
                ?.toObject<FirestoreTrip>()
                ?.run { memberUids.toMutableList().apply { add(userUid) }.toList() }
                ?.let { updatedUserUdis ->
                    documentReference.update(FirestoreTrip::memberUids.name, updatedUserUdis)
                        .await()
                }
        }
    }

    override suspend fun removeTripMember(tripId: String, userUid: String) {
        firestore.getTripDocument(tripId = tripId).let { documentReference ->
            documentReference.get()
                .await()
                ?.toObject<FirestoreTrip>()
                ?.run { memberUids.toMutableList().apply { remove(userUid) }.toList() }
                ?.let { updatedUserUdis ->
                    documentReference.update(FirestoreTrip::memberUids.name, updatedUserUdis)
                        .await()
                }
        }
        userProfileRepository.removeTripIdFromUserProfile(userUid = userUid, tripId = tripId)
    }

    override suspend fun fetchTripMembers(tripId: String): Set<User> {
        firestore.getTripDocument(tripId = tripId)
            .get()
            .await()
            ?.toObject<FirestoreTrip>()
            .let { firestoreTrip ->
                return if (firestoreTrip == null) {
                    emptySet()
                } else {
                    val memberUids = firestoreTrip.memberUids
                    val members = mutableListOf<FirestoreUser>()

                    for (step in 0..firestoreTrip.memberUids.size / 10) {
                        val memberUidGroup = memberUids.drop(10 * step).take(10)

                        if (memberUidGroup.isNotEmpty()) {
                            firestore.getUserCollection()
                                .whereIn(FirestoreUser::uid.name, memberUidGroup)
                                .get()
                                .await()
                                .toObjects<FirestoreUser>()
                                .also { firestoreUsers -> members.addAll(firestoreUsers) }
                        }
                    }
                    members.map { firestoreUser -> firestoreUser.mapToUser() }.toSet()
                }
            }
    }

    override suspend fun fetchTripMember(userUid: String): User? {
        return firestore.collection(USER_COLLECTION_NAME)
            .document(userUid)
            .get()
            .await()
            ?.toObject<FirestoreUser>()
            ?.mapToUser()
    }

    override suspend fun searchTripMembers(email: String): Set<User> {
        val emailPropertyName = FirestoreUser::email.name

        return firestore.collection(USER_COLLECTION_NAME)
            .whereGreaterThanOrEqualTo(emailPropertyName, email)
            .whereLessThanOrEqualTo(emailPropertyName, "$email$UNICODE_RANGE")
            .get()
            .await()
            .documents.mapNotNull { document -> document.toObject<FirestoreUser>() }
            .map { firestoreUser -> firestoreUser.mapToUser() }
            .toSet()
    }
}
