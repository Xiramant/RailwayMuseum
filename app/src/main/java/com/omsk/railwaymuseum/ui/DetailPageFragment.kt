package com.omsk.railwaymuseum.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.databinding.FragmentDetailPageBinding
import com.omsk.railwaymuseum.util.setGameBackground

class DetailPageFragment : Fragment() {

    private val args: DetailPageFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentDetailPageBinding.inflate(inflater)

        setGameBackground(binding.pageDetailBackground)
        binding.pageDetailWebview.setBackgroundColor(Color.TRANSPARENT)
        binding.pageDetailWebview.loadUrl(args.detailPageRequest)

        return binding.root
    }
}