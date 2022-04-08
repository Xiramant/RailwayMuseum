package com.omsk.railwaymuseum.ui.review

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.omsk.railwaymuseum.databinding.FragmentReviewAddingBinding
import com.omsk.railwaymuseum.ui.ProgressBarFragment
import com.omsk.railwaymuseum.util.*
import com.omsk.railwaymuseum.viewmodels.IMAGES_URI_SEPARATOR
import com.omsk.railwaymuseum.viewmodels.ProgressBarViewModel
import com.omsk.railwaymuseum.viewmodels.ReviewViewModel
import java.io.File
import java.util.*

class ReviewAddingFragment(val viewModel: ReviewViewModel) : BottomSheetDialogFragment() {

    private lateinit var imagesDirectory: File
    private lateinit var binding: FragmentReviewAddingBinding
    private val messageNicknameEmpty = "Заполните поле \"Имя Фамилия\"."
    private val messageReviewEmpty = "Заполните поле \"Текст отзыва\"."
    private val successfulToastText = "Отзыв успешно добавлен"
    private val reduceImageCountToastText = "Сократите количество изображений до 5"
    private val progressBarUploadImagesMessage = "Загрузка изображений..."
    private val progressBarUploadReviewTextMessage = "Загрузка текста отзыва..."
    private val successfulUploadImagesCountToastText = "Успешно загружено изображений: "

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentReviewAddingBinding.inflate(inflater)
        imagesDirectory = File("${requireActivity().applicationContext.filesDir}" +
                "${File.separator}${REVIEW_IMAGES_TAG}")

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ReviewAddingAdapter(ClickListenerImageFull {
            showFullscreenImage(this, it)},
            ClickListenerImageDelete {
                if(it.delete()) {
                    viewModel.setImageList(imagesDirectory)
                }
            })
        binding.reviewAddingImagesRecycler.adapter = adapter
        binding.reviewAddingImagesRecycler.setHasFixedSize(true)

        val progressBarViewModel = ProgressBarViewModel()
        val progressBarFragment = ProgressBarFragment(progressBarViewModel)
        progressBarFragment.isCancelable = false

        binding.reviewAddingInputLayoutNickname.setStartIconOnClickListener {
            hideKeyboard(binding.root)
        }
        binding.reviewAddingInputLayoutReview.setStartIconOnClickListener {
            hideKeyboard(binding.root)
        }

        //Показ EndIcon, если поле не пустое
        endIconShow(binding.reviewAddingInputLayoutNickname, binding.reviewAddingTextInputNickname)
        endIconShow(binding.reviewAddingInputLayoutReview, binding.reviewAddingTextInputReview)

        binding.reviewAddingInputLayoutNickname.setEndIconOnClickListener {
            binding.reviewAddingTextInputReview.requestFocus()
        }
        binding.reviewAddingInputLayoutReview.setEndIconOnClickListener {
            hideKeyboard(binding.root)
            viewFocus(binding.reviewAddingCamera)
        }

        binding.reviewAddingCamera.setOnClickListener {
            val intent = Intent(requireActivity(), ReviewCameraActivity::class.java)
            startActivity(intent)
        }

        binding.reviewAddingSend.setOnClickListener {
            if(emptinessCheck(binding.reviewAddingTextInputNickname, messageNicknameEmpty)) {
                return@setOnClickListener
            }
            if(emptinessCheck(binding.reviewAddingTextInputReview, messageReviewEmpty)) {
                return@setOnClickListener
            }
            viewModel.imageList.value?.let{
                if(it.size > 5) {
                    Toast.makeText(context, reduceImageCountToastText, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            progressBarFragment.show(parentFragmentManager, ProgressBarFragment.TAG)
            progressBarViewModel.setMessage(progressBarUploadImagesMessage)
            viewModel.uploadImageList()

        //здесь можно сделать обсервер successfullyUploadImagesList и errorUploadImages (но в начале
        // перевести их в LiveData) и устанавливать сообщение по состоянию загрузки изображений в progressBar
        }

        viewModel.isImageUpload.observe(viewLifecycleOwner, {
            if (it) {

                if(!viewModel.imageList.value.isNullOrEmpty()) {
                    val countSuccessfullyUploadImages = if(viewModel.successfullyUploadImagesList.isNullOrEmpty()) {
                        0
                    } else {
                        viewModel.successfullyUploadImagesList.size
                    }
                    Toast.makeText(context, "$successfulUploadImagesCountToastText$countSuccessfullyUploadImages " +
                            "из ${viewModel.imageList.value!!.size}", Toast.LENGTH_SHORT).show()
                }

                progressBarViewModel.setMessage(progressBarUploadReviewTextMessage)
                val successfullyUploadImagesString = if(viewModel.successfullyUploadImagesList.isNullOrEmpty()) {
                    ""
                } else {
                    viewModel.successfullyUploadImagesList.joinToString(separator = IMAGES_URI_SEPARATOR)
                }

                viewModel.insertReview(replaceApostrophe(binding.reviewAddingTextInputNickname),
                    replaceApostrophe(binding.reviewAddingTextInputReview),
                    successfullyUploadImagesString)

                viewModel.clearUploadParam()
                progressBarFragment.dismiss()
            }
        })

        viewModel.reviewInsert.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    Toast.makeText(context, successfulToastText, Toast.LENGTH_SHORT).show()
                    hideKeyboard(binding.root)
                    dismiss()
                    viewModel.getReview()
                }
            }
        })

        viewModel.errorDescription.observe(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.setImageList(imagesDirectory)
    }

    override fun onStop() {
        super.onStop()
        viewModel.hideToggle()
    }

    //Наличие апострофа в тексте приводит к ошибке его вставки в БД (на стороне сервера),
    //  поэтому он заменяется на `
    private fun replaceApostrophe(textView: TextView): String {
        return textView.text.toString().replace('\'', '`')
    }

    companion object {
        const val TAG = "reviewAddingFragment"
    }
}