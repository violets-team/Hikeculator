package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.data.common.USER_COLLECTION_NAME
import com.example.hikeculator.data.common.mapToUser
import com.example.hikeculator.data.entities.FirestoreUser
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.repositories.MemberGroupRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MemberGroupRepositoryImpl(private val firestore: FirebaseFirestore) : MemberGroupRepository {

    companion object {

        private const val UNICODE_RANGE = "\uf8ff"
    }

    override fun addTripMember(user: User) {
        TODO("Not yet implemented")
    }

    override fun removeTripMember(user: User) {
        TODO("Not yet implemented")
    }

    override fun fetchTripMembers(): Flow<Set<User>> = flow {
        TODO("Not yet implemented")
    }

    override fun fetchTripMember(): User {
        TODO("Not yet implemented")
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
