package com.omsk.railwaymuseum.ui.review

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.omsk.railwaymuseum.databinding.ActivityReviewCameraBinding
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.Resolution
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val REVIEW_IMAGES_TAG = "review-images"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L
private const val FILENAME_FORMAT = "yyyy-MM-dd_HH-mm-ss_SSS"
private const val PHOTO_EXTENSION = ".jpg"

class ReviewCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewCameraBinding
    private lateinit var imagesDirectory: File

    private var fotoapparat: Fotoapparat? = null
    private var fotoapparatState : FotoapparatState? = null
    private var cameraStatus : CameraState? = null
    private var flashState: FlashState? = null
    private var photoFile: File? = null
    private val permissions = arrayOf(Manifest.permission.CAMERA)
    private val cameraResolution = Resolution(1280, 960)
    private val cameraQuality = 80

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagesDirectory = File("${applicationContext.filesDir}${File.separator}${REVIEW_IMAGES_TAG}")
        imagesDirectory.mkdirs()

        createFotoapparat()
        setInitialConditions()

        binding.cameraBack.setOnClickListener {
            onBackPressed()
        }
        binding.fabCamera.setOnClickListener {
            takePhoto()
        }
        binding.fabSwitchCamera.setOnClickListener {
            switchCamera()
        }
        binding.fabFlash.setOnClickListener {
            changeFlashState()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        if (hasNoPermissions()) {
            requestPermission()
        }else{
            fotoapparat?.start()
            fotoapparatState = FotoapparatState.ON
        }
    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        binding.cameraLayout.postDelayed({
            hideSystemUI()
        }, IMMERSIVE_FLAG_TIMEOUT)

        if(!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF){
            val intent = Intent(baseContext, ReviewCameraActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

    private fun setInitialConditions() {
        cameraStatus = CameraState.BACK
        flashState = FlashState.OFF
        fotoapparatState = FotoapparatState.OFF
    }

    private fun createFotoapparat(){
        fotoapparat = Fotoapparat(
            context = this,
            view = binding.cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(
                logcat()
            ),
            cameraErrorCallback = { error ->
                println("Recorder errors: $error")
            },
            cameraConfiguration = CameraConfiguration(
                pictureResolution = { nearestBy(cameraResolution, Resolution::area) },
                //Сжатие изображения производится в секции добавления отзыва с помощью библиотеки compressor
                jpegQuality = manualJpegQuality(cameraQuality)
            )
        )
    }

    private inline fun <T> Iterable<T>.nearestBy(ofValue: T, selector: (T) -> Int): T? {
        val iterator = iterator()
        if (!iterator.hasNext()) return null
        val valueToCompare = selector(ofValue)
        var nearestElem = iterator.next()
        var nearestRange = abs(selector(nearestElem) - valueToCompare)
        var currentRange: Int
        while (iterator.hasNext()) {
            val e = iterator.next()
            val v = selector(e)
            currentRange = abs(v - valueToCompare)
            if (currentRange < nearestRange) {
                nearestElem = e
                nearestRange = currentRange
            }
        }
        return nearestElem
    }

    private fun changeFlashState() {
        fotoapparat?.updateConfiguration(
            CameraConfiguration(
                flashMode = if(flashState == FlashState.TORCH) off() else torch()
            )
        )

        flashState = if(flashState == FlashState.TORCH) FlashState.OFF else FlashState.TORCH
    }

    private fun switchCamera() {
        fotoapparat?.switchTo(
            lensPosition =  if (cameraStatus == CameraState.BACK) front() else back(),
            cameraConfiguration = CameraConfiguration()
        )

        cameraStatus = if(cameraStatus == CameraState.BACK) CameraState.FRONT else CameraState.BACK
    }

    private fun takePhoto() {
        if (hasNoPermissions()) {
            val permissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, permissions,0)
        }else{
            photoFile = createFile()
            photoFile?.let {
                fotoapparat
                    ?.takePicture()
                    ?.saveToFile(it)
            }
        }
    }

    //Helper function used to create a timestamped file
    private fun createFile() =
        File(imagesDirectory, "${SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())}$PHOTO_EXTENSION")

    private fun hasNoPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

enum class CameraState{
    FRONT, BACK
}

enum class FlashState{
    TORCH, OFF
}

enum class FotoapparatState{
    ON, OFF
}