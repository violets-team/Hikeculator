package com.example.hikeculator

import android.app.Application
import androidx.work.WorkManager
import com.example.hikeculator.di.dataModule
import com.example.hikeculator.di.domainModule
import com.example.hikeculator.di.presentationModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider

@ExperimentalCoroutinesApi
class DiTest : KoinTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Test
    fun `verify DI`() {
        MockProvider.register { mockkClass(it) }

        val mockContext = mockkClass(Application::class)

        mockkStatic(FirebaseFirestore::class)
        mockkStatic(FirebaseAuth::class)
        mockkStatic(WorkManager::class)

        every { FirebaseFirestore.getInstance() } returns mockk(relaxed = true)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { WorkManager.getInstance(any()) } returns mockk(relaxed = true)

        koinApplication {
            androidContext(mockContext)
            modules(dataModule, domainModule, presentationModule)
        }.checkModules()
    }
}