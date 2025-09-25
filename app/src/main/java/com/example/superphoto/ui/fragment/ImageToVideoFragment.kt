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
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.adapter.SelectedImageAdapter
import com.example.superphoto.data.repository.AIGenerationRepository
import com.example.superphoto.data.model.VideoDuration
import com.example.superphoto.utils.GenerationStatusManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ImageToVideoFragment : Fragment() {

    // Dependency injection
    private val aiGenerationRepository: AIGenerationRepository by inject()
    private lateinit var statusManager: GenerationStatusManager

    // UI Elements
    private lateinit var uploadArea: LinearLayout
    private lateinit var refreshButton: ImageView
    private lateinit var promptEditText: EditText
    private lateinit var negativePromptEditText: EditText
    private lateinit var duration10s: TextView
    private lateinit var duration15s: TextView
    private lateinit var duration20s: TextView
    private lateinit var generateButton: Button
    
    // Hint chips - removed as they don't exist in image_to_video layout
    
    // Selected images RecyclerView
    private lateinit var selectedImagesContainer: LinearLayout
    private lateinit var rcvImgToVideo: RecyclerView
    private lateinit var selectedImageAdapter: SelectedImageAdapter
    
    // State variables
    private var selectedDuration = 10 // 10, 15, or 20 seconds
    private var selectedImageUri: Uri? = null
    private val selectedImages = mutableListOf<Uri>()
    private var isGenerating = false

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                addImageToSelection(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_to_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusManager = GenerationStatusManager(requireContext(), aiGenerationRepository, lifecycleScope)
        initViews(view)
        setupClickListeners()
        setupRecyclerView()
    }

    private fun initViews(view: View) {
        uploadArea = view.findViewById(R.id.uploadArea)
        refreshButton = view.findViewById(R.id.refreshButton)
        promptEditText = view.findViewById(R.id.promptEditText)
        negativePromptEditText = view.findViewById(R.id.negativePromptEditText)
        duration10s = view.findViewById(R.id.duration10s)
        duration15s = view.findViewById(R.id.duration15s)
        duration20s = view.findViewById(R.id.duration20s)
        generateButton = view.findViewById(R.id.generateButton)

        // Selected images RecyclerView
        selectedImagesContainer = view.findViewById(R.id.selectedImagesContainer)
        rcvImgToVideo = view.findViewById(R.id.rcv_img_to_video)
    }

    private fun setupClickListeners() {
        // Upload area click
        uploadArea.setOnClickListener {
            animateClick(uploadArea)
            openImagePicker()
        }

        // Refresh button click
        refreshButton.setOnClickListener {
            animateRotation(refreshButton)
            refreshHints()
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

        // Hint chips click listeners - removed as they don't exist in image_to_video layout
    }

    private fun selectDuration(duration: Int) {
        selectedDuration = duration
        updateDurationSelection()
    }

    private fun updateDurationSelection() {
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.ai_text_primary)
        val secondaryColor = ContextCompat.getColor(requireContext(), R.color.ai_text_secondary)

        // Reset all durations
        duration10s.setTextColor(secondaryColor)
        duration15s.setTextColor(secondaryColor)
        duration20s.setTextColor(secondaryColor)

        // Highlight selected duration
        when (selectedDuration) {
            10 -> duration10s.setTextColor(primaryColor)
            15 -> duration15s.setTextColor(primaryColor)
            20 -> duration20s.setTextColor(primaryColor)
        }
    }

    private fun generateVideo() {
        if (selectedImages.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one image", Toast.LENGTH_SHORT).show()
            return
        }

        if (isGenerating) {
            Toast.makeText(requireContext(), "Video generation in progress...", Toast.LENGTH_SHORT).show()
            return
        }

        val prompt = promptEditText.text.toString().trim()
        val negativePrompt = negativePromptEditText.text.toString().trim()
        
        // Convert duration to enum
        val duration = when (selectedDuration) {
            10 -> VideoDuration.SHORT
            15 -> VideoDuration.MEDIUM
            20 -> VideoDuration.LONG
            else -> VideoDuration.SHORT
        }

        isGenerating = true
        updateGenerateButtonState()
        
        lifecycleScope.launch {
            try {
                Toast.makeText(requireContext(), "Starting video generation...", Toast.LENGTH_SHORT).show()
                
                val result = aiGenerationRepository.generateVideoFromImages(
                    imageUris = selectedImages,
                    prompt = prompt,
                    negativePrompt = negativePrompt,
                    duration = duration.seconds
                )
                
                if (result.isSuccess) {
                    val taskId = result.getOrNull()!!.taskId
                    statusManager.startStatusPolling(taskId, object : GenerationStatusManager.StatusCallback {
                            override fun onProgress(progress: Int, message: String) {
                                Toast.makeText(requireContext(), 
                                    "Generation progress: $progress% - $message", 
                                    Toast.LENGTH_SHORT).show()
                            }
                            
                            override fun onCompleted(resultUri: Uri?) {
                                isGenerating = false
                                updateGenerateButtonState()
                                
                                if (resultUri != null) {
                                    Toast.makeText(requireContext(), 
                                        "Video generated successfully!", 
                                        Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), 
                                        "Video generation completed but no result available", 
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                            
                            override fun onFailed(error: String) {
                                isGenerating = false
                                updateGenerateButtonState()
                                Toast.makeText(requireContext(), 
                                    "Video generation failed: $error", 
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                    
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    Toast.makeText(requireContext(), 
                        "Failed to start video generation: $error", 
                        Toast.LENGTH_LONG).show()
                }
                
            } catch (e: Exception) {
                isGenerating = false
                updateGenerateButtonState()
                Toast.makeText(requireContext(), 
                    "Error: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun updateGenerateButtonState() {
        generateButton.isEnabled = !isGenerating && selectedImages.isNotEmpty()
        generateButton.text = if (isGenerating) "Generating..." else "Generate Video"
    }

    // applyHint method removed as hint chips don't exist in image_to_video layout

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

    private fun setupRecyclerView() {
        selectedImageAdapter = SelectedImageAdapter(selectedImages) { position ->
            removeImageFromSelection(position)
        }
        rcvImgToVideo.adapter = selectedImageAdapter
        rcvImgToVideo.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun addImageToSelection(uri: Uri) {
        selectedImages.add(uri)
        selectedImageAdapter.notifyItemInserted(selectedImages.size - 1)
        updateSelectedImagesVisibility()
    }

    private fun removeImageFromSelection(position: Int) {
        // Kiểm tra bounds để tránh IndexOutOfBoundsException
        if (position >= 0 && position < selectedImages.size) {
            selectedImages.removeAt(position)
            selectedImageAdapter.notifyItemRemoved(position)
            // Cập nhật lại các position sau khi xóa
            selectedImageAdapter.notifyItemRangeChanged(position, selectedImages.size)
            updateSelectedImagesVisibility()
        } else {
            // Log lỗi để debug
            android.util.Log.e("ImageToVideoFragment", 
                "Invalid position: $position, list size: ${selectedImages.size}")
        }
    }

    private fun updateSelectedImagesVisibility() {
        // Hiển thị rcv_img_to_video khi có ảnh được chọn
        rcvImgToVideo.visibility = if (selectedImages.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        
        // Cũng có thể hiển thị selectedImagesContainer nếu cần
        selectedImagesContainer.visibility = if (selectedImages.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        
        // Cập nhật trạng thái nút generate
        updateGenerateButtonState()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun refreshHints() {
        // No hint chips in image_to_video layout, so just show a message
        Toast.makeText(requireContext(), "No hints available for image to video", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): ImageToVideoFragment {
            return ImageToVideoFragment()
        }
    }
}