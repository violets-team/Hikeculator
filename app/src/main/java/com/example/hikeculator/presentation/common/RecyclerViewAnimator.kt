package com.example.hikeculator.presentation.common

import androidx.annotation.AnimRes
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAnimator(private val recyclerView: RecyclerView) {

    private var wasNotAnimated = true

    fun animateOnlyOnce(@AnimRes layoutAnimationId: Int) {
        if (wasNotAnimated) {
            recyclerView.getAnimated(layoutAnimationId = layoutAnimationId)
            wasNotAnimated = false
        }
    }
}