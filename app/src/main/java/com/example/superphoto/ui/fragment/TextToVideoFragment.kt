package com.example.superphoto.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.superphoto.R

class TextToVideoFragment : Fragment() {

    // UI Elements
    private lateinit var promptEditText: EditText
    private lateinit var negativePromptEditText: EditText
    private lateinit var refreshButton: ImageView
    private lateinit var hint1: TextView
    private lateinit var hint2: TextView
    private lateinit var hint3: TextView
    private lateinit var duration10s: TextView
    private lateinit var duration15s: TextView
    private lateinit var duration20s: TextView
    private lateinit var generateButton: Button
    
    // State variables
    private var selectedDuration = 10 // 10, 15, or 20 seconds
    
    // Hint options
    private val hintOptions = listOf(
        listOf("Dancing on the Street", "Volcano by the Sea", "Surfing on the sea"),
        listOf("City at Night", "Forest Adventure", "Ocean Waves"),
        listOf("Mountain Climbing", "Desert Journey", "Space Travel"),
        listOf("Underwater World", "Flying Birds", "Rainy Day"),
        listOf("Sunset Beach", "Winter Snow", "Spring Garden")
    )
    private var currentHintIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_to_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupClickListeners()
        updateDurationSelection()
    }

    private fun initViews(view: View) {
        promptEditText = view.findViewById(R.id.promptEditText)
        negativePromptEditText = view.findViewById(R.id.negativePromptEditText)
        refreshButton = view.findViewById(R.id.refreshButton)
        hint1 = view.findViewById(R.id.hint1)
        hint2 = view.findViewById(R.id.hint2)
        hint3 = view.findViewById(R.id.hint3)
        duration10s = view.findViewById(R.id.duration10s)
        duration15s = view.findViewById(R.id.duration15s)
        duration20s = view.findViewById(R.id.duration20s)
        generateButton = view.findViewById(R.id.generateButton)
    }

    private fun setupClickListeners() {
        // Refresh button click
        refreshButton.setOnClickListener {
            animateRotation(refreshButton)
            refreshHints()
        }

        // Hint clicks
        hint1.setOnClickListener {
            animateClick(hint1)
            applyHint(hint1.text.toString())
        }

        hint2.setOnClickListener {
            animateClick(hint2)
            applyHint(hint2.text.toString())
        }

        hint3.setOnClickListener {
            animateClick(hint3)
            applyHint(hint3.text.toString())
        }

        // Duration selection
        duration10s.setOnClickListener {
            animateClick(duration10s)
            selectDuration(10)
        }

        duration15s.setOnClickListener {
            animateClick(duration15s)
            selectDuration(15)
        }

        duration20s.setOnClickListener {
            animateClick(duration20s)
            selectDuration(20)
        }

        // Generate button click
        generateButton.setOnClickListener {
            animateClick(generateButton)
            generateVideo()
        }
    }

    private fun selectDuration(duration: Int) {
        selectedDuration = duration
        updateDurationSelection()
    }

    private fun updateDurationSelection() {
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.ai_text_primary)
        val secondaryColor = ContextCompat.getColor(requireContext(), R.color.ai_text_secondary)
        val selectedBackground = ContextCompat.getDrawable(requireContext(), R.drawable.ai_duration_button)
        val unselectedBackground = ContextCompat.getDrawable(requireContext(), R.drawable.ai_duration_button_unselected)

        // Reset all durations
        duration10s.setTextColor(secondaryColor)
        duration15s.setTextColor(secondaryColor)
        duration20s.setTextColor(secondaryColor)
        duration10s.background = unselectedBackground
        duration15s.background = unselectedBackground
        duration20s.background = unselectedBackground

        // Highlight selected duration
        when (selectedDuration) {
            10 -> {
                duration10s.setTextColor(primaryColor)
                duration10s.background = selectedBackground
            }
            15 -> {
                duration15s.setTextColor(primaryColor)
                duration15s.background = selectedBackground
            }
            20 -> {
                duration20s.setTextColor(primaryColor)
                duration20s.background = selectedBackground
            }
        }
    }

    private fun applyHint(hintText: String) {
        val currentText = promptEditText.text.toString()
        if (currentText.isEmpty()) {
            promptEditText.setText(hintText)
        } else {
            promptEditText.setText("$currentText, $hintText")
        }
        promptEditText.setSelection(promptEditText.text.length)
    }

    private fun refreshHints() {
        currentHintIndex = (currentHintIndex + 1) % hintOptions.size
        val newHints = hintOptions[currentHintIndex]
        
        hint1.text = newHints[0]
        hint2.text = newHints[1]
        hint3.text = newHints[2]
        
        Toast.makeText(requireContext(), "Hints refreshed!", Toast.LENGTH_SHORT).show()
    }

    private fun generateVideo() {
        val prompt = promptEditText.text.toString().trim()
        val negativePrompt = negativePromptEditText.text.toString().trim()
        
        if (prompt.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a prompt", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Generating ${selectedDuration}s video from text...",
            Toast.LENGTH_LONG
        ).show()

        // TODO: Implement actual video generation logic
        // This would typically involve:
        // 1. Sending the prompt and negative prompt to AI service
        // 2. Processing the request with selected duration
        // 3. Showing progress dialog
        // 4. Handling the generated video result
    }

    private fun animateClick(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 150
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }

    private fun animateRotation(view: View) {
        val rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotation.duration = 500
        rotation.interpolator = AccelerateDecelerateInterpolator()
        rotation.start()
    }

    companion object {
        fun newInstance(): TextToVideoFragment {
            return TextToVideoFragment()
        }
    }
}