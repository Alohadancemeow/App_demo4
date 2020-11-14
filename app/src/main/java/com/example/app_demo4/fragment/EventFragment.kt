package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_demo4.R
import com.example.app_demo4.adapter.EventAdapter
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // set event Menu
        val viewPager = viewPager_event
        viewPager.adapter = EventAdapter(childFragmentManager)
        val eventTab = tab_event
        eventTab.setupWithViewPager(viewPager)
    }

}