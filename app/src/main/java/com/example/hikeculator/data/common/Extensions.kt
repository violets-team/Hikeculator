package com.example.hikeculator.data.common

import com.example.hikeculator.data.fiebase.GENERAL_TRIP_COLLECTION_NAME
import com.example.hikeculator.data.fiebase.TRIP_DAY_COLLECTION_NAME
import com.example.hikeculator.data.fiebase.TRIP_PROVISION_BAG_COLLECTION_NAME
import com.example.hikeculator.data.fiebase.USER_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
        .document()
}

fun FirebaseFirestore.getUserDocument(userUid: String): DocumentReference {
    return collection(USER_COLLECTION_NAME)
        .document(userUid)
}

fun FirebaseFirestore.getTripDayDocument(tripId: String, tripDayId: String): DocumentReference {
    return getTripDayCollection(tripId).document(tripDayId)
}