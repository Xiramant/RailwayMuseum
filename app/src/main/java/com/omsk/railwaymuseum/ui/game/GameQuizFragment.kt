package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.databinding.FragmentGameQuizBinding
import com.omsk.railwaymuseum.util.setGameBackground
import com.omsk.railwaymuseum.viewmodels.GameQuestionsViewModel


class GameQuizFragment : Fragment() {

    private val args: GameQuizFragmentArgs by navArgs()
    private val toastMessage = "Выберите вариант ответа"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentGameQuizBinding.inflate(inflater)
        setGameBackground(binding.gameQuizBackground)

        val viewModel = ViewModelProvider(this, GameQuestionsViewModel.Factory(args.game))
                .get(GameQuestionsViewModel::class.java)

        viewModel.moveToResult.observe(viewLifecycleOwner, {
            if(it) {
                binding.gameQuizChronometer.stop()
                val directions = GameQuizFragmentDirections
                        .actionGameQuizFragmentToGameResultFragment(
                                args.game,
                                viewModel.rightAnswerNumber.value!!,
                                binding.gameQuizChronometer.base)
                findNavController().navigate(directions)
                viewModel.reset()
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.gameQuizName.text = args.game.name

        binding.gameQuizNext.setOnClickListener {
            val checkedId = binding.gameQuizRadioGroup.checkedRadioButtonId
            if (checkedId == -1) {
                Toast.makeText(binding.gameQuizRadioGroup.context,
                        toastMessage,
                        Toast.LENGTH_SHORT).show()
            } else {
                val selectAnswerId = when(checkedId) {
                    R.id.game_quiz_radio_button_0 -> 0
                    R.id.game_quiz_radio_button_1 -> 1
                    R.id.game_quiz_radio_button_2 -> 2
                    R.id.game_quiz_radio_button_3 -> 3
                    R.id.game_quiz_radio_button_4 -> 4
                    R.id.game_quiz_radio_button_5 -> 5
                    else -> -1
                }
                viewModel.testRightAnswer(selectAnswerId)

                binding.gameQuizRadioGroup.clearCheck()
                viewModel.setQuestion()
                binding.gameQuizScroll.scrollTo(0, 0)
            }
        }

        binding.gameQuizChronometer.start()

        return binding.root
    }
}