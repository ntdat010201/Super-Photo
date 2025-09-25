package com.example.superphoto.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.superphoto.R
import com.example.superphoto.ui.fragment.AITransformationFragment
import com.superphoto.constants.TransformationConstants

class ToolsFragment : Fragment() {

    // UI Elements
    private lateinit var celebrityPhotoCard: CardView
    private lateinit var backgroundRemoverCard: CardView
    private lateinit var faceSwapCard: CardView
    private lateinit var aiEnhanceCard: CardView
    private lateinit var colorizeCard: CardView
    private lateinit var objectRemovalCard: CardView
    private lateinit var styleTransferCard: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tools, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupClickListeners()
    }

    private fun initViews(view: View) {
        try {
            celebrityPhotoCard = view.findViewById(R.id.celebrityPhotoCard)
                ?: throw IllegalStateException("celebrityPhotoCard not found")
            backgroundRemoverCard = view.findViewById(R.id.backgroundRemoverCard)
                ?: throw IllegalStateException("backgroundRemoverCard not found")
            faceSwapCard = view.findViewById(R.id.faceSwapCard)
                ?: throw IllegalStateException("faceSwapCard not found")
            aiEnhanceCard = view.findViewById(R.id.aiEnhanceCard)
                ?: throw IllegalStateException("aiEnhanceCard not found")
            colorizeCard = view.findViewById(R.id.colorizeCard)
                ?: throw IllegalStateException("colorizeCard not found")
            objectRemovalCard = view.findViewById(R.id.objectRemovalCard)
                ?: throw IllegalStateException("objectRemovalCard not found")
            styleTransferCard = view.findViewById(R.id.styleTransferCard)
                ?: throw IllegalStateException("styleTransferCard not found")
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error initializing views: ${e.message}", e)
            showErrorToast("Failed to initialize tools interface")
        }
    }

    private fun setupClickListeners() {
        try {
            if (::celebrityPhotoCard.isInitialized) {
                celebrityPhotoCard.setOnClickListener {
                    safeExecute { openCelebrityPhotoTool() }
                }
            }

            if (::backgroundRemoverCard.isInitialized) {
                backgroundRemoverCard.setOnClickListener {
                    safeExecute { openTransformationTool("background_remover") }
                }
            }

            if (::faceSwapCard.isInitialized) {
                faceSwapCard.setOnClickListener {
                    safeExecute { openTransformationTool("face_swap") }
                }
            }

            if (::aiEnhanceCard.isInitialized) {
                aiEnhanceCard.setOnClickListener {
                    safeExecute { openTransformationTool("ai_enhance") }
                }
            }

            if (::colorizeCard.isInitialized) {
                colorizeCard.setOnClickListener {
                    safeExecute { openTransformationTool("enhance_colorize") }
                }
            }

            if (::objectRemovalCard.isInitialized) {
                objectRemovalCard.setOnClickListener {
                    safeExecute { showComingSoon("Object Removal") }
                }
            }

            if (::styleTransferCard.isInitialized) {
                styleTransferCard.setOnClickListener {
                    safeExecute { openTransformationTool("style_transfer") }
                }
            }
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error setting up click listeners: ${e.message}", e)
            showErrorToast("Failed to setup tool interactions")
        }
    }

    private fun openCelebrityPhotoTool() {
        try {
            if (!isAdded || isDetached) {
                showErrorToast("Cannot navigate - fragment not attached")
                return
            }

            // Navigate to Celebrity Photo Tool
            val celebrityPhotoFragment = CelebrityPhotoFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, celebrityPhotoFragment)
                .addToBackStack(null)
                .commit()
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error opening celebrity photo tool: ${e.message}", e)
            showErrorToast("Failed to open Celebrity Photo tool")
        }
    }

    private fun openTransformationTool(transformationId: String) {
        try {
            if (!isAdded || isDetached) {
                showErrorToast("Cannot navigate - fragment not attached")
                return
            }

            val transformation = TransformationConstants.getTransformationById(transformationId)
            if (transformation != null) {
                // Navigate to AI Transformation Activity
                val aiTransformationFragment = AITransformationFragment.newInstance(transformation)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, aiTransformationFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Log.w("ToolsFragment", "Transformation not found for ID: $transformationId")
                showComingSoon("AI Tool")
            }
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error opening transformation tool '$transformationId': ${e.message}", e)
            showErrorToast("Failed to open AI transformation tool")
        }
    }

    private fun showComingSoon(toolName: String) {
        try {
            context?.let { ctx ->
                Toast.makeText(ctx, "$toolName - Coming Soon! 🚀", Toast.LENGTH_SHORT).show()
            } ?: run {
                Log.w("ToolsFragment", "Cannot show coming soon toast - context is null")
            }
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error showing coming soon toast: ${e.message}", e)
        }
    }

    private fun showErrorToast(message: String) {
        try {
            context?.let { ctx ->
                Toast.makeText(ctx, "Error: $message", Toast.LENGTH_LONG).show()
            } ?: run {
                Log.e("ToolsFragment", "Cannot show error toast - context is null: $message")
            }
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error showing error toast: ${e.message}", e)
        }
    }

    private fun safeExecute(action: () -> Unit) {
        try {
            if (!isAdded || isDetached) {
                showErrorToast("Action cannot be performed - fragment not available")
                return
            }
            action()
        } catch (e: Exception) {
            Log.e("ToolsFragment", "Error executing action: ${e.message}", e)
            showErrorToast("An unexpected error occurred")
        }
    }

    companion object {
        fun newInstance() = ToolsFragment()
    }
}