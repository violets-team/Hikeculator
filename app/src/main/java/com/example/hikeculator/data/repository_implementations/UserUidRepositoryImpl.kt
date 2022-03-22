package com.example.hikeculator.data.repository_implementations

import com.example.hikeculator.domain.repositories.UserUidRepositiory

class UserUidRepositoryImpl : UserUidRepositiory {

    companion object {

        private const val EMPTY_UID = ""
    }

    override var uid: String = EMPTY_UID

}