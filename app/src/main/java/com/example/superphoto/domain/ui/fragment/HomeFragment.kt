package com.example.superphoto.domain.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superphoto.R
import com.example.superphoto.databinding.FragmentHomeBinding
import com.example.superphoto.domain.adapter.FeaturesAdapter
import com.example.superphoto.domain.adapter.PhotosAdapter
import com.example.superphoto.domain.adapter.RescuePhotosAdapter
import com.example.superphoto.domain.model.FeatureItem
import com.example.superphoto.domain.model.PhotoItem
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope
import kotlin.getValue

class HomeFragment : Fragment(), AndroidScopeComponent {
    override val scope: Scope by fragmentScope()

    private lateinit var binding: FragmentHomeBinding
    
    // Danh sách các tính năng chính
    private val featuresList = listOf(
        FeatureItem(R.drawable.ic_edit, R.string.edit),
        FeatureItem(R.drawable.ic_face, R.string.face),
        FeatureItem(R.drawable.ic_delete, R.string.delete),
        FeatureItem(R.drawable.ic_merge, R.string.merge),
        FeatureItem(R.drawable.ic_style, R.string.style),
        FeatureItem(R.drawable.ic_effect, R.string.effect),
        FeatureItem(R.drawable.ic_stitch, R.string.stitch)
    )
    
    // Danh sách ảnh mẫu cho phần cứu ảnh xấu
    private val rescuePhotosList = listOf(
        PhotoItem(R.drawable.background_theme, R.string.enhance),
        PhotoItem(R.drawable.background_theme, R.string.retouch),
        PhotoItem(R.drawable.background_theme, R.string.remove),
        PhotoItem(R.drawable.background_theme, R.string.enhance)
    )
    
    // Danh sách ảnh mẫu cho phần tỏa sáng với vẻ ngoài
    private val outdoorPhotosList = listOf(
        PhotoItem(R.drawable.background_theme, R.string.enhance),
        PhotoItem(R.drawable.background_theme, R.string.retouch),
        PhotoItem(R.drawable.background_theme, R.string.remove),
        PhotoItem(R.drawable.background_theme, R.string.enhance)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setupUI()
        return binding.root
    }
    
    private fun setupUI() {
        // Thiết lập RecyclerView cho các tính năng
        setupFeaturesRecyclerView()
        
        // Thiết lập RecyclerView cho phần cứu ảnh xấu
        setupRescuePhotosRecyclerView()
        
        // Thiết lập RecyclerView cho phần tỏa sáng với vẻ ngoài
        setupOutdoorPhotosRecyclerView()

    }
    
    private fun setupFeaturesRecyclerView() {
        binding.rvFeatures.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = FeaturesAdapter(featuresList) { position ->
                // Xử lý khi nhấn vào tính năng
            }
        }
    }
    
    private fun setupRescuePhotosRecyclerView() {
        binding.rvRescuePhotos.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = RescuePhotosAdapter(rescuePhotosList) { position ->
                // Xử lý khi nhấn vào ảnh cứu
            }
        }
    }
    
    private fun setupOutdoorPhotosRecyclerView() {
        binding.rvOutdoorPhotos.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = PhotosAdapter(outdoorPhotosList) { position ->
                // Xử lý khi nhấn vào ảnh ngoài trời
            }
        }
    }
    
    // Không cần các adapter ở đây nữa vì đã chuyển sang package adapter
}