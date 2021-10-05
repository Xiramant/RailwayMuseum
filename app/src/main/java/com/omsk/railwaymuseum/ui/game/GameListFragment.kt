package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.omsk.railwaymuseum.databinding.FragmentGameListBinding
import com.omsk.railwaymuseum.viewmodels.GameListViewModel

class GameListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentGameListBinding.inflate(inflater)
        val viewModel = ViewModelProvider(this, GameListViewModel.Factory()).get(GameListViewModel:: class.java)

        val adapter = GameListAdapter(ClickListenerGameList { gameId ->
            gameId.let {
                val directions = GameListFragmentDirections.actionGameListFragmentToGameRulesFragment(it)
                findNavController().navigate(directions)
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.gameListRecycler.adapter = adapter

        return binding.root
    }
}