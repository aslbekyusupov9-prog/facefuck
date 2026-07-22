package com.aifacerating

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult

/**
 * Native MediaPipe Tasks Vision (478 3D Landmarks) Android Engine
 * Loads local face_landmarker.task from assets, executes on-device without internet.
 */
class MediaPipeAnalyzer(private val context: Context) {

    private var faceLandmarker: FaceLandmarker? = null

    init {
        setupFaceLandmarker()
    }

    private fun setupFaceLandmarker() {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("face_landmarker.task")
                .build()

            val options = FaceLandmarker.FaceLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinFaceDetectionConfidence(0.5f)
                .setMinFacePresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setNumFaces(1)
                .setRunningMode(RunningMode.IMAGE)
                .build()

            faceLandmarker = FaceLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Analyze Bitmap image on-device and return 478 3D landmarks & calculated scores
     */
    fun analyzeFaceBitmap(bitmap: Bitmap, gender: String = "male"): FaceScores {
        val mpImage = BitmapImageBuilder(bitmap).build()
        val result: FaceLandmarkerResult? = faceLandmarker?.detect(mpImage)

        val landmarksList = ArrayList<FaceLandmark>()

        if (result != null && result.faceLandmarks().isNotEmpty()) {
            val landmarks = result.faceLandmarks()[0]
            for (lm in landmarks) {
                landmarksList.add(FaceLandmark(lm.x(), lm.y(), lm.z()))
            }
        }

        // Calculate biometric scores using pure Kotlin FaceScoreCalculator
        return FaceScoreCalculator.calculateAllScores(landmarksList, bitmap, gender)
    }

    fun close() {
        faceLandmarker?.close()
        faceLandmarker = null
    }
}
