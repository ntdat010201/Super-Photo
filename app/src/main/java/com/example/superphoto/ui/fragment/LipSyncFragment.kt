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
import com.example.superphoto.R

class LipSyncFragment : Fragment() {

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
        
        generateButton.isEnabled = hasVideo && hasAudio
        
        if (hasVideo && hasAudio) {
            generateButton.alpha = 1.0f
        } else {
            generateButton.alpha = 0.5f
        }
    }

    private fun generateLipSync() {
        if (selectedVideoUri == null || selectedAudioUri == null) {
            Toast.makeText(requireContext(), "Please select both video and audio files", Toast.LENGTH_SHORT).show()
            return
        }

        val enhanceQuality = enhanceQualityCheckbox.isChecked
        val preserveExpression = preserveExpressionCheckbox.isChecked
        
        Toast.makeText(
            requireContext(),
            "Generating lip sync video with selected options...",
            Toast.LENGTH_LONG
        ).show()

        // TODO: Implement actual lip sync generation logic
        // This would typically involve:
        // 1. Uploading video and audio files to server
        // 2. Processing lip sync with selected options
        // 3. Showing progress dialog
        // 4. Handling the generated video result
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