package com.example.superphoto.domain.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.superphoto.R
import com.example.superphoto.databinding.ActivityMainBinding
import com.example.superphoto.domain.adapter.ViewpagerActivityAdapter
import com.example.superphoto.domain.base.BaseActivity
import com.example.superphoto.domain.ui.fragment.HomeFragment
import com.example.superphoto.domain.ui.fragment.MineFragment
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private var adapter: ViewpagerActivityAdapter? = null
    private val homeFragment by inject<HomeFragment>()
    private val soundFragment by inject<MineFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        refreshViewPager()
        viewPagerWithNav()

    }
    private fun initView() {
    }

    private fun initListener() {

    }


    private fun refreshViewPager() {
        adapter = ViewpagerActivityAdapter(this)
        adapter!!.setFragments(
            homeFragment, soundFragment,
        )
        binding.viewPager2.adapter = adapter
        binding.viewPager2.offscreenPageLimit = 2
        binding.viewPager2.isUserInputEnabled = false
    }

    private fun viewPagerWithNav() {
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true
                    }

                    1 -> {
                        binding.bottomNavigation.menu.findItem(R.id.nav_mine).isChecked = true
                    }

                    else -> {
                        binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true
                    }
                }
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    binding.viewPager2.currentItem = 0
                    true
                }

                R.id.nav_mine -> {
                    binding.viewPager2.currentItem = 1
                    true
                }

                else -> false
            }
        }
    }

}