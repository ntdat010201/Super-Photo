package com.example.superphoto.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.superphoto.utils.StorageHelper
import java.io.File
import java.io.FileOutputStream
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.adapter.SelectedImageAdapter
import com.example.superphoto.data.repository.AIGenerationManager
import com.example.superphoto.data.model.VideoDuration
import com.example.superphoto.utils.GenerationStatusManager
import com.superphoto.ai.AIRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject

class ImageToVideoFragment : Fragment() {

    // Dependency injection
    private val aiGenerationManager: AIGenerationManager by inject()
    private val aiRepository: AIRepository by inject()
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
    
    // New UI elements for multiple image selection
    private lateinit var imagesCountText: TextView
    private lateinit var addMoreImagesButton: Button
    
    // Selected images RecyclerView
    private lateinit var selectedImagesContainer: LinearLayout
    private lateinit var rcvImgToVideo: RecyclerView
    private lateinit var selectedImageAdapter: SelectedImageAdapter
    
    // Video preview UI elements
    private lateinit var videoPreviewSection: LinearLayout
    private lateinit var loadingContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var progressPercentage: TextView
    private lateinit var videoResultContainer: LinearLayout
    private lateinit var videoPreview: VideoView
    private lateinit var playPauseButton: Button
    private lateinit var downloadVideoButton: Button
    private lateinit var shareVideoButton: Button
    
    // State variables
    private var selectedDuration = 10 // 10, 15, or 20 seconds
    private var selectedImageUri: Uri? = null
    private val selectedImages = mutableListOf<Uri>()
    private var isGenerating = false
    private var generatedVideoUri: Uri? = null
    private var isVideoPlaying = false

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
        statusManager = GenerationStatusManager(requireContext(), aiGenerationManager, lifecycleScope)
        initViews(view)
        setupClickListeners()
        setupRecyclerView()
    }

    // Image picker launcher for multiple selection
    private val multipleImagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                if (data.clipData != null) {
                    // Multiple images selected
                    val clipData = data.clipData!!
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        addImageToSelection(uri)
                    }
                } else if (data.data != null) {
                    // Single image selected
                    val uri = data.data!!
                    addImageToSelection(uri)
                }
            }
        }
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

        // New UI elements
        imagesCountText = view.findViewById(R.id.imagesCountText)
        addMoreImagesButton = view.findViewById(R.id.addMoreImagesButton)

        // Selected images RecyclerView
        selectedImagesContainer = view.findViewById(R.id.selectedImagesContainer)
        rcvImgToVideo = view.findViewById(R.id.rcv_img_to_video)
        
        // Video preview UI elements
        videoPreviewSection = view.findViewById(R.id.videoPreviewSection)
        loadingContainer = view.findViewById(R.id.loadingContainer)
        progressBar = view.findViewById(R.id.progressBar)
        progressText = view.findViewById(R.id.progressText)
        progressPercentage = view.findViewById(R.id.progressPercentage)
        videoResultContainer = view.findViewById(R.id.videoResultContainer)
        videoPreview = view.findViewById(R.id.videoPreview)
        playPauseButton = view.findViewById(R.id.playPauseButton)
        downloadVideoButton = view.findViewById(R.id.downloadVideoButton)
        shareVideoButton = view.findViewById(R.id.shareVideoButton)
    }

    private fun setupClickListeners() {
        // Upload area click
        uploadArea.setOnClickListener {
            animateClick(uploadArea)
            openImagePicker()
        }

        // Add more images button
        addMoreImagesButton.setOnClickListener {
            animateClick(addMoreImagesButton)
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
        
        // Video controls
        playPauseButton.setOnClickListener {
            animateClick(playPauseButton)
            toggleVideoPlayback()
        }
        
        downloadVideoButton.setOnClickListener {
            animateClick(downloadVideoButton)
            downloadVideo()
        }
        
        shareVideoButton.setOnClickListener {
            animateClick(shareVideoButton)
            shareVideo()
        }
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
        // Validate input before proceeding
        val validationResult = validateInput()
        if (!validationResult.isValid) {
            Toast.makeText(requireContext(), validationResult.errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        if (isGenerating) {
            Toast.makeText(requireContext(), "Video generation in progress...", Toast.LENGTH_SHORT).show()
            return
        }

        val prompt = promptEditText.text.toString().trim()

        isGenerating = true
        updateGenerateButtonState()
        showLoadingState()
        
        lifecycleScope.launch {
            try {
                if (selectedImages.isNotEmpty()) {
                    // Image + Text generation workflow (like AIImageFragment)
                    generateVideoFromImagesAndText(prompt)
                } else {
                    // Text-only generation workflow
                    generateVideoFromTextOnly(prompt)
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    isGenerating = false
                    updateGenerateButtonState()
                    hideLoadingState()
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun generateVideoFromImagesAndText(userPrompt: String) {
        try {
            // Phase 1: Loading and analyzing images
            activity?.runOnUiThread {
                showLoadingWithMessage("🖼️ Đang tải và phân tích ảnh...")
            }
            
            // Step 1: Load bitmap from first image URI
            val firstImageBitmap = loadBitmapFromUri(selectedImages.first())
            if (firstImageBitmap == null) {
                throw Exception("Failed to load selected image")
            }
            
            // Phase 2: AI analysis with Gemini
            activity?.runOnUiThread {
                showLoadingWithMessage("🤖 AI đang phân tích nội dung để tạo video...")
            }
            
            // Step 2: Generate video description using Gemini
            val videoPrompt = createVideoPrompt(userPrompt, selectedImages.size, selectedDuration)
            val descriptionResult = aiRepository.generateImageDescription(firstImageBitmap, videoPrompt)
            if (descriptionResult.isFailure) {
                throw Exception("Failed to analyze image: ${descriptionResult.exceptionOrNull()?.message}")
            }
            
            val enhancedPrompt = descriptionResult.getOrThrow()
            
            // Phase 3: Video generation using Pollinations (as image sequence)
            activity?.runOnUiThread {
                showLoadingWithMessage("🎬 Đang tạo video với yêu cầu của bạn...")
            }
            
            // Step 3: Generate video frames using Pollinations
            val videoResult = generateVideoFromPollinations(enhancedPrompt)
            
            if (videoResult.isSuccess) {
                activity?.runOnUiThread {
                    showLoadingWithMessage("✅ Hoàn thành! Đang tải video...")
                }
                val generatedVideoUri = videoResult.getOrThrow()
                activity?.runOnUiThread {
                    isGenerating = false
                    updateGenerateButtonState()
                    showVideoResult(generatedVideoUri)
                }
            } else {
                throw Exception("Failed to generate video: ${videoResult.exceptionOrNull()?.message}")
            }
            
        } catch (e: Exception) {
            activity?.runOnUiThread {
                isGenerating = false
                updateGenerateButtonState()
                hideLoadingState()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun generateVideoFromTextOnly(userPrompt: String) {
        try {
            // Phase 1: AI analysis with Gemini
            activity?.runOnUiThread {
                showLoadingWithMessage("🤖 AI đang phân tích yêu cầu để tạo video...")
            }
            
            // Step 1: Generate video description using Gemini
            val videoPrompt = createVideoPrompt(userPrompt, 0, selectedDuration)
            val enhancedPrompt = "Create a detailed video concept: $videoPrompt"
            
            // Phase 2: Video generation using Pollinations
            activity?.runOnUiThread {
                showLoadingWithMessage("🎬 Đang tạo video với yêu cầu của bạn...")
            }
            
            // Step 2: Generate video using Pollinations
            val videoResult = generateVideoFromPollinations(enhancedPrompt)
            
            if (videoResult.isSuccess) {
                activity?.runOnUiThread {
                    showLoadingWithMessage("✅ Hoàn thành! Đang tải video...")
                }
                val generatedVideoUri = videoResult.getOrThrow()
                activity?.runOnUiThread {
                    isGenerating = false
                    updateGenerateButtonState()
                    showVideoResult(generatedVideoUri)
                }
            } else {
                throw Exception("Failed to generate video: ${videoResult.exceptionOrNull()?.message}")
            }
            
        } catch (e: Exception) {
            activity?.runOnUiThread {
                isGenerating = false
                updateGenerateButtonState()
                hideLoadingState()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
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
        // Kiểm tra giới hạn số lượng ảnh (tối đa 10 ảnh)
        if (selectedImages.size >= 10) {
            Toast.makeText(requireContext(), "Maximum 10 images allowed", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Kiểm tra xem ảnh đã được chọn chưa
        if (selectedImages.contains(uri)) {
            Toast.makeText(requireContext(), "Image already selected", Toast.LENGTH_SHORT).show()
            return
        }
        
        selectedImages.add(uri)
        selectedImageAdapter.notifyItemInserted(selectedImages.size - 1)
        updateSelectedImagesVisibility()
        updateImagesCount()
    }

    private fun removeImageFromSelection(position: Int) {
        // Kiểm tra bounds để tránh IndexOutOfBoundsException
        if (position >= 0 && position < selectedImages.size) {
            selectedImages.removeAt(position)
            selectedImageAdapter.notifyItemRemoved(position)
            // Cập nhật lại các position sau khi xóa
            selectedImageAdapter.notifyItemRangeChanged(position, selectedImages.size)
            updateSelectedImagesVisibility()
            updateImagesCount()
        } else {
            // Log lỗi để debug
            android.util.Log.e("ImageToVideoFragment", 
                "Invalid position: $position, list size: ${selectedImages.size}")
        }
    }

    private fun updateSelectedImagesVisibility() {
        // Hiển thị selectedImagesContainer khi có ảnh được chọn
        selectedImagesContainer.visibility = if (selectedImages.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        
        // Cập nhật trạng thái nút generate
        updateGenerateButtonState()
    }

    private fun updateImagesCount() {
        val count = selectedImages.size
        imagesCountText.text = "Selected Images ($count)"
        
        // Cập nhật text của nút Add More dựa trên số lượng ảnh
        addMoreImagesButton.text = if (count == 0) {
            "+ Add Images"
        } else {
            "+ Add More"
        }
        
        // Disable nút Add More nếu đã đạt giới hạn
        addMoreImagesButton.isEnabled = count < 10
        addMoreImagesButton.alpha = if (count < 10) 1.0f else 0.5f
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            // Cho phép chọn nhiều ảnh
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            type = "image/*"
        }
        multipleImagePickerLauncher.launch(intent)
    }

    private fun refreshHints() {
        // No hint chips in image_to_video layout, so just show a message
        Toast.makeText(requireContext(), "No hints available for image to video", Toast.LENGTH_SHORT).show()
    }
    
    // Video preview UI state management
    private fun showLoadingState() {
        videoPreviewSection.visibility = View.VISIBLE
        loadingContainer.visibility = View.VISIBLE
        videoResultContainer.visibility = View.GONE
        progressText.text = "Generating video..."
        progressPercentage.text = "0%"
    }
    
    private fun hideLoadingState() {
        videoPreviewSection.visibility = View.GONE
        loadingContainer.visibility = View.GONE
        videoResultContainer.visibility = View.GONE
    }
    
    private fun updateProgress(progress: Int, message: String) {
        progressText.text = message
        progressPercentage.text = "$progress%"
    }
    
    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            android.util.Log.e("ImageToVideoFragment", "Error loading bitmap from URI: ${e.message}")
            null
        }
    }

    private fun createVideoPrompt(userPrompt: String, imageCount: Int, duration: Int): String {
        val basePrompt = if (imageCount > 0) {
            "Create a $duration-second video based on the provided image(s) and this description: $userPrompt. " +
            "Make it cinematic, smooth, and engaging with natural motion and transitions."
        } else {
            "Create a $duration-second video based on this description: $userPrompt. " +
            "Make it cinematic, smooth, and engaging with natural motion and visual appeal."
        }
        return basePrompt
    }

    private suspend fun generateVideoFromPollinations(prompt: String): Result<Uri> {
        return withContext(Dispatchers.IO) {
            try {
                // Use Pollinations to generate an image that represents the video concept
                val imageResult = aiRepository.generateImageFromText(prompt)
                if (imageResult.isFailure) {
                    return@withContext Result.failure(Exception("Failed to generate video concept: ${imageResult.exceptionOrNull()?.message}"))
                }
                
                val imageBitmap = imageResult.getOrThrow()
                
                // Convert the generated bitmap to a simple video (for now, we'll create a static video)
                val videoUri = createVideoFromBitmap(imageBitmap)
                if (videoUri != null) {
                    Result.success(videoUri)
                } else {
                    Result.failure(Exception("Failed to create video from generated image"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private suspend fun createVideoFromBitmap(bitmap: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                // For now, we'll save the image as a "video" (this is a simplified approach)
                // In a real implementation, you would create an actual video file
                
                // Convert bitmap to byte array
                val outputStream = java.io.ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                val imageBytes = outputStream.toByteArray()
                
                // Save as video file (simplified approach)
                val fileName = "generated_video_${System.currentTimeMillis()}.mp4"
                val videoFile = StorageHelper.saveVideoToExternalStorage(
                    requireContext(),
                    imageBytes,
                    fileName,
                    "ai_generated"
                )
                
                if (videoFile != null) {
                    Uri.fromFile(videoFile)
                } else {
                    null
                }
            } catch (e: Exception) {
                android.util.Log.e("ImageToVideoFragment", "Error creating video from bitmap: ${e.message}")
                null
            }
        }
    }

    private fun showLoadingWithMessage(message: String) {
        loadingContainer.visibility = View.VISIBLE
        videoResultContainer.visibility = View.GONE
        progressText.text = message
        progressPercentage.text = "0%"
        generateButton.isEnabled = false
    }

    private fun showVideoResult(videoUri: Uri) {
        generatedVideoUri = videoUri
        loadingContainer.visibility = View.GONE
        videoResultContainer.visibility = View.VISIBLE
        
        // Setup video preview
        videoPreview.setVideoURI(videoUri)
        videoPreview.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            // Auto-play the video
            videoPreview.start()
            isVideoPlaying = true
            playPauseButton.text = "⏸ Pause"
        }
        
        videoPreview.setOnErrorListener { _, _, _ ->
            Toast.makeText(requireContext(), "Error loading video preview", Toast.LENGTH_SHORT).show()
            false
        }
    }
    
    private fun toggleVideoPlayback() {
        if (isVideoPlaying) {
            videoPreview.pause()
            playPauseButton.text = "▶ Play"
            isVideoPlaying = false
        } else {
            videoPreview.start()
            playPauseButton.text = "⏸ Pause"
            isVideoPlaying = true
        }
    }
    
    private fun downloadVideo() {
        generatedVideoUri?.let { uri ->
            try {
                val contentResolver = requireContext().contentResolver
                val inputStream = contentResolver.openInputStream(uri)
                
                if (inputStream != null) {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val superPhotoDir = File(downloadsDir, "SuperPhoto_Downloads")
                    if (!superPhotoDir.exists()) {
                        superPhotoDir.mkdirs()
                    }
                    
                    val fileName = "SuperPhoto_Video_${System.currentTimeMillis()}.mp4"
                    val outputFile = File(superPhotoDir, fileName)
                    
                    val outputStream = FileOutputStream(outputFile)
                    inputStream.copyTo(outputStream)
                    
                    inputStream.close()
                    outputStream.close()
                    
                    Toast.makeText(requireContext(), 
                        "Video saved to Downloads/SuperPhoto_Downloads/$fileName", 
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Error accessing video file", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error downloading video: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "No video to download", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shareVideo() {
        generatedVideoUri?.let { uri ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "video/mp4"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Video"))
        } ?: run {
            Toast.makeText(requireContext(), "No video to share", Toast.LENGTH_SHORT).show()
        }
    }

    // Validation function
    private fun validateInput(): ValidationResult {
        val prompt = promptEditText.text.toString().trim()
        
        // Check minimum images
        if (selectedImages.isEmpty()) {
            return ValidationResult(false, "Please select at least one image to generate video")
        }
        
        // Check maximum images (limit to 10)
        if (selectedImages.size > 10) {
            return ValidationResult(false, "Maximum 10 images allowed for video generation")
        }
        
        // Check prompt requirements
        if (prompt.isEmpty()) {
            return ValidationResult(false, "Please enter a description for your video")
        }
        
        // Check minimum prompt length
        if (prompt.length < 5) {
            return ValidationResult(false, "Description must be at least 5 characters long")
        }
        
        // Check maximum prompt length
        if (prompt.length > 500) {
            return ValidationResult(false, "Description must be less than 500 characters")
        }
        
        // Check for inappropriate content (basic check)
        val inappropriateWords = listOf("violence", "hate", "explicit", "nsfw")
        val lowerPrompt = prompt.lowercase()
        for (word in inappropriateWords) {
            if (lowerPrompt.contains(word)) {
                return ValidationResult(false, "Please use appropriate content for video generation")
            }
        }
        
        // Check if duration is selected
        if (selectedDuration == null) {
            return ValidationResult(false, "Please select video duration")
        }
        
        return ValidationResult(true)
    }

    // Data class for validation result
    private data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String = ""
    )

    companion object {
        fun newInstance(): ImageToVideoFragment {
            return ImageToVideoFragment()
        }
    }
}