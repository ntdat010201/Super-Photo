package com.example.superphoto.domain.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.superphoto.domain.ui.fragment.HomeFragment
import com.example.superphoto.domain.ui.fragment.MineFragment

class ViewpagerActivityAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var homeFragment: HomeFragment? = null
    private var mineFragment: MineFragment? = null


    fun setFragments(
        homeFragment: HomeFragment,
        mineFragment: MineFragment,

        ) {
        this.homeFragment = homeFragment
        this.mineFragment = mineFragment

    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> homeFragment!!
            1 -> mineFragment!!
            else -> homeFragment!!
        }

    }
}