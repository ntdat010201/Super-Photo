package com.example.superphoto.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.adapter.CelebrityAdapter
import com.example.superphoto.model.Celebrity
import com.example.superphoto.ui.component.CelebrityInputUploader
import com.example.superphoto.data.repository.GeminiRepository
import org.koin.android.ext.android.inject
import kotlinx.coroutines.*
import java.io.IOException

class CelebrityPhotoFragment : Fragment() {

    // UI Elements
    private lateinit var backButton: ImageView
    private lateinit var titleText: TextView
    
    // User Photo Section
    private lateinit var userPhotoCard: CardView
    private lateinit var userPhotoImageView: ImageView
    private lateinit var userPhotoPlaceholder: LinearLayout
    private lateinit var removeUserPhotoButton: ImageView
    
    // Celebrity Selection Section
    private lateinit var celebrityNameEditText: EditText
    private lateinit var celebrityRecyclerView: RecyclerView
    private lateinit var celebrityAdapter: CelebrityAdapter
    
    // Action Buttons
    private lateinit var generateButton: Button
    private lateinit var loadingProgress: LinearLayout
    
    // Result Section
    private lateinit var resultSection: LinearLayout
    private lateinit var resultImageView: ImageView
    private lateinit var downloadButton: Button
    private lateinit var shareButton: Button
    
    // State variables
    private var selectedUserPhoto: Bitmap? = null
    private var selectedCelebrity: Celebrity? = null
    private var isGenerating = false
    
    // Celebrity Input Uploader
    private lateinit var celebrityInputUploader: CelebrityInputUploader
    
    // Gemini Repository
    private val geminiRepository: GeminiRepository by inject()
    
    // Coroutine scope
    private val fragmentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    // Sample celebrity data
    private val celebrities = listOf(
        Celebrity("1", "Taylor Swift", "https://example.com/taylor.jpg"),
        Celebrity("2", "Leonardo DiCaprio", "https://example.com/leo.jpg"),
        Celebrity("3", "Emma Watson", "https://example.com/emma.jpg"),
        Celebrity("4", "Ryan Reynolds", "https://example.com/ryan.jpg"),
        Celebrity("5", "Scarlett Johansson", "https://example.com/scarlett.jpg"),
        Celebrity("6", "Chris Evans", "https://example.com/chris.jpg"),
        Celebrity("7", "Jennifer Lawrence", "https://example.com/jennifer.jpg"),
        Celebrity("8", "Robert Downey Jr.", "https://example.com/rdj.jpg")
    )
    


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_celebrity_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupCelebrityInputUploader()
        setupClickListeners()
        setupCelebrityRecyclerView()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        fragmentScope.cancel()
    }

    private fun initViews(view: View) {
        backButton = view.findViewById(R.id.backButton)
        titleText = view.findViewById(R.id.titleText)
        
        userPhotoCard = view.findViewById(R.id.userPhotoCard)
        userPhotoImageView = view.findViewById(R.id.userPhotoImageView)
        userPhotoPlaceholder = view.findViewById(R.id.userPhotoPlaceholder)
        removeUserPhotoButton = view.findViewById(R.id.removeUserPhotoButton)
        
        celebrityNameEditText = view.findViewById(R.id.celebrityNameEditText)
        celebrityRecyclerView = view.findViewById(R.id.celebrityRecyclerView)
        
        generateButton = view.findViewById(R.id.generateButton)
        loadingProgress = view.findViewById(R.id.loadingProgress)
        
        resultSection = view.findViewById(R.id.resultSection)
        resultImageView = view.findViewById(R.id.resultImageView)
        downloadButton = view.findViewById(R.id.downloadButton)
        shareButton = view.findViewById(R.id.shareButton)
    }

    private fun setupCelebrityInputUploader() {
        celebrityInputUploader = CelebrityInputUploader(
            fragment = this,
            uploadArea = userPhotoCard,
            selectedImageView = userPhotoImageView,
            uploadPlaceholder = userPhotoPlaceholder,
            onImageSelected = { uri ->
                loadImageFromUri(uri)
            }
        )
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        removeUserPhotoButton.setOnClickListener {
            removeUserPhoto()
        }

        generateButton.setOnClickListener {
            generateCelebrityPhoto()
        }

        downloadButton.setOnClickListener {
            downloadResult()
        }

        shareButton.setOnClickListener {
            shareResult()
        }
    }

    private fun setupCelebrityRecyclerView() {
        celebrityAdapter = CelebrityAdapter(celebrities) { celebrity ->
            selectCelebrity(celebrity)
        }
        
        celebrityRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = celebrityAdapter
        }
    }



    private fun loadImageFromUri(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            
            selectedUserPhoto = bitmap
            userPhotoImageView.setImageBitmap(bitmap)
            userPhotoImageView.visibility = View.VISIBLE
            userPhotoPlaceholder.visibility = View.GONE
            removeUserPhotoButton.visibility = View.VISIBLE
            
            updateGenerateButtonState()
            
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeUserPhoto() {
        selectedUserPhoto = null
        celebrityInputUploader.clearSelection()
        removeUserPhotoButton.visibility = View.GONE
        updateGenerateButtonState()
    }

    private fun selectCelebrity(celebrity: Celebrity) {
        selectedCelebrity = celebrity
        celebrityNameEditText.setText(celebrity.name)
        
        // Update adapter selection
        celebrityAdapter.setSelectedCelebrity(celebrity.id)
        
        updateGenerateButtonState()
    }

    private fun updateGenerateButtonState() {
        val canGenerate = selectedUserPhoto != null && 
                         (selectedCelebrity != null || celebrityNameEditText.text.toString().trim().isNotEmpty())
        
        generateButton.isEnabled = canGenerate && !isGenerating
        generateButton.alpha = if (canGenerate && !isGenerating) 1.0f else 0.5f
    }

    private fun generateCelebrityPhoto() {
        if (!celebrityInputUploader.validateImageSelection()) {
            return
        }
        
        if (selectedCelebrity == null) {
            Toast.makeText(context, "Please select a celebrity! ⭐", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (isGenerating) {
            return
        }
        
        isGenerating = true
        loadingProgress.visibility = View.VISIBLE
        generateButton.isEnabled = false
        generateButton.text = "Generating..."
        
        // Generate celebrity photo with AI
        generateCelebrityPhotoWithAI(selectedUserPhoto!!, selectedCelebrity!!.name)
    }

    private fun generateCelebrityPhotoWithAI(userPhoto: Bitmap, celebrityName: String) {
        fragmentScope.launch {
            try {
                val result = geminiRepository.generateCelebrityPhoto(userPhoto, celebrityName)
                
                if (result.isSuccess) {
                    val generatedText = result.getOrNull()
                    if (!generatedText.isNullOrEmpty()) {
                        // For now, show the original photo as result since Gemini returns text
                        // In a real implementation, you would use an image generation API
                        showResult(userPhoto)
                        
                        // Show the AI description in a toast or dialog
                        Toast.makeText(
                            context, 
                            "AI Analysis: ${generatedText.take(100)}...", 
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        showError("No response from AI")
                    }
                } else {
                    val error = result.exceptionOrNull()
                    showError("AI processing failed: ${error?.message ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun showResult(resultBitmap: Bitmap) {
        isGenerating = false
        loadingProgress.visibility = View.GONE
        generateButton.isEnabled = true
        generateButton.text = "Generate Celebrity Photo"
        
        resultImageView.setImageBitmap(resultBitmap)
        resultSection.visibility = View.VISIBLE
        
        // Scroll to result
        view?.findViewById<ScrollView>(R.id.scrollView)?.post {
            view?.findViewById<ScrollView>(R.id.scrollView)?.fullScroll(View.FOCUS_DOWN)
        }
        
        Toast.makeText(context, "Celebrity photo generated! 🎉", Toast.LENGTH_SHORT).show()
    }
    
    private fun showError(message: String) {
        isGenerating = false
        loadingProgress.visibility = View.GONE
        generateButton.isEnabled = true
        generateButton.text = "Generate Celebrity Photo"
        
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun downloadResult() {
        Toast.makeText(context, "Download feature coming soon! 📥", Toast.LENGTH_SHORT).show()
    }

    private fun shareResult() {
        Toast.makeText(context, "Share feature coming soon! 📤", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = CelebrityPhotoFragment()
    }
}