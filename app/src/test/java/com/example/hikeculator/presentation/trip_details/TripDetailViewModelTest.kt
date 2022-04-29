package com.example.hikeculator.presentation.trip_details

import app.cash.turbine.test
import com.example.hikeculator.MainCoroutineRule
import com.example.hikeculator.domain.entities.TripDay
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class TripDetailViewModelTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Test
    fun `verify getting data`() {
        val emptyTrips = emptyList<TripDay>()

        val viewModel = TripDetailViewModel(
            tripInteractor = mockk {
                every { fetchTrip(any()) } returns flow { emit(value = null) }
            },
            tripDayInteractor = mockk {
                every { fetchTripDays(any()) } returns flow { emit(emptyTrips) }
            },
            tripId = "tripId"
        )

        runBlocking {
            viewModel.data.test {
                val data = awaitItem()
                assertNull(data.first)
                assertEquals(emptyTrips, data.second)
            }
        }
    }

    @Test
    fun `getting problem message id when getting trip is failed`() {
        val viewModel = TripDetailViewModel(
            tripInteractor = mockk {
                every { fetchTrip(any()) } returns flow { throw Exception() }
            },
            tripDayInteractor = mockk {
                every { fetchTripDays(any()) } returns flow {}
            },
            tripId = "tripId"
        )

        runBlocking {
            viewModel.problemMessage.test() { assertNotNull(awaitItem()) }
        }
    }

    @Test
    fun `getting problem message id when getting trip days is failed`() {
        val viewModel = TripDetailViewModel(
            tripInteractor = mockk {
                every { fetchTrip(any()) } returns flow {}
            },
            tripDayInteractor = mockk {
                every { fetchTripDays(any()) } returns flow { throw Exception() }
            },
            tripId = "tripId"
        )

        runBlocking {
            viewModel.problemMessage.test { assertNotNull(awaitItem()) }
        }
    }
}