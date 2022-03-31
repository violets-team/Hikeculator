package com.example.hikeculator.presentation.trip_details

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import com.example.hikeculator.R
import com.example.hikeculator.databinding.TripDetailButtonsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TripDetailButtonView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, defStyleAttr = 0)
    constructor(context: Context) : this(context, attrs = null)

    companion object {

        private const val LEFT_CONTENT_POSITION_DP = 10
        private const val CHILD_MARGIN = 8
        private const val TOP_PARENT_PADDING = 20

        private const val CLICK_DURATION: Long = 40

        private const val START_BUTTON_POSITION: Float = 0F

        private const val START_ANIMATION_LATENCY: Long = 500

        private const val MAIN_BUTTON_INITIAL_SIZE: Float = 0.85F
        private const val MAIN_BUTTON_SIZE_CHANGING_VALUE: Float = 0.15F

        private const val DISPLAY_BUTTON_SIZE_CHANGING_VALUE: Float = 1.5F

        private const val MAX_ALPHA: Float = 1F
        private const val MIN_ALPHA: Float = 0F

        private const val PROVISION_BAG_BUTTON_DISPLAYING_DURATION: Long = 600
        private const val PROVISION_BAG_BUTTON_HIDING_DURATION: Long = 700
        private const val PROVISION_BAG_BUTTON_TRANSLATION_VALUE: Float = 350F

        private const val MEMBER_BUTTON_DISPLAYING_DURATION: Long = 400
        private const val MEMBER_BUTTON_HIDING_DURATION: Long = 400
        private const val MEMBER_BUTTON_HIDING_TRANSLATION_VALUE: Float = 350F
    }

    private val binding = TripDetailButtonsBinding.inflate(LayoutInflater.from(context), this)

    private var onProvisionBagButtonClick: (() -> Unit)? = null
    private var onMemberButtonClick: (() -> Unit)? = null

    private var mainButtonIsPressed = true
    private var inAnimationProcess = false

    private val leftContentPosition = LEFT_CONTENT_POSITION_DP.toPixels()
    private val childMargin = CHILD_MARGIN.toPixels()
    private val topParentPadding = TOP_PARENT_PADDING.toPixels()
    private val contentHeight get() = children.map { it.measuredHeight + childMargin }.sum()

    private val FloatingActionButton.clickAnimator: ViewPropertyAnimator
        get() = this.animate().apply { duration = CLICK_DURATION }

    private val FloatingActionButton.displayAnimator: ViewPropertyAnimator
        get() = this.animate().apply {
            interpolator = AnticipateOvershootInterpolator()
            translationY(START_BUTTON_POSITION)
            increaseSize(DISPLAY_BUTTON_SIZE_CHANGING_VALUE)
            alpha(MAX_ALPHA)
        }

    private val FloatingActionButton.hidingAnimator: ViewPropertyAnimator
        get() = this.animate().apply {
            alpha(MIN_ALPHA)
            decreaseSize(DISPLAY_BUTTON_SIZE_CHANGING_VALUE)
        }

    init {
        orientation = VERTICAL
        setInitialMainButtonSize()

        Handler(Looper.getMainLooper())
            .postDelayed({ animateButtons() }, START_ANIMATION_LATENCY)

        binding.actionButtonMain.setOnClickListener {
            if (!inAnimationProcess) {
                animateButtons()
            }
        }
        binding.actionButtonProvisionBag.setOnClickListener { onProvisionBagButtonClick?.invoke() }
        binding.actionButtonMembers.setOnClickListener { onMemberButtonClick?.invoke() }

        setAttributes(attributes = attrs, defStyleAttr = defStyleAttr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val parentWidth = children.first().measuredWidth + childMargin * 2
        val parentHeight = contentHeight + topParentPadding

        setMeasuredDimension(
            resolveSize(parentWidth, widthMeasureSpec),
            resolveSize(parentHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var topPosition = bottom - top - contentHeight

        binding.actionButtonProvisionBag.apply {
            place(topPosition = topPosition)
            topPosition += measuredHeight + childMargin
        }
        binding.actionButtonMembers.apply {
            place(topPosition = topPosition)
            topPosition += measuredHeight + childMargin
        }

        binding.actionButtonMain.apply {
            place(topPosition = topPosition)
            topPosition += measuredHeight + childMargin
        }
    }

    fun setOnProvisionBagButtonClickListener(listener: () -> Unit) {
        onProvisionBagButtonClick = listener
    }

    fun setOnMemberButtonClickListener(listener: () -> Unit) {
        onMemberButtonClick = listener
    }

    private fun setAttributes(attributes: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(
            attributes,
            R.styleable.TripDetailButtonView,
            defStyleAttr,
            0
        )
        try {
            setMainButtonAttributes(typedArray = typedArray)
            setProvisionBagButtonAttributes(typedArray = typedArray)
            setMemberButtonAttributes(typedArray = typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setMainButtonAttributes(typedArray: TypedArray) {
        binding.actionButtonMain.setAttributes(
            typedArray = typedArray,
            backgroundId =  R.styleable.TripDetailButtonView_mainButtonBackground,
            imageId = R.styleable.TripDetailButtonView_mainButtonImage,
            tintId = R.styleable.TripDetailButtonView_mainButtonTint,
        )
    }

    private fun setProvisionBagButtonAttributes(typedArray: TypedArray) {
        binding.actionButtonProvisionBag.setAttributes(
            typedArray = typedArray,
            backgroundId =   R.styleable.TripDetailButtonView_provisionBagButtonBackground,
            imageId = R.styleable.TripDetailButtonView_provisionBagButtonImage,
            tintId = R.styleable.TripDetailButtonView_provisionBagButtonTint,
        )
    }

    private fun setMemberButtonAttributes(typedArray: TypedArray) {
        binding.actionButtonMembers.setAttributes(
            typedArray = typedArray,
            backgroundId =   R.styleable.TripDetailButtonView_memberButtonBackground,
            imageId = R.styleable.TripDetailButtonView_memberButtonImage,
            tintId = R.styleable.TripDetailButtonView_memberButtonTint,
        )
    }

    private fun FloatingActionButton.setAttributes(
        typedArray: TypedArray,
        @StyleableRes backgroundId: Int,
        @StyleableRes imageId: Int,
        @StyleableRes tintId: Int,
    ) {
        backgroundTintList = typedArray.getColorStateList(backgroundId)
        setImageDrawable(typedArray.getDrawable(imageId))
        imageTintList = typedArray.getColorStateList(tintId)
    }

    private fun Int.toPixels(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        resources.displayMetrics
    ).toInt()

    private fun setInitialMainButtonSize() {
        binding.actionButtonMain.scaleX = MAIN_BUTTON_INITIAL_SIZE
        binding.actionButtonMain.scaleY = MAIN_BUTTON_INITIAL_SIZE
    }

    private fun View.place(topPosition: Int) {
        layout(
            leftContentPosition,
            topPosition,
            leftContentPosition + measuredWidth,
            topPosition + measuredHeight
        )
    }

    private fun animateButtons() {
        inAnimationProcess = true

        if (mainButtonIsPressed) {
            hideProvisionBagButton()
            hideMemberButton()
        } else {
            displayProvisionBagButton()
            displayMemberButton()
        }
        animateMainButton()

        mainButtonIsPressed = !mainButtonIsPressed
    }

    private fun animateMainButton() {
        binding.actionButtonMain.apply {
            val value = MAIN_BUTTON_SIZE_CHANGING_VALUE * if (mainButtonIsPressed) 1 else -1

            clickAnimator.perform { increaseSize(value) }
                .withEndAction {
                    clickAnimator.perform { decreaseSize(value) }
                        .withEndAction {
                            clickAnimator.perform { increaseSize(value) }
                        }
                }
        }
    }

    private fun ViewPropertyAnimator.increaseSize(value: Float) {
        scaleXBy(value)
        scaleYBy(value)
    }

    private fun ViewPropertyAnimator.decreaseSize(value: Float) {
        scaleXBy(-value)
        scaleYBy(-value)
    }

    private fun displayProvisionBagButton() {
        binding.actionButtonProvisionBag.apply {
            isClickable = true
            displayAnimator.perform { duration = PROVISION_BAG_BUTTON_DISPLAYING_DURATION }
                .withEndAction { inAnimationProcess = false }
        }
    }

    private fun displayMemberButton() {
        binding.actionButtonMembers.apply {
            isClickable = true
            displayAnimator.perform { duration = MEMBER_BUTTON_DISPLAYING_DURATION }
        }
    }

    private fun hideProvisionBagButton() {
        binding.actionButtonProvisionBag.apply {
            isClickable = false

            hidingAnimator.perform {
                translationY(PROVISION_BAG_BUTTON_TRANSLATION_VALUE)
                duration = PROVISION_BAG_BUTTON_HIDING_DURATION
            }.withEndAction { inAnimationProcess = false }
        }
    }

    private fun hideMemberButton() {
        binding.actionButtonMembers.apply {
            isClickable = false

            hidingAnimator.perform {
                translationY(MEMBER_BUTTON_HIDING_TRANSLATION_VALUE)
                duration = MEMBER_BUTTON_HIDING_DURATION
            }
        }
    }

    private fun ViewPropertyAnimator.perform(
        block: (ViewPropertyAnimator.() -> Unit)? = null,
    ): ViewPropertyAnimator {
        return this.apply { block?.invoke(this) }
    }
}