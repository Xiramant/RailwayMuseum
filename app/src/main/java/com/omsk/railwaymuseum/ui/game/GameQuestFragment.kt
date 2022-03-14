package com.omsk.railwaymuseum.ui.game

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blikoon.qrcodescanner.QrCodeActivity
import com.omsk.railwaymuseum.databinding.FragmentGameQuestBinding
import com.omsk.railwaymuseum.util.setGameBackground
import com.omsk.railwaymuseum.util.showFullscreenImage
import com.omsk.railwaymuseum.viewmodels.GameQuestionsViewModel


private const val TOAST_FAIL_MESSAGE = "Неверно. Попробуйте еще раз."
private const val TOAST_SUCCESSFUL_MESSAGE = "Верно!"

class GameQuestFragment : Fragment() {

    private val args: GameQuestFragmentArgs by navArgs()
    private val viewModel: GameQuestionsViewModel by lazy {  ViewModelProvider(this, GameQuestionsViewModel.Factory(args.game))
            .get(GameQuestionsViewModel::class.java)}
    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val qrCodeAnswer = result.data?.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult")
            qrCodeAnswer?.let {
                viewModel.questTestRightAnswer(it)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentGameQuestBinding.inflate(inflater)
        setGameBackground(binding.gameQuestBackground)

        viewModel.moveToResult.observe(viewLifecycleOwner, {
            if(it) {
                binding.gameQuestChronometer.stop()
                val directions = GameQuestFragmentDirections
                        .actionGameQuestFragmentToGameResultFragment(
                                args.game,
                                viewModel.rightAnswerNumber.value!!,
                                binding.gameQuestChronometer.base)
                findNavController().navigate(directions)
                viewModel.reset()
            }
        })

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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.gameQuestName.text = args.game.name

        binding.gameQuestImage.setOnClickListener {
            showFullscreenImage(this, viewModel.currentGameQuestion.value!!.image)
        }

        binding.gameQuestScip.setOnClickListener {
            viewModel.setQuestion()
        }

        binding.gameQuestScan.setOnClickListener {
            val intent = Intent(context, QrCodeActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.gameQuestChronometer.start()

        return binding.root
    }

}