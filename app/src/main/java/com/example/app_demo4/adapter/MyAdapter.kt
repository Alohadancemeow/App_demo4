package com.example.app_demo4.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.app_demo4.fragment.EventFragment
import com.example.app_demo4.fragment.HomeFragment
import com.example.app_demo4.fragment.UsersFragment

class MyAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> HomeFragment()
            1 -> UsersFragment()
            2 -> EventFragment()
            else -> HomeFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val pageTitle = arrayOf("Home", "User", "Event")
        return pageTitle[position]
    }
}