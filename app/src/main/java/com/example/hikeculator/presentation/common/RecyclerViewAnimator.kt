package com.example.hikeculator.presentation.common

import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAnimator(
    private val recyclerView: RecyclerView,
) {

    var wasNotAnimated = true
        private set

    fun animateOnlyOnce() {
        if (wasNotAnimated) {
            recyclerView.scheduleLayoutAnimation()
            wasNotAnimated = false
        }
    }

    fun reset() {
        wasNotAnimated = true
    }
}