package com.example.app_demo4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo4.R
import com.example.app_demo4.adapter.EventRecyclerviewAdapter
import com.example.app_demo4.model.EventData
import kotlinx.android.synthetic.main.fragment_event1.*
import kotlinx.android.synthetic.main.fragment_user1.*

class Event1Fragment : Fragment() {

    //
    private lateinit var rv: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: EventRecyclerviewAdapter

    // content :
    private lateinit var content: ArrayList<EventData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initContent()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv = rv_event
        layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        adapter = EventRecyclerviewAdapter(content)

        rv.layoutManager = layoutManager
        rv.adapter = adapter

        // add line
//        rv.addItemDecoration(DividerItemDecoration(rv.context,layoutManager.orientation))

        // from interface
        adapter.setOnItemClickListener(object : EventRecyclerviewAdapter.OnItemClickListener {
            override fun setOnClickListener(adapterPosition: Int) {
                Toast.makeText(context, "${content[adapterPosition].eventName} is clicked",
                    Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initContent() {
        content = ArrayList()
        content.add(EventData(content.size + 1, "Event1", "At the temple"))
        content.add(EventData(content.size + 1, "Event2", "At the temple"))
        content.add(EventData(content.size + 1, "Event3", "At the temple"))
        content.add(EventData(content.size + 1, "Event4", "At the temple"))
        content.add(EventData(content.size + 1, "Event5", "At the temple"))
        content.add(EventData(content.size + 1, "Event6", "At the temple"))
        content.add(EventData(content.size + 1, "Event7", "At the temple"))
        content.add(EventData(content.size + 1, "Event8", "At the temple"))
        content.add(EventData(content.size + 1, "Event9", "At the temple"))
        content.add(EventData(content.size + 1, "Event10", "At the temple"))
    }

}