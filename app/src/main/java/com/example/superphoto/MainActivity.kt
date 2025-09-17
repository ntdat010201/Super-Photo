package com.example.superphoto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.domain.adapter.HorizontalNavAdapter
import com.example.superphoto.domain.adapter.PhotoSampleAdapter
import com.example.superphoto.domain.base.BaseActivity
import com.example.superphoto.domain.model.NavItem
import com.example.superphoto.domain.model.PhotoSample

class MainActivity : BaseActivity() {

    private lateinit var bottomNavigationRecyclerView: RecyclerView
    private lateinit var rescuePhotosRecyclerView: RecyclerView
    private lateinit var appearancePhotosRecyclerView: RecyclerView

    private lateinit var horizontalNavAdapter: HorizontalNavAdapter
    private lateinit var rescuePhotosAdapter: PhotoSampleAdapter
    private lateinit var appearancePhotosAdapter: PhotoSampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerViews
        setupRecyclerViews()

        // Load data
        loadData()
    }


    private fun setupRecyclerViews() {
        // Setup Bottom Navigation RecyclerView
        bottomNavigationRecyclerView = findViewById(R.id.rv_menu)
        bottomNavigationRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        horizontalNavAdapter = HorizontalNavAdapter(emptyList()) { navItem, position ->
            handleNavItemClick(navItem, position)
        }
        bottomNavigationRecyclerView.adapter = horizontalNavAdapter

        // Setup Rescue Photos RecyclerView
        rescuePhotosRecyclerView = findViewById(R.id.rv_rescue_photos)
        rescuePhotosRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rescuePhotosAdapter = PhotoSampleAdapter(emptyList()) { photoSample ->
            handlePhotoClick(photoSample, "rescue")
        }
        rescuePhotosRecyclerView.adapter = rescuePhotosAdapter

        // Setup Appearance Photos RecyclerView
        appearancePhotosRecyclerView = findViewById(R.id.rv_appearance_photos)
        appearancePhotosRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        appearancePhotosAdapter = PhotoSampleAdapter(emptyList()) { photoSample ->
            handlePhotoClick(photoSample, "appearance")
        }
        appearancePhotosRecyclerView.adapter = appearancePhotosAdapter
    }

    private fun loadData() {
        // Load navigation items
        val navItems = listOf(
            NavItem("edit", "Chỉnh sửa", R.drawable.ic_edit_tab, true),
            NavItem("face", "Khuôn mặt", R.drawable.ic_face),
            NavItem("remove", "Xóa", R.drawable.ic_remove),
            NavItem("collage", "Ghép ảnh", R.drawable.ic_collage),
            NavItem("style", "Phong cách", R.drawable.ic_style)
        )
        horizontalNavAdapter.updateItems(navItems)

        // Load rescue photos
        val rescuePhotos = listOf(
            PhotoSample(
                "1",
                "Tăng cường",
                "Chụp lại",
                R.drawable.sample_photo_placeholder,
                "rescue"
            ),
            PhotoSample("2", "Làm sáng", "Tự động", R.drawable.sample_photo_placeholder, "rescue"),
            PhotoSample("3", "Khử nhiễu", "AI", R.drawable.sample_photo_placeholder, "rescue"),
            PhotoSample("4", "Sửa mờ", "Nâng cao", R.drawable.sample_photo_placeholder, "rescue")
        )
        rescuePhotosAdapter.updateItems(rescuePhotos)

        // Load appearance photos
        val appearancePhotos = listOf(
            PhotoSample(
                "5",
                "Làm đẹp",
                "Tự nhiên",
                R.drawable.sample_photo_placeholder,
                "appearance"
            ),
            PhotoSample(
                "6",
                "Trang điểm",
                "Nhẹ nhàng",
                R.drawable.sample_photo_placeholder,
                "appearance"
            ),
            PhotoSample(
                "7",
                "Làn da",
                "Mịn màng",
                R.drawable.sample_photo_placeholder,
                "appearance"
            ),
            PhotoSample("8", "Mắt", "Sáng", R.drawable.sample_photo_placeholder, "appearance")
        )
        appearancePhotosAdapter.updateItems(appearancePhotos)
    }

    private fun handleNavItemClick(navItem: NavItem, position: Int) {
        Toast.makeText(this, "Clicked: ${navItem.title}", Toast.LENGTH_SHORT).show()
        // TODO: Implement navigation logic
    }

    private fun handlePhotoClick(photoSample: PhotoSample, category: String) {
        Toast.makeText(this, "Clicked: ${photoSample.title} in $category", Toast.LENGTH_SHORT)
            .show()
        // TODO: Implement photo editing logic
    }
}