package com.example.app_demo4.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.app_demo4.fragment.Event1Fragment
import com.example.app_demo4.fragment.Event2Fragment
import com.example.app_demo4.fragment.User1Fragment
import com.example.app_demo4.fragment.User2Fragment

class EventAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> Event1Fragment()
            1 -> Event2Fragment()
            else -> Event1Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val pagerTitle = arrayOf("General", "Special")
        return pagerTitle[position]
    }


}