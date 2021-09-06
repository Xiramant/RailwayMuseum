package com.omsk.railwaymuseum.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.databinding.FragmentEventListBinding
import com.omsk.railwaymuseum.util.BASE_URL
import com.omsk.railwaymuseum.viewmodels.EventListViewModel

const val EVENT_REQUEST = "${BASE_URL}mobile.php?goal=event&id="

class EventListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentEventListBinding.inflate(inflater)

        val adapter = EventListAdapter(ClickListenerEventList { cardView, eventId ->
            eventId.let {
                val eventDetailTransitionName = getString(R.string.event_detail_transition_name)
                val extras = FragmentNavigatorExtras(cardView to eventDetailTransitionName)
                val directions = EventListFragmentDirections
                        .actionEventListFragmentToDetailPageFragment("$EVENT_REQUEST$eventId")
                findNavController().navigate(directions, extras)

                exitTransition = MaterialElevationScale(false).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
            }
        })

        val viewModel = ViewModelProvider(this, EventListViewModel.Factory()).get(EventListViewModel:: class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //данные связываются и передаются в recycler view через binding utils и xml

        binding.eventListRecycler.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}