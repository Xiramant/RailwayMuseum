package com.omsk.railwaymuseum.ui.review

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val IMMERSIVE_FLAG_TIMEOUT = 500L

class ReviewCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewCameraBinding
    private lateinit var imagesDirectory: File

    private var fotoapparat: Fotoapparat? = null
    private var fotoapparatState : FotoapparatState? = null
    private var cameraStatus : CameraState? = null
    private var flashState: FlashState? = null
    private var photoFile: File? = null

    private val permissions = arrayOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagesDirectory = File("${applicationContext.filesDir}/${REVIEW_IMAGES_TAG}")
        imagesDirectory.mkdirs()

        createFotoapparat()
        setInitialConditions()

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
            }
        )
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
            photoFile = createFile(imagesDirectory)
            photoFile?.let {
                fotoapparat
                    ?.takePicture()
                    ?.saveToFile(it)
            }
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

    private fun hasNoPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
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

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        const val REVIEW_IMAGES_TAG = "review-images"

        //Helper function used to create a timestamped file
        private fun createFile(baseFolder: File) =
            File(baseFolder, SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + PHOTO_EXTENSION)
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