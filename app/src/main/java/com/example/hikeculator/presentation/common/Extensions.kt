package com.example.hikeculator.presentation.common

import android.content.Context
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.domain.entities.NutritionalValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T> Flow<T>.launchWhenCreated(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenCreated { this@launchWhenCreated.collect() }
}

fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted { this@launchWhenStarted.collect() }
}

fun <T> Flow<T>.launchWhenResumed(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenResumed { this@launchWhenResumed.collect() }
}

inline fun <T> Flow<T>.collectWhenStarted(
    lifecycleScope: LifecycleCoroutineScope,
    crossinline onEach: (T) -> Unit,
) {
    lifecycleScope.launchWhenStarted { this@collectWhenStarted.collect { onEach(it) } }
}

fun EditText.toTrimmed(): String = text.toString().trim()

fun Context.showToast(@StringRes messageId: Int) {
    Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
}

fun RecyclerView.getAnimated(@AnimRes layoutAnimationId: Int) {
    val layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, layoutAnimationId)

    layoutAnimation = layoutAnimationController
}



