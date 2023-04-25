package com.omsk.railwaymuseum.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.databinding.FragmentDetailPageBinding
import com.omsk.railwaymuseum.util.isNightMode
import com.omsk.railwaymuseum.util.setGameBackground

//Переместить MODE_NIGHT в запрашивающие фрагменты HomeFrament и EventListFragment нельзя,
//т.к. при смене режима на странице DetailPageFragment не происходит изменения запрашиваемого url
//Можно дописать в конце запроса запрашивающих фрагментов нужный символ и потом обрабатывать строку,
//но лучше сделать через второй параметр
const val MODE_NIGHT = "mode=night"

class DetailPageFragment : Fragment() {

    private val args: DetailPageFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentDetailPageBinding.inflate(inflater)

        setGameBackground(binding.pageDetailBackground)
        binding.pageDetailWebview.setBackgroundColor(Color.TRANSPARENT)

        val url = if(isNightMode(binding.root.context)) {
            "${args.detailPageRequest}${args.nightModeSymbolRequest}$MODE_NIGHT"
        } else {
            args.detailPageRequest
        }

        binding.pageDetailWebview.loadUrl(url)

        return binding.root
    }
}