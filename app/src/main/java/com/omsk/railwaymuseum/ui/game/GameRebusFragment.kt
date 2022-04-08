package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.databinding.FragmentGameRebusBinding
import com.omsk.railwaymuseum.util.emptinessCheck
import com.omsk.railwaymuseum.util.endIconShow
import com.omsk.railwaymuseum.util.setGameBackground
import com.omsk.railwaymuseum.util.showFullscreenImage
import com.omsk.railwaymuseum.viewmodels.GameQuestionsViewModel
import com.omsk.railwaymuseum.util.hideKeyboard


class GameRebusFragment : Fragment() {
    private val messageAnswerEmpty = "Напишите ответ"
    private val args: GameRebusFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameRebusBinding.inflate(inflater)
        setGameBackground(binding.gameRebusBackground)

        val viewModel = ViewModelProvider(this, GameQuestionsViewModel.Factory(args.game))
            .get(GameQuestionsViewModel::class.java)

        viewModel.moveToResult.observe(viewLifecycleOwner, {
            if(it) {
                binding.gameRebusChronometer.stop()
                val directions = GameRebusFragmentDirections
                    .actionGameRebusFragmentToGameResultFragment(
                        args.game,
                        viewModel.rightAnswerNumber.value!!,
                        binding.gameRebusChronometer.base)
                findNavController().navigate(directions)
                viewModel.reset()
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.gameRebusName.text = args.game.name

        binding.gameRebusAnswerLayout.setStartIconOnClickListener {
            hideKeyboard(binding.root)
        }

        endIconShow(binding.gameRebusAnswerLayout, binding.gameRebusAnswerTextInput)

        binding.gameRebusAnswerLayout.setEndIconOnClickListener {
            binding.gameRebusNext.callOnClick()
        }

        binding.gameRebusImage.setOnClickListener {
            showFullscreenImage(this, viewModel.currentGameQuestion.value!!.image)
        }

        binding.gameRebusScip.setOnClickListener {
            viewModel.setQuestion()
            binding.gameRebusAnswerTextInput.text?.clear()
        }

        binding.gameRebusNext.setOnClickListener {
            if(emptinessCheck(binding.gameRebusAnswerTextInput, messageAnswerEmpty)) {
                return@setOnClickListener
            }

            viewModel.rebusTestRightAnswer(binding.gameRebusAnswerTextInput.text.toString())
            binding.gameRebusAnswerTextInput.requestFocus()
            hideKeyboard(binding.root)
        }

        viewModel.wrongAnswerToast.observe(viewLifecycleOwner, {
            if(it) {
                Toast.makeText(context, TOAST_FAIL_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.rightAnswerNumber.observe(viewLifecycleOwner, {
            if (it != 0) {
                Toast.makeText(context, TOAST_SUCCESSFUL_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.rightAnswerNumber.observe(viewLifecycleOwner, {
            binding.gameRebusAnswerTextInput.text?.clear()
        })

        binding.gameRebusChronometer.start()

        return binding.root
    }

}