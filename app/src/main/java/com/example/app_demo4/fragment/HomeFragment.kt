package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_demo4.R
import com.example.app_demo4.adapter.HomeRecyclerviewAdapter
import com.example.app_demo4.model.HomeData
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    // Property:
    lateinit var eventCardList: ArrayList<HomeData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initDataCard()
        setRecyclerView()
    }

    private fun initDataCard() {

        eventCardList = ArrayList()

        eventCardList.add(
            HomeData(
            "Event A",
            "Eat Lunch",
            "Inside of temple",
            "10:30AM - 12:00PM")
        )
        eventCardList.add(HomeData(
            "Event B",
            "Eat Lunch",
            "Outside of temple",
            "10:30AM - 12:00PM")
        )
        eventCardList.add(HomeData(
            "Event A",
            "Eat Lunch",
            "Inside of temple",
            "10:30AM - 12:00PM")
        )
        eventCardList.add(HomeData(
            "Event B",
            "Eat Lunch",
            "Outside of temple",
            "10:30AM - 12:00PM")
        )
        eventCardList.add(HomeData(
            "Event A",
            "Eat Lunch",
            "Inside of temple",
            "10:30AM - 12:00PM")
        )
    }

    private fun setRecyclerView() {
        val homeCardAdapter = HomeRecyclerviewAdapter(eventCardList)
        rv_home.adapter = homeCardAdapter
        rv_home.setHasFixedSize(true)
    }
}