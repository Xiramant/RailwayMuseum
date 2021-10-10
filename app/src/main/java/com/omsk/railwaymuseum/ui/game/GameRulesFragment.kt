package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.databinding.FragmentGameRulesBinding
import com.omsk.railwaymuseum.viewmodels.GameRulesViewModel

class GameRulesFragment : Fragment() {

    private val args: GameRulesFragmentArgs by navArgs()
    private lateinit var binding : FragmentGameRulesBinding
    private val viewModel: GameRulesViewModel by lazy { ViewModelProvider(this, GameRulesViewModel.Factory(args.gameId)).get(GameRulesViewModel:: class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentGameRulesBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.response.observe(viewLifecycleOwner, {
            it?.let {
                if(it.comment.isNotEmpty()) {
                    binding.gameRulesCommentTitle.visibility = View.VISIBLE
                    binding.gameRulesCommentDescription.visibility = View.VISIBLE
                }
            }
        })

        val fade = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.gameRulesConstraintText.startAnimation(fade)

        val shoot = AnimationUtils.loadAnimation(context, R.anim.shoot)
        binding.gameRulesGo.startAnimation(shoot)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Перенести в BindingUtils не получается, т.к. функция требует передать 1 или 2 аргумента
        //  (в дополнение к аттрибуту), что в данном случае не требуется, а передавать аргумент без
        //  его использования не лучший вариант.
        Glide.with(view.context)
                .load(R.drawable.game_rules_background)
                .into(binding.gameRulesBackground)

        viewModel.response.observe(viewLifecycleOwner, {
            it?.let {
                val gameRulesCharacterAnim = when(it.type) {
                    view.context.getString(R.string.game_type_quiz) -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_quiz)
                    view.context.getString(R.string.game_type_quest) -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_quest)
                    view.context.getString(R.string.game_type_fragment) -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_fragment)
                    else -> AnimationUtils.loadAnimation(context, R.anim.translate_game_rules_character_quiz)
                }
                binding.gameRulesCharacter.startAnimation(gameRulesCharacterAnim)
            }
        })
    }
}