package com.example.hikeculator.presentation.general_trip_list

import android.util.Log
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.entities.User
import com.example.hikeculator.domain.enums.TripDifficultyCategory
import com.example.hikeculator.domain.enums.TripSeason
import com.example.hikeculator.domain.enums.TripType
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

import org.junit.rules.TestWatcher
import org.junit.runner.Description

//@get:Rule
//val coroutineRule = GeneralTripViewModelTest.MainCoroutineRule()
@ExperimentalCoroutinesApi
class GeneralTripViewModelTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    val dispatcher = TestCoroutineDispatcher()

    private val tripInteractor: TripInteractor = mockk(relaxUnitFun = true)
    private val tripDayInteractor: TripDayInteractor = mockk(relaxUnitFun = true)
    private val provisionBagInteractor: ProvisionBagInteractor = mockk(relaxUnitFun = true)
    private val userProfileInteractor: UserProfileInteractor = mockk() {
        every { fetchObservableUserProfile() } returns flow { emit(null) }
    }

    private lateinit var viewModel: GeneralTripViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        viewModel = GeneralTripViewModel(
            tripInteractor = tripInteractor,
            tripDayInteractor = tripDayInteractor,
            provisionBagInteractor = provisionBagInteractor,
            userProfileInteractor = userProfileInteractor
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
//        dispatcher.close()
    }

    @Test
    fun `invoke deleting day collection and provision bag and trip on deleting trip`() =
        runBlocking {

            val tripId = "some id"

            val trip = mockk<Trip> { every { id } returns tripId }

            viewModel.deleteTrip(trip = trip)


            coVerifyAll() {
                tripDayInteractor.removeTripDayCollection(tripId = tripId)
                provisionBagInteractor.removeProvisionBag(tripId = tripId)
                tripInteractor.removeTrip(trip = trip)
            }
        }

}

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}