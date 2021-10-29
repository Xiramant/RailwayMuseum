package com.omsk.railwaymuseum.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.omsk.railwaymuseum.databinding.FragmentEventListBinding
import com.omsk.railwaymuseum.util.BASE_URL
import com.omsk.railwaymuseum.viewmodels.EventListViewModel

const val EVENT_REQUEST = "${BASE_URL}mobile.php?goal=event&id="

class EventListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentEventListBinding.inflate(inflater)

        val adapter = EventListAdapter(ClickListenerEventList {
            it.let {
                val directions = EventListFragmentDirections
                        .actionEventListFragmentToDetailPageFragment("$EVENT_REQUEST$it")
                findNavController().navigate(directions)
            }
        })

        val viewModel = ViewModelProvider(this, EventListViewModel.Factory()).get(EventListViewModel:: class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //данные связываются и передаются в recycler view через binding utils и xml

        binding.eventListRecycler.adapter = adapter

        return binding.root
    }
}