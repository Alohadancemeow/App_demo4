package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_demo4.R
import com.example.app_demo4.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_user.*


class UsersFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        // set User menu
        val viewPager = viewPager_user
        viewPager.adapter = UserAdapter(childFragmentManager)  // send child fragment to adapter
        val tab = tab_user
        tab.setupWithViewPager(viewPager)



        // show child fragment
//        childFragmentManager.beginTransaction().apply {
//            add(R.id.frame_layout_child_frag, user1Fragment)
//            commit()
//        }


    }


}


