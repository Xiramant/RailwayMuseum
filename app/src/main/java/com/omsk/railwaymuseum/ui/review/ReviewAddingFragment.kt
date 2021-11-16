package com.omsk.railwaymuseum.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.omsk.railwaymuseum.databinding.FragmentReviewAddingBinding
import com.omsk.railwaymuseum.ui.review.ReviewCameraActivity.Companion.REVIEW_IMAGES_TAG
import com.omsk.railwaymuseum.util.showFullscreenImage
import com.omsk.railwaymuseum.viewmodels.ReviewViewModel
import com.omsk.railwaymuseum.viewmodels.SUCCESSFUL_RESPONSE
import java.io.File
import java.util.*

val EXTENSION_WHITELIST = arrayOf("JPG", "PNG", "GIF")

class ReviewAddingFragment(val viewModel: ReviewViewModel) : BottomSheetDialogFragment() {

    private lateinit var imagesDirectory: File
    private lateinit var binding: FragmentReviewAddingBinding
    private val messageNicknameEmpty = "Заполните поле \"Имя Фамилия\"."
    private val messageReviewEmpty = "Заполните поле \"Текст отзыва\"."

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentReviewAddingBinding.inflate(inflater)
        imagesDirectory = File("${requireActivity().applicationContext.filesDir}/${REVIEW_IMAGES_TAG}")

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ReviewAddingAdapter(ClickListenerImageFull {
            showFullscreenImage(this, it)},
        ClickListenerImageDelete {
            if(it.delete()) {
                viewModel.setImageList(imagesDirectory)
            }
        })
        binding.reviewImagesRecycler.adapter = adapter
        binding.reviewImagesRecycler.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialConditions(view)

        binding.reviewAddingInputLayoutNickname.setStartIconOnClickListener {
            hideKeyboard(it)
        }
        binding.reviewAddingInputLayoutReview.setStartIconOnClickListener {
            hideKeyboard(it)
        }

        //Показ EndIcon, если поле не пустое
        binding.reviewAddingInputLayoutNickname.addOnEditTextAttachedListener {nicknameLayout ->
            nicknameLayout.isEndIconVisible = !binding.reviewAddingTextInputNickname.text.isNullOrEmpty()
            binding.reviewAddingTextInputNickname.addTextChangedListener {nicknameText ->
                nicknameLayout.isEndIconVisible = !nicknameText.isNullOrEmpty()
            }
        }
        binding.reviewAddingInputLayoutReview.addOnEditTextAttachedListener {reviewLayout ->
            reviewLayout.isEndIconVisible = !binding.reviewAddingTextInputReview.text.isNullOrEmpty()
            binding.reviewAddingTextInputReview.addTextChangedListener {reviewText ->
                reviewLayout.isEndIconVisible = !reviewText.isNullOrEmpty()
            }
        }

        binding.reviewAddingInputLayoutNickname.setEndIconOnClickListener {
            binding.reviewAddingTextInputReview.requestFocus()
        }
        binding.reviewAddingInputLayoutReview.setEndIconOnClickListener {
            hideKeyboard(view)
            binding.reviewAddingCamera.isFocusable = true
            binding.reviewAddingCamera.isFocusableInTouchMode = true
            binding.reviewAddingCamera.requestFocus()
        }

        binding.reviewAddingSend.setOnClickListener {
            if(emptynessCheck(binding.reviewAddingTextInputNickname, messageNicknameEmpty)) {
                return@setOnClickListener
            }
            if(emptynessCheck(binding.reviewAddingTextInputReview, messageReviewEmpty)) {
                return@setOnClickListener
            }

            viewModel.insertReview(replaceApostrophe(binding.reviewAddingTextInputNickname),
                    replaceApostrophe(binding.reviewAddingTextInputReview))
        }

        binding.reviewAddingCamera.setOnClickListener {
            val intent = Intent(requireActivity(), ReviewCameraActivity::class.java)
            startActivity(intent)
        }

        viewModel.reviewInsert.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    Toast.makeText(context, SUCCESSFUL_RESPONSE, Toast.LENGTH_SHORT).show()
                    hideKeyboard(view)
                    dismiss()
                    viewModel.getReview()
                }
            }
        })

        viewModel.errorDescription.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setImageList(imagesDirectory)
    }

    //Установка начальных значений: показ клавиатуры, установка фокуса в поле ввода ФИО
    private fun setInitialConditions(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        binding.reviewAddingInputLayoutNickname.requestFocus()
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun emptynessCheck(textView: TextView, message: String): Boolean {
        if(!textView.text.isNullOrEmpty()) {
            return false
        }

        Toast.makeText(textView.context, message, Toast.LENGTH_SHORT).show()
        textView.requestFocus()
        return true
    }

    //Наличие апострофа в тексте приводит к ошибке его вставки в БД (на стороне сервера),
    //  поэтому он заменяется на `
    private fun replaceApostrophe(textView: TextView): String {
        return textView.text.toString().replace('\'', '`')
    }
}