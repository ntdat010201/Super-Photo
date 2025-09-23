package com.example.superphoto.ui.fragment

import android.os.Bundle
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
        celebrityPhotoCard = view.findViewById(R.id.celebrityPhotoCard)
        backgroundRemoverCard = view.findViewById(R.id.backgroundRemoverCard)
        faceSwapCard = view.findViewById(R.id.faceSwapCard)
        aiEnhanceCard = view.findViewById(R.id.aiEnhanceCard)
        colorizeCard = view.findViewById(R.id.colorizeCard)
        objectRemovalCard = view.findViewById(R.id.objectRemovalCard)
        styleTransferCard = view.findViewById(R.id.styleTransferCard)
    }

    private fun setupClickListeners() {
        celebrityPhotoCard.setOnClickListener {
            openCelebrityPhotoTool()
        }

        backgroundRemoverCard.setOnClickListener {
            openTransformationTool("background_remover")
        }

        faceSwapCard.setOnClickListener {
            openTransformationTool("face_swap")
        }

        aiEnhanceCard.setOnClickListener {
            openTransformationTool("ai_enhance")
        }

        colorizeCard.setOnClickListener {
            openTransformationTool("enhance_colorize")
        }

        objectRemovalCard.setOnClickListener {
            showComingSoon("Object Removal")
        }

        styleTransferCard.setOnClickListener {
            openTransformationTool("style_transfer")
        }
    }

    private fun openCelebrityPhotoTool() {
        // Navigate to Celebrity Photo Tool
        val celebrityPhotoFragment = CelebrityPhotoFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, celebrityPhotoFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openTransformationTool(transformationId: String) {
        val transformation = TransformationConstants.getTransformationById(transformationId)
        if (transformation != null) {
            // Navigate to AI Transformation Activity
            val aiTransformationFragment = AITransformationFragment.newInstance(transformation)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, aiTransformationFragment)
                .addToBackStack(null)
                .commit()
        } else {
            showComingSoon("AI Tool")
        }
    }

    private fun showComingSoon(toolName: String) {
        Toast.makeText(context, "$toolName - Coming Soon! 🚀", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = ToolsFragment()
    }
}