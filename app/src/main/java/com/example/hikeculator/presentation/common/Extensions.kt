package com.example.hikeculator.presentation.common

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

private const val GREATEST_PERCENTAGE_VALUE = 999

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

fun Context.hideKeyBoardIfOpen(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun RecyclerView.getAnimated(@AnimRes layoutAnimationId: Int) {
    val layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, layoutAnimationId)

    layoutAnimation = layoutAnimationController
}


fun TextView.setTextPercentage(percentage: Int) {
    val displayedPercentage = if (percentage < GREATEST_PERCENTAGE_VALUE) {
        percentage
    } else {
        GREATEST_PERCENTAGE_VALUE
    }

    text = context.getString(R.string.pfc_percentage, displayedPercentage)
}

