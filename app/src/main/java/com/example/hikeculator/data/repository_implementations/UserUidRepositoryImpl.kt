package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.domain.repositories.UserUidRepository

class UserUidRepositoryImpl : UserUidRepository {

    companion object {

        private const val EMPTY_UID = ""
    }

    override var uid: String = EMPTY_UID
}