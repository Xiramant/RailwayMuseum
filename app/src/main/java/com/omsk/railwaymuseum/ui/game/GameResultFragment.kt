package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.data.GameType.*
import com.omsk.railwaymuseum.databinding.FragmentGameResultBinding
import com.omsk.railwaymuseum.util.isNightMode
import com.omsk.railwaymuseum.util.setGameBackground

class GameResultFragment : Fragment() {

    private val args: GameResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentGameResultBinding.inflate(inflater)
        setGameBackground(binding.gameResultBackground)

        val image = if (!isNightMode(binding.gameResultCharacter.context)) {
            R.drawable.game_character_good
        } else {
            R.drawable.game_character_good_night
        }
        Glide.with(binding.gameResultCharacter.context)
                .load(image)
                .into(binding.gameResultCharacter)
        binding.gameResultName.text = args.game.name
        binding.gameResultNumber.text = binding.gameResultNumber.context.getString(R.string.game_comparison, args.gameResultNumber, args.game.questionsNumber)
        binding.gameResultTime.base = args.gameTime

        binding.gameRulesGameList.setOnClickListener {
            val directions = GameResultFragmentDirections.actionGameResultFragmentToGameListFragment()
            findNavController().navigate(directions)
        }
        binding.gameRulesReplay.setOnClickListener {
            val directions = when(args.game.type) {
                QUIZ -> GameResultFragmentDirections.actionGameResultFragmentToGameQuizFragment(args.game)
                REBUS -> GameResultFragmentDirections.actionGameResultFragmentToGameRebusFragment(args.game)
                else -> GameResultFragmentDirections.actionGameResultFragmentToGameQuestFragment(args.game)
            }
            findNavController().navigate(directions)
        }

        return binding.root
    }
}