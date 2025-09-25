package com.example.superphoto.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.superphoto.R
import com.example.superphoto.data.repository.AIGenerationRepository
import com.example.superphoto.utils.GenerationStatusManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LipSyncFragment : Fragment() {

    // Dependency injection
    private val aiGenerationRepository: AIGenerationRepository by inject()
    private lateinit var statusManager: GenerationStatusManager

    // UI Elements
    private lateinit var videoUploadArea: LinearLayout
    private lateinit var audioUploadArea: LinearLayout
    private lateinit var selectedVideoContainer: LinearLayout
    private lateinit var selectedAudioContainer: LinearLayout
    private lateinit var selectedVideoName: TextView
    private lateinit var selectedVideoSize: TextView
    private lateinit var selectedAudioName: TextView
    private lateinit var selectedAudioSize: TextView
    private lateinit var removeVideoButton: ImageView
    private lateinit var removeAudioButton: ImageView
    private lateinit var enhanceQualityCheckbox: CheckBox
    private lateinit var preserveExpressionCheckbox: CheckBox
    private lateinit var generateButton: Button
    
    // State variables
    private var selectedVideoUri: Uri? = null
    private var selectedAudioUri: Uri? = null
    private var isGenerating = false
    
    // File picker launchers
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedVideoUri = uri
                updateSelectedVideo(uri)
            }
        }
    }
    
    private val audioPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedAudioUri = uri
                updateSelectedAudio(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lip_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusManager = GenerationStatusManager(requireContext(), aiGenerationRepository, lifecycleScope)
        initViews(view)
        setupClickListeners()
        updateGenerateButtonState()
    }

    private fun initViews(view: View) {
        videoUploadArea = view.findViewById(R.id.videoUploadArea)
        audioUploadArea = view.findViewById(R.id.audioUploadArea)
        selectedVideoContainer = view.findViewById(R.id.selectedVideoContainer)
        selectedAudioContainer = view.findViewById(R.id.selectedAudioContainer)
        selectedVideoName = view.findViewById(R.id.selectedVideoName)
        selectedVideoSize = view.findViewById(R.id.selectedVideoSize)
        selectedAudioName = view.findViewById(R.id.selectedAudioName)
        selectedAudioSize = view.findViewById(R.id.selectedAudioSize)
        removeVideoButton = view.findViewById(R.id.removeVideoButton)
        removeAudioButton = view.findViewById(R.id.removeAudioButton)
        enhanceQualityCheckbox = view.findViewById(R.id.enhanceQualityCheckbox)
        preserveExpressionCheckbox = view.findViewById(R.id.preserveExpressionCheckbox)
        generateButton = view.findViewById(R.id.generateButton)
    }

    private fun setupClickListeners() {
        // Video upload area click
        videoUploadArea.setOnClickListener {
            animateClick(videoUploadArea)
            openVideoPicker()
        }

        // Audio upload area click
        audioUploadArea.setOnClickListener {
            animateClick(audioUploadArea)
            openAudioPicker()
        }

        // Remove video button
        removeVideoButton.setOnClickListener {
            animateClick(removeVideoButton)
            removeSelectedVideo()
        }

        // Remove audio button
        removeAudioButton.setOnClickListener {
            animateClick(removeAudioButton)
            removeSelectedAudio()
        }

        // Generate button click
        generateButton.setOnClickListener {
            animateClick(generateButton)
            generateLipSync()
        }

        // Checkbox listeners
        enhanceQualityCheckbox.setOnCheckedChangeListener { _, _ ->
            updateGenerateButtonState()
        }

        preserveExpressionCheckbox.setOnCheckedChangeListener { _, _ ->
            updateGenerateButtonState()
        }
    }

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        videoPickerLauncher.launch(intent)
    }

    private fun openAudioPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        audioPickerLauncher.launch(intent)
    }

    private fun updateSelectedVideo(uri: Uri) {
        selectedVideoContainer.visibility = View.VISIBLE
        
        // Get file name from URI
        val fileName = getFileName(uri) ?: "video.mp4"
        selectedVideoName.text = fileName
        
        // Get file size (simplified)
        selectedVideoSize.text = "Video file"
        
        updateGenerateButtonState()
    }

    private fun updateSelectedAudio(uri: Uri) {
        selectedAudioContainer.visibility = View.VISIBLE
        
        // Get file name from URI
        val fileName = getFileName(uri) ?: "audio.mp3"
        selectedAudioName.text = fileName
        
        // Get file size (simplified)
        selectedAudioSize.text = "Audio file"
        
        updateGenerateButtonState()
    }

    private fun removeSelectedVideo() {
        selectedVideoUri = null
        selectedVideoContainer.visibility = View.GONE
        updateGenerateButtonState()
    }

    private fun removeSelectedAudio() {
        selectedAudioUri = null
        selectedAudioContainer.visibility = View.GONE
        updateGenerateButtonState()
    }

    private fun updateGenerateButtonState() {
        val hasVideo = selectedVideoUri != null
        val hasAudio = selectedAudioUri != null
        val canGenerate = hasVideo && hasAudio && !isGenerating
        
        generateButton.isEnabled = canGenerate
        
        if (canGenerate) {
            generateButton.alpha = 1.0f
            generateButton.text = "Generate Lip Sync"
        } else if (isGenerating) {
            generateButton.alpha = 0.7f
            generateButton.text = "Generating..."
        } else {
            generateButton.alpha = 0.5f
            generateButton.text = "Generate Lip Sync"
        }
    }

    private fun generateLipSync() {
        if (selectedVideoUri == null || selectedAudioUri == null) {
            Toast.makeText(requireContext(), "Please select both video and audio files", Toast.LENGTH_SHORT).show()
            return
        }

        if (isGenerating) {
            Toast.makeText(requireContext(), "Generation in progress, please wait...", Toast.LENGTH_SHORT).show()
            return
        }

        val enhanceQuality = enhanceQualityCheckbox.isChecked
        val preserveExpression = preserveExpressionCheckbox.isChecked
        
        isGenerating = true
        updateGenerateButtonState()
        
        Toast.makeText(
            requireContext(),
            "Starting lip sync generation...",
            Toast.LENGTH_SHORT
        ).show()

        lifecycleScope.launch {
            try {
                val result = aiGenerationRepository.generateLipSync(
                    videoUri = selectedVideoUri!!,
                    audioUri = selectedAudioUri!!,
                    enhanceQuality = enhanceQuality,
                    preserveExpression = preserveExpression
                )

                if (result.isSuccess) {
                    val taskId = result.getOrNull()!!.taskId
                    statusManager.startStatusPolling(taskId, object : GenerationStatusManager.StatusCallback {
                            override fun onProgress(progress: Int, message: String) {
                                Toast.makeText(requireContext(), 
                                    "Lip sync progress: $progress% - $message", 
                                    Toast.LENGTH_SHORT).show()
                            }
                            
                            override fun onCompleted(resultUri: Uri?) {
                                isGenerating = false
                                updateGenerateButtonState()
                                
                                if (resultUri != null) {
                                    Toast.makeText(requireContext(), 
                                        "Lip sync video generated successfully! Saved to gallery.", 
                                        Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), 
                                        "Lip sync generation completed but no result available.", 
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                            
                            override fun onFailed(error: String) {
                                isGenerating = false
                                updateGenerateButtonState()
                                Toast.makeText(requireContext(), 
                                    "Lip sync generation failed: $error", 
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                    
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    Toast.makeText(
                        requireContext(),
                        "Failed to start lip sync generation: $error",
                        Toast.LENGTH_LONG
                    ).show()
                    isGenerating = false
                    updateGenerateButtonState()
                }
            } catch (e: Exception) {
                isGenerating = false
                updateGenerateButtonState()
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
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

    companion object {
        fun newInstance(): LipSyncFragment {
            return LipSyncFragment()
        }
    }
}