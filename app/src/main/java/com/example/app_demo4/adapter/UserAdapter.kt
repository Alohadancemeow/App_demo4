package com.example.app_demo4.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.app_demo4.fragment.User1Fragment
import com.example.app_demo4.fragment.User2Fragment

class UserAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> User1Fragment()
            1 -> User2Fragment()
            else -> User1Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val pagerTitle = arrayOf("User1", "User2")
        return pagerTitle[position]
    }


}