package com.omsk.railwaymuseum.ui.review

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.omsk.railwaymuseum.databinding.FragmentReviewBinding
import com.omsk.railwaymuseum.viewmodels.ReviewViewModel

class ReviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentReviewBinding.inflate(inflater)
        val viewModel = ViewModelProvider(this, ReviewViewModel.Factory())
                                                        .get(ReviewViewModel:: class.java)

        val adapter = ReviewAdapter(ClickListenerReviewDetail {
//            it.let {
//            }
        }, this)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.reviewListRecycler.adapter = adapter
        binding.reviewListRecycler.setHasFixedSize(true)

        binding.reviewFab.setOnClickListener {
            val bottomDrawer = ReviewAddingFragment(viewModel)
            bottomDrawer.show(parentFragmentManager, ReviewAddingFragment.TAG)
        }

        viewModel.hide.observe(viewLifecycleOwner, {
            val imm = binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        })

        return binding.root
    }
}