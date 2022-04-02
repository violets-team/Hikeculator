package com.example.hikeculator.data.common

import com.example.hikeculator.data.fiebase.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.getTripSubCollection(): CollectionReference {
    return collection(GENERAL_TRIP_COLLECTION_NAME)
}

fun FirebaseFirestore.getTripDocument(tripId: String): DocumentReference {
    return getTripSubCollection()
        .document(tripId)
}

fun FirebaseFirestore.getTripDayCollection(tripId: String): CollectionReference {
    return getTripDocument(tripId = tripId)
        .collection(TRIP_DAY_COLLECTION_NAME)
}

fun FirebaseFirestore.getProvisionBagCollection(tripId: String): CollectionReference {
    return getTripDocument(tripId = tripId)
        .collection(TRIP_PROVISION_BAG_COLLECTION_NAME)
}

fun FirebaseFirestore.getProvisionBagDocument(tripId: String): DocumentReference {
    return getProvisionBagCollection(tripId = tripId)
        .document(TRIP_PROVISION_BAG_DOCUMENT_NAME)
}

fun FirebaseFirestore.getUserDocument(userUid: String): DocumentReference {
    return collection(USER_COLLECTION_NAME)
        .document(userUid)
}