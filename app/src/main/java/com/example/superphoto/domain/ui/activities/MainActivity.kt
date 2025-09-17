package com.example.superphoto.domain.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.superphoto.R
import com.example.superphoto.databinding.ActivityMainBinding
import com.example.superphoto.domain.base.BaseActivity
import com.superphoto.fragment.HomeFragment
import com.superphoto.fragment.TemplatesFragment
import com.superphoto.fragment.CreateFragment
import com.superphoto.fragment.ToolsFragment
import com.superphoto.fragment.AssetsFragment

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupBottomNavigation()
        setupClickListeners()
        
        // Load default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(), 0)
        }
    }

    private fun setupClickListeners() {
        // Setup settings button
        binding.settingsIcon.setOnClickListener {
            // Handle settings click
        }
    }
    
    private fun setupBottomNavigation() {
        val bottomNavItems = listOf(
            binding.bottomNavigation.getChildAt(0), // Home
            binding.bottomNavigation.getChildAt(1), // Templates  
            binding.bottomNavigation.getChildAt(2), // Create
            binding.bottomNavigation.getChildAt(3), // Tools
            binding.bottomNavigation.getChildAt(4)  // Assets
        )
        
        bottomNavItems.forEachIndexed { index, item ->
            item.setOnClickListener {
                selectBottomNavItem(index)
            }
        }
    }
    
    private fun selectBottomNavItem(index: Int) {
        if (currentFragmentIndex == index) return
        
        val fragment = when (index) {
            0 -> HomeFragment.newInstance()
            1 -> TemplatesFragment.newInstance()
            2 -> CreateFragment.newInstance()
            3 -> ToolsFragment.newInstance()
            4 -> AssetsFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
        
        loadFragment(fragment, index)
        updateBottomNavSelection(index)
    }
    
    private fun loadFragment(fragment: Fragment, index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        currentFragmentIndex = index
    }
    
    private fun updateBottomNavSelection(selectedIndex: Int) {
        for (i in 0 until binding.bottomNavigation.childCount) {
            val item = binding.bottomNavigation.getChildAt(i)
            val isSelected = i == selectedIndex
            
            // Update visual state based on selection
            item.alpha = if (isSelected) 1.0f else 0.6f
            
            // You can add more visual feedback here like changing colors
            // based on your design requirements
        }
    }
}