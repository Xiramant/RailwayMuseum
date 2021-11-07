package com.omsk.railwaymuseum.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.omsk.railwaymuseum.databinding.FragmentReviewBinding
import com.omsk.railwaymuseum.viewmodels.ReviewViewModel

class ReviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentReviewBinding.inflate(inflater)

        val viewModel = ViewModelProvider(this, ReviewViewModel.Factory()).get(ReviewViewModel:: class.java)

        val adapter = ReviewAdapter(ClickListenerReviewDetail {
//            it.let {
//            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.reviewListRecycler.adapter = adapter

        binding.reviewFab.setOnClickListener {
            val bottomDrawer = ReviewAddingFragment(viewModel)
            bottomDrawer.show(parentFragmentManager, bottomDrawer.tag)
        }

        return binding.root
    }
}