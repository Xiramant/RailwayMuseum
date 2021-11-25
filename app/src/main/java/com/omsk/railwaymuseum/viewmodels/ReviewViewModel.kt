package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.review.ReviewApi
import com.omsk.railwaymuseum.net.review.ReviewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

const val SUCCESSFUL_RESPONSE = "Success"
const val UNSUCCESSFUL_RESULT = "Data transmission error."
const val UNSUCCESSFUL_UPLOAD_IMAGE_RESULT = "Image transmission error."
const val IMAGES_URI_SEPARATOR = ";"
val EXTENSION_WHITELIST = arrayOf("JPG", "PNG")

class ReviewViewModel: ViewModel() {

    //Переменная со списком всех отзывов.
    private val _response = MutableLiveData<List<ReviewModel>>()
    val response: LiveData<List<ReviewModel>>
        get() = _response

    //Переменная описания ошибки при добавлении отзыва на сервер.
    private val _errorDescription = MutableLiveData<String>()
    val errorDescription: LiveData<String>
        get() = _errorDescription

    //Переменная для отслеживания (успешности) добавления отзыва и обновлении листа отзывов,
    //  если отзыв был успешно загружен на сервер.
    private val _reviewInsert = MutableLiveData(false)
    val reviewInsert: LiveData<Boolean>
        get() = _reviewInsert

    //Переменная списка файлов изображений добавления отзыва.
    private val _imageList = MutableLiveData<List<File>>()
    val imageList: LiveData<List<File>>
        get() = _imageList

    //Переменная для скрытия клавиатуры при выходе из модального окна добавления отзыва к фрагменту
    //  просмотра отзывов.
    //Имплеметации кода скрытия клавиатуры в окне добавления отзыва (в методе OnStop или других
    //  методах жизненного цикла) не срабатывает, поэтому введена переменная, которая изменяется
    //  при закрытии модального окна добавления отзыва (в методе OnStop). В фрагменте просмотра
    //  отзывов поставлен наблюдатель, который отслеживает изменение переменной и при любом изменении
    //  скрывает клавиатуру.
    private val _hide = MutableLiveData(false)
    val hide: LiveData<Boolean>
        get() = _hide

    //Потокобезопасный счетчик загрузки файлов добавления отзыва на сервер и логическая переменная
    //  отслеживания завершения загрузки файлов на сервер.
    //Завернуть AtomicInteger в LiveData не выходит, т.к. объект AtomicInteger остается неизменным
    //  (меняются только значения в нем), поэтому сформировать необходимый наблюдатель на LiveData не получается.
    //При отправке отзыва на сервер значение счетчика принимается за количество файлов, загружаемых на сервер.
    //  При успешном (или неуспешном) результате загрузки фотографий счетчик снижается и при
    //  достижении 0 LiveData переменной isImageUpload устанавливается в true. В наблюдателе этого
    //  события запускается запись текста отзыва (ФИО, текст, список изображений) в БД сервера.
    private val countDownloadLeft = AtomicInteger(-1)
    private val _isImageUpload = MutableLiveData(false)
    val isImageUpload: LiveData<Boolean>
        get() = _isImageUpload

    val successfullyUploadImagesList = mutableListOf<String>()
    private val errorUploadImages = mutableListOf<String>()

    init {
        getReview()
    }

    fun getReview() {
        viewModelScope.launch {
            try {
                val responseTemp = ReviewApi.retrofitService.getReviewApi()
                responseTemp.forEach {
                    if(it.imagesUriUnion.isNotEmpty()) {
                        it.imagesUriList = it.imagesUriUnion.split(IMAGES_URI_SEPARATOR)
                    }
                }
                _response.value = responseTemp
                _reviewInsert.value = false
            } catch (e: Exception) {
                _response.value = ArrayList()
            }
        }
    }

    fun insertReview(name: String, review: String, androidId: String, images: String) {
        viewModelScope.launch {
            try {
                val response = ReviewApi.retrofitInsertService.insertReviewApi(name, review, androidId, images)
                if (response == SUCCESSFUL_RESPONSE) {
                    _reviewInsert.value = true
                } else {
                    _errorDescription.value = response
                }
            } catch (e: Exception) {
                _errorDescription.value = UNSUCCESSFUL_RESULT
            }
        }
    }

    fun setImageList(imagesDirectory: File) {
        if(imagesDirectory.exists()) {
            _imageList.value = imagesDirectory.listFiles { file ->
                EXTENSION_WHITELIST.contains(file.extension.uppercase())
            }?.toList()
        }
    }

    fun uploadImageList() {
        if(imageList.value.isNullOrEmpty()) {
            _isImageUpload.value = true
            return
        }

        imageList.value?.let {
            countDownloadLeft.set(it.size)
            it.forEach {file ->
                uploadReviewFile(file)
            }
        }
    }

    private fun uploadReviewFile(file: File) {
        // Parsing any Media type file
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val filename: RequestBody = RequestBody.create(MediaType.parse("text/plain"), file.name)

        viewModelScope.launch {
            try {
                val response = ReviewApi.retrofitUploadService.uploadFileApi(fileToUpload, filename)
                if (response.endsWith(SUCCESSFUL_RESPONSE)) {
                    successfullyUploadImagesList.add(response.removeSuffix(SUCCESSFUL_RESPONSE))
                } else {
                    errorUploadImages.add("$file: $response")
                }
            } catch (e: Exception) {
                errorUploadImages.add("$file: $UNSUCCESSFUL_UPLOAD_IMAGE_RESULT")
            }

            if (countDownloadLeft.decrementAndGet() == 0) {
                _isImageUpload.value = true
            }
        }
    }

    fun clearUploadParam() {
        successfullyUploadImagesList.clear()
        errorUploadImages.clear()
        countDownloadLeft.set(-1)
        _isImageUpload.value = false
        deleteReviewAddingImages()
    }

    private fun deleteReviewAddingImages() {
        imageList.value?.let {list ->
            list.forEach {file ->
                file.delete()
            }
        }
    }

    fun hideToggle() {
        _hide.value = !_hide.value!!
    }



    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReviewViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct ReviewViewModel")
        }
    }
}