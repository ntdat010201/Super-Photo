package com.example.superphoto.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.superphoto.R
import com.example.superphoto.base.BaseActivity
import com.example.superphoto.databinding.ActivityMainBinding
import com.example.superphoto.ui.fragment.AssetsFragment
import com.example.superphoto.ui.fragment.CreateFragment
import com.example.superphoto.ui.fragment.HomeFragment
import com.example.superphoto.ui.fragment.SearchFragment
import com.example.superphoto.ui.fragment.TemplatesFragment
import com.example.superphoto.ui.fragment.ToolsFragment


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentFragmentIndex = 0
    private var previousFragmentIndex = 0 // Track previous fragment before SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupBottomNavigation()
        setupClickListeners()

        // Load default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.Companion.newInstance(), 0)
        }
        
        // Listen for back stack changes để hiện lại header
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (currentFragment !is SearchFragment) {
                binding.headerLayout.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun setupClickListeners() {
        // Setup settings button
        binding.settingsIcon.setOnClickListener {
            // Handle settings click
        }
        
        // Setup search EditText focus and click - navigate to SearchFragment
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                openSearchFragment()
            }
        }
        
        binding.searchEditText.setOnClickListener {
            openSearchFragment()
        }
    }
    
    private fun openSearchFragment() {
        val searchText = binding.searchEditText.text.toString()
        val searchFragment = SearchFragment.newInstance(searchText)
        
        // Save current fragment index before switching to SearchFragment
        previousFragmentIndex = currentFragmentIndex
        
        // Use the same navigation system as other fragments
        loadFragment(searchFragment, 5) // Index 5 for SearchFragment
        
        // Hide header when opening SearchFragment
        binding.headerLayout.visibility = android.view.View.GONE
        
        // Clear focus from searchEditText
        binding.searchEditText.clearFocus()
        
        // Reset bottom navigation selection (no tab selected for search)
        updateBottomNavSelection(-1)
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
            0 -> HomeFragment.Companion.newInstance()
            1 -> TemplatesFragment.Companion.newInstance()
            2 -> CreateFragment.Companion.newInstance()
            3 -> ToolsFragment.Companion.newInstance()
            4 -> AssetsFragment.Companion.newInstance()
            5 -> SearchFragment.newInstance("") // SearchFragment with empty search
            else -> HomeFragment.Companion.newInstance()
        }

        loadFragment(fragment, index)
        updateBottomNavSelection(index)
        
        // Show header for all fragments except SearchFragment
        if (index == 5) {
            binding.headerLayout.visibility = android.view.View.GONE
        } else {
            binding.headerLayout.visibility = android.view.View.VISIBLE
        }
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
            val isSelected = i == selectedIndex && selectedIndex != -1

            // Update visual state based on selection
            item.alpha = if (isSelected) 1.0f else 0.6f

            // You can add more visual feedback here like changing colors
            // based on your design requirements
        }
    }
    
    // Public method for SearchFragment to return to previous fragment
    fun returnFromSearch() {
        // Return to the previous fragment that was active before SearchFragment
        selectBottomNavItem(previousFragmentIndex)
    }
}