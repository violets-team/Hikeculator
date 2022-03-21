package com.example.hikeculator.data.common

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.getTripSubCollection(userUid: String): CollectionReference {
    return collection(GENERAL_TRIP_COLLECTION_NAME)
        .document(userUid)
        .collection(GENERAL_TRIP_SUB_COLLECTION_NAME)
}

fun FirebaseFirestore.getTripDocument(userUid: String, tripId: String): DocumentReference {
    return getTripSubCollection(userUid = userUid)
        .document(tripId)
}

fun FirebaseFirestore.getTripDayCollection(userUid: String, tripId: String): CollectionReference {
    return getTripDocument(userUid = userUid, tripId = tripId)
        .collection(TRIP_DAY_COLLECTION_NAME)
}

fun FirebaseFirestore.getProvisionBagCollection(userUid: String, tripId: String): CollectionReference {
    return getTripDocument(userUid = userUid, tripId = tripId)
        .collection(TRIP_PROVISION_BAG_COLLECTION_NAME)
}

fun FirebaseFirestore.getProvisionBagDocument(userUid: String, tripId: String): DocumentReference {
    return getProvisionBagCollection(userUid = userUid, tripId = tripId)
        .document()
}