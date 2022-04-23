package com.example.hikeculator.presentation.general_trip_list

import app.cash.turbine.test
import com.example.hikeculator.MainCoroutineRule
import com.example.hikeculator.domain.entities.Trip
import com.example.hikeculator.domain.interactors.ProvisionBagInteractor
import com.example.hikeculator.domain.interactors.TripDayInteractor
import com.example.hikeculator.domain.interactors.TripInteractor
import com.example.hikeculator.domain.interactors.UserProfileInteractor
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GeneralTripViewModelTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

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
        viewModel = GeneralTripViewModel(
            tripInteractor = tripInteractor,
            tripDayInteractor = tripDayInteractor,
            provisionBagInteractor = provisionBagInteractor,
            userProfileInteractor = userProfileInteractor
        )
    }

    @Test
    fun `invoke deleting day collection, provision bag and trip on deleting trip`() =
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

    @Test
    fun `getting message when deleting trip was failed`() {
        runBlocking {
            val trip = mockk<Trip>()

            coEvery { tripDayInteractor.removeTripDayCollection(any()) } throws Exception()
            coEvery { provisionBagInteractor.removeProvisionBag(tripId = any()) } throws Exception()
            coEvery { tripDayInteractor.removeTripDayCollection(tripId = any()) } throws Exception()

            viewModel.problemMessage.test {
                viewModel.deleteTrip(trip = trip)
                assertNotNull(awaitItem())
            }
        }
    }
}