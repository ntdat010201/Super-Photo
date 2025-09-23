package com.example.superphoto.ui.component

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.superphoto.R

class CelebrityInputUploader(
    private val fragment: Fragment,
    private val uploadArea: View,
    private val selectedImageView: ImageView,
    private val uploadPlaceholder: LinearLayout,
    private val onImageSelected: (Uri) -> Unit
) {
    
    private var selectedImageUri: Uri? = null
    
    // Image picker launcher
    private val imagePickerLauncher: ActivityResultLauncher<Intent> = 
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    handleImageSelection(uri)
                }
            }
        }
    
    // Camera launcher
    private val cameraLauncher: ActivityResultLauncher<Intent> = 
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri?.let { uri ->
                    handleImageSelection(uri)
                }
            }
        }
    
    init {
        setupUploadArea()
    }
    
    private fun setupUploadArea() {
        uploadArea.setOnClickListener {
            showImagePickerOptions()
        }
    }
    
    private fun showImagePickerOptions() {
        // Create intent chooser for gallery and camera
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
        
        imagePickerLauncher.launch(chooserIntent)
    }
    
    private fun handleImageSelection(uri: Uri) {
        selectedImageUri = uri
        displaySelectedImage(uri)
        onImageSelected(uri)
        
        // Show success message
        Toast.makeText(fragment.context, "Image selected successfully! 📸", Toast.LENGTH_SHORT).show()
    }
    
    private fun displaySelectedImage(uri: Uri) {
        // Hide upload placeholder
        uploadPlaceholder.visibility = LinearLayout.GONE
        
        // Show selected image
        selectedImageView.visibility = ImageView.VISIBLE
        
        // Load image with Glide
        Glide.with(fragment)
            .load(uri)
            .transform(CircleCrop())
            .placeholder(R.drawable.ic_person_placeholder)
            .error(R.drawable.ic_person_placeholder)
            .into(selectedImageView)
    }
    
    fun clearSelection() {
        selectedImageUri = null
        selectedImageView.visibility = ImageView.GONE
        uploadPlaceholder.visibility = LinearLayout.VISIBLE
    }
    
    fun getSelectedImageUri(): Uri? = selectedImageUri
    
    fun hasSelectedImage(): Boolean = selectedImageUri != null
    
    fun validateImageSelection(): Boolean {
        if (selectedImageUri == null) {
            Toast.makeText(fragment.context, "Please select an image first! 📷", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    
    companion object {
        private const val TAG = "CelebrityInputUploader"
        
        // Image validation constants
        private const val MAX_IMAGE_SIZE_MB = 10
        private const val MIN_IMAGE_DIMENSION = 256
        
        fun isValidImageSize(uri: Uri): Boolean {
            // TODO: Implement image size validation
            return true
        }
        
        fun isValidImageFormat(uri: Uri): Boolean {
            // TODO: Implement image format validation (JPEG, PNG)
            return true
        }
    }
}