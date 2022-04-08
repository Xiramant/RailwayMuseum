package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.data.GameType.*
import com.omsk.railwaymuseum.databinding.FragmentGameRulesBinding
import com.omsk.railwaymuseum.net.game.getEmptyGameRulesModel
import com.omsk.railwaymuseum.util.setGameBackground
import com.omsk.railwaymuseum.viewmodels.GameRulesViewModel

class GameRulesFragment : Fragment() {

    private val args: GameRulesFragmentArgs by navArgs()
    private lateinit var binding : FragmentGameRulesBinding
    private val viewModel: GameRulesViewModel by lazy { ViewModelProvider(this, GameRulesViewModel.Factory(args.gameId)).get(GameRulesViewModel:: class.java) }
    private val errorMessage = "Не удалось загрузить данные с сервера"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentGameRulesBinding.inflate(inflater)
        setGameBackground(binding.gameRulesBackground)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.response.observe(viewLifecycleOwner, {
            it?.let {
                if(it == getEmptyGameRulesModel()) {
                    Toast.makeText(binding.root.context,
                        errorMessage,
                        Toast.LENGTH_SHORT).show()
                }

                if(it.comment.isNotEmpty()) {
                    binding.gameRulesCommentTitle.visibility = VISIBLE
                    binding.gameRulesCommentDescription.visibility = VISIBLE
                }
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.response.observe(viewLifecycleOwner, {
            it?.let {

                val fade = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                binding.gameRulesLayoutText.startAnimation(fade)
                binding.gameRulesLayoutText.visibility = VISIBLE

                val shoot = AnimationUtils.loadAnimation(context, R.anim.shoot)
                binding.gameRulesGo.startAnimation(shoot)
                binding.gameRulesGo.visibility = VISIBLE

                val gameRulesCharacterAnim = when(it.type) {
                    QUIZ, REBUS -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_quiz)
                    QUEST -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_quest)
                    FRAGMENT -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_fragment)
                    else -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_quiz)
                }
                binding.gameRulesCharacter.startAnimation(gameRulesCharacterAnim)
                binding.gameRulesCharacter.visibility = VISIBLE

                binding.gameRulesGo.setOnClickListener {view ->
                    val directions = when(it.type) {
                        QUIZ ->
                            GameRulesFragmentDirections.actionGameRulesFragmentToGameQuizFragment(it)
                        REBUS -> GameRulesFragmentDirections.actionGameRulesFragmentToGameRebusFragment(it)
                        else -> GameRulesFragmentDirections.actionGameRulesFragmentToGameQuestFragment(it)
                    }

                    findNavController().navigate(directions)
                }
            }
        })
    }
}