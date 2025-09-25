package com.example.superphoto.ui.components

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.superphoto.R
import kotlinx.coroutines.*

/**
 * Enhanced Loading Manager với animations và progress tracking
 * Quản lý loading states cho AI processing tasks
 */
class EnhancedLoadingManager(
    private val context: Context,
    private val container: ViewGroup
) {
    
    private var loadingView: View? = null
    private var currentJob: Job? = null
    private var onCancelListener: (() -> Unit)? = null
    
    // Loading components
    private var loadingTitle: TextView? = null
    private var loadingMessage: TextView? = null
    private var loadingIcon: TextView? = null
    private var outerProgressBar: ProgressBar? = null
    private var innerProgressBar: ProgressBar? = null
    private var estimatedTimeText: TextView? = null
    private var cancelButton: Button? = null
    
    // Step indicators
    private var step1Indicator: View? = null
    private var step1Text: TextView? = null
    private var step1Status: TextView? = null
    private var step2Indicator: View? = null
    private var step2Text: TextView? = null
    private var step2Status: TextView? = null
    private var step3Indicator: View? = null
    private var step3Text: TextView? = null
    private var step3Status: TextView? = null
    
    private var currentStep = 0
    private var isShowing = false
    
    /**
     * Hiển thị loading với configuration tùy chỉnh
     */
    fun show(config: LoadingConfig) {
        if (isShowing) return
        
        isShowing = true
        currentStep = 0
        
        // Inflate loading layout
        loadingView = LayoutInflater.from(context)
            .inflate(R.layout.component_enhanced_loading, container, false)
        
        initializeViews()
        setupConfiguration(config)
        setupClickListeners()
        
        // Add to container with animation
        container.addView(loadingView)
        animateIn()
        
        // Start progress simulation
        startProgressAnimation(config)
    }
    
    /**
     * Ẩn loading với animation
     */
    fun hide() {
        if (!isShowing) return
        
        currentJob?.cancel()
        animateOut {
            container.removeView(loadingView)
            loadingView = null
            isShowing = false
        }
    }
    
    /**
     * Cập nhật step hiện tại
     */
    fun updateStep(step: Int, message: String? = null) {
        if (!isShowing || step < 1 || step > 3) return
        
        currentStep = step
        
        // Update step indicators
        updateStepIndicator(1, step >= 1)
        updateStepIndicator(2, step >= 2)
        updateStepIndicator(3, step >= 3)
        
        // Update message if provided
        message?.let {
            loadingMessage?.text = it
        }
        
        // Animate icon change based on step
        animateIconForStep(step)
    }
    
    /**
     * Set cancel listener
     */
    fun setOnCancelListener(listener: () -> Unit) {
        onCancelListener = listener
    }
    
    private fun initializeViews() {
        loadingView?.let { view ->
            loadingTitle = view.findViewById(R.id.loadingTitle)
            loadingMessage = view.findViewById(R.id.loadingMessage)
            loadingIcon = view.findViewById(R.id.loadingIcon)
            outerProgressBar = view.findViewById(R.id.outerProgressBar)
            innerProgressBar = view.findViewById(R.id.innerProgressBar)
            estimatedTimeText = view.findViewById(R.id.estimatedTimeText)
            cancelButton = view.findViewById(R.id.cancelButton)
            
            // Step indicators
            step1Indicator = view.findViewById(R.id.step1Indicator)
            step1Text = view.findViewById(R.id.step1Text)
            step1Status = view.findViewById(R.id.step1Status)
            step2Indicator = view.findViewById(R.id.step2Indicator)
            step2Text = view.findViewById(R.id.step2Text)
            step2Status = view.findViewById(R.id.step2Status)
            step3Indicator = view.findViewById(R.id.step3Indicator)
            step3Text = view.findViewById(R.id.step3Text)
            step3Status = view.findViewById(R.id.step3Status)
        }
    }
    
    private fun setupConfiguration(config: LoadingConfig) {
        loadingTitle?.text = config.title
        loadingMessage?.text = config.message
        loadingIcon?.text = config.icon
        estimatedTimeText?.text = "⏱️ Estimated time: ${config.estimatedTime}"
        
        // Setup step texts
        step1Text?.text = config.step1Text
        step2Text?.text = config.step2Text
        step3Text?.text = config.step3Text
        
        // Show/hide cancel button
        cancelButton?.visibility = if (config.showCancel) View.VISIBLE else View.GONE
    }
    
    private fun setupClickListeners() {
        cancelButton?.setOnClickListener {
            onCancelListener?.invoke()
            hide()
        }
    }
    
    private fun animateIn() {
        loadingView?.let { view ->
            view.alpha = 0f
            view.scaleX = 0.8f
            view.scaleY = 0.8f
            
            view.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }
    
    private fun animateOut(onComplete: () -> Unit) {
        loadingView?.animate()
            ?.alpha(0f)
            ?.scaleX(0.8f)
            ?.scaleY(0.8f)
            ?.setDuration(200)
            ?.setInterpolator(AccelerateDecelerateInterpolator())
            ?.withEndAction(onComplete)
            ?.start()
    }
    
    private fun startProgressAnimation(config: LoadingConfig) {
        currentJob = CoroutineScope(Dispatchers.Main).launch {
            // Simulate progress steps
            delay(1000)
            updateStep(1, "Uploading image...")
            
            delay(2000)
            updateStep(2, "AI analyzing image...")
            
            delay(3000)
            updateStep(3, "Generating result...")
        }
    }
    
    private fun updateStepIndicator(stepNumber: Int, isActive: Boolean) {
        val indicator = when (stepNumber) {
            1 -> step1Indicator
            2 -> step2Indicator
            3 -> step3Indicator
            else -> return
        }
        
        val textView = when (stepNumber) {
            1 -> step1Text
            2 -> step2Text
            3 -> step3Text
            else -> return
        }
        
        val statusView = when (stepNumber) {
            1 -> step1Status
            2 -> step2Status
            3 -> step3Status
            else -> return
        }
        
        if (isActive) {
            indicator?.background = ContextCompat.getDrawable(context, R.drawable.step_indicator_active)
            textView?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            
            if (stepNumber < currentStep) {
                statusView?.visibility = View.VISIBLE
            }
        } else {
            indicator?.background = ContextCompat.getDrawable(context, R.drawable.step_indicator_inactive)
            textView?.setTextColor(ContextCompat.getColor(context, R.color.text_tertiary))
            statusView?.visibility = View.GONE
        }
    }
    
    private fun animateIconForStep(step: Int) {
        val newIcon = when (step) {
            1 -> "📤"
            2 -> "🧠"
            3 -> "✨"
            else -> "🤖"
        }
        
        loadingIcon?.let { icon ->
            // Scale down
            icon.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .setDuration(150)
                .withEndAction {
                    // Change icon and scale up
                    icon.text = newIcon
                    icon.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()
        }
    }
    
    /**
     * Configuration class cho loading
     */
    data class LoadingConfig(
        val title: String = "AI Processing",
        val message: String = "Analyzing your image with advanced AI...",
        val icon: String = "🤖",
        val estimatedTime: String = "15-30 seconds",
        val showCancel: Boolean = true,
        val step1Text: String = "📤 Uploading image...",
        val step2Text: String = "🧠 AI Analysis...",
        val step3Text: String = "✨ Generating result..."
    )
    
    companion object {
        /**
         * Tạo config cho background removal
         */
        fun backgroundRemovalConfig() = LoadingConfig(
            title = "Background Removal",
            message = "AI is removing background from your image...",
            icon = "🖼️",
            step2Text = "🧠 Detecting objects...",
            step3Text = "✂️ Removing background..."
        )
        
        /**
         * Tạo config cho face swap
         */
        fun faceSwapConfig() = LoadingConfig(
            title = "Face Swap",
            message = "AI is swapping faces in your image...",
            icon = "👤",
            step2Text = "🧠 Detecting faces...",
            step3Text = "🔄 Swapping faces..."
        )
        
        /**
         * Tạo config cho AI enhance
         */
        fun aiEnhanceConfig() = LoadingConfig(
            title = "AI Enhancement",
            message = "AI is enhancing your image quality...",
            icon = "✨",
            step2Text = "🧠 Analyzing quality...",
            step3Text = "⚡ Enhancing image..."
        )
        
        /**
         * Tạo config cho colorize
         */
        fun colorizeConfig() = LoadingConfig(
            title = "AI Colorization",
            message = "AI is adding colors to your image...",
            icon = "🎨",
            step2Text = "🧠 Analyzing content...",
            step3Text = "🌈 Adding colors..."
        )
        
        /**
         * Tạo config cho object removal
         */
        fun objectRemovalConfig() = LoadingConfig(
            title = "Object Removal",
            message = "AI is removing objects from your image...",
            icon = "🗑️",
            step2Text = "🧠 Detecting objects...",
            step3Text = "🔧 Removing objects..."
        )
        
        /**
         * Tạo config cho smart suggestions
         */
        fun smartSuggestionsConfig() = LoadingConfig(
            title = "Smart Analysis",
            message = "AI is analyzing your image for suggestions...",
            icon = "💡",
            estimatedTime = "5-10 seconds",
            step2Text = "🧠 Analyzing content...",
            step3Text = "💡 Generating suggestions..."
        )
    }
}