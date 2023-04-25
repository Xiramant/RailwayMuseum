package com.omsk.railwaymuseum.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.databinding.FragmentEventListBinding
import com.omsk.railwaymuseum.util.PHP_URL
import com.omsk.railwaymuseum.util.isNightMode
import com.omsk.railwaymuseum.viewmodels.EventListViewModel

const val DETAIL_REQUEST = "${PHP_URL}section_id.php?id="
const val MODE_NIGHT_SYMBOL_REQUEST = "&"

class EventListFragment : Fragment() {

    private val args: EventListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentEventListBinding.inflate(inflater)

        val adapter = EventListAdapter(ClickListenerEventList {
            it.let {
                val directions = EventListFragmentDirections
                        .actionEventListFragmentToDetailPageFragment("$DETAIL_REQUEST$it", MODE_NIGHT_SYMBOL_REQUEST)
                findNavController().navigate(directions)
            }
        })

        val viewModel = ViewModelProvider(this, EventListViewModel.Factory(args.sectionNetGoalType))
                .get(EventListViewModel:: class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //данные связываются и передаются в recycler view через binding utils и xml

        binding.eventListRecycler.adapter = adapter
        binding.eventListRecycler.setHasFixedSize(true)

        return binding.root
    }
}