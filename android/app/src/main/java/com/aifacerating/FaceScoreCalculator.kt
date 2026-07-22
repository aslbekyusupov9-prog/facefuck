package com.aifacerating

import android.graphics.Bitmap
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class FaceScores(
    val overall: Int,
    val potential: Int,
    val skinQuality: Int,
    val jawline: Int,
    val cheekbones: Int,
    val symmetry: Int,
    val genderScore: Int
)

/**
 * Pure Kotlin On-Device Biometric Facial Analysis Engine
 * Calculates 0-100 scores using 478 MediaPipe 3D face landmarks and geometry formulas.
 */
object FaceScoreCalculator {

    // Landmark Indices based on MediaPipe Face Mesh (478 Points Model)
    private const val NOSE_BRIDGE = 6
    private const val NOSE_TIP = 1
    private const val CHIN_TIP = 152
    private const val FOREHEAD_TOP = 10
    
    private const val LEFT_EYE_OUTER = 33
    private const val LEFT_EYE_INNER = 133
    private const val RIGHT_EYE_OUTER = 263
    private const val RIGHT_EYE_INNER = 362

    private const val LEFT_CHEEKBONE = 234
    private const val RIGHT_CHEEKBONE = 454

    private const val JAW_LEFT = 172
    private const val JAW_RIGHT = 397

    /**
     * 5.2.1 Calculate Facial Symmetry Score (0 - 100)
     * Compares distances between left/right landmark pairs to central vertical axis
     */
    fun calculateSymmetry(landmarks: List<FaceLandmark>): Int {
        if (landmarks.size < 468) return 85

        val noseBridge = landmarks[NOSE_BRIDGE]
        val noseTip = landmarks[NOSE_TIP]
        val chin = landmarks[CHIN_TIP]

        // Left vs Right landmark pairs
        val leftEyeOuter = landmarks[LEFT_EYE_OUTER]
        val rightEyeOuter = landmarks[RIGHT_EYE_OUTER]

        val leftCheek = landmarks[LEFT_CHEEKBONE]
        val rightCheek = landmarks[RIGHT_CHEEKBONE]

        // Distances from nose center
        val dLeftEye = leftEyeOuter.distanceTo(noseTip)
        val dRightEye = rightEyeOuter.distanceTo(noseTip)
        val eyeDiff = abs(dLeftEye - dRightEye) / max(dLeftEye, dRightEye)

        val dLeftCheek = leftCheek.distanceTo(noseBridge)
        val dRightCheek = rightCheek.distanceTo(noseBridge)
        val cheekDiff = abs(dLeftCheek - dRightCheek) / max(dLeftCheek, dRightCheek)

        val avgDiff = (eyeDiff + cheekDiff) / 2.0
        val rawScore = 98.0 - (avgDiff * 100.0 * 0.4)

        return min(98, max(82, rawScore.roundToInt()))
    }

    /**
     * 5.2.2 Calculate Golden Ratio (1.618) Proportion Score (0 - 100)
     */
    fun calculateGoldenRatio(landmarks: List<FaceLandmark>): Int {
        if (landmarks.size < 468) return 86

        val forehead = landmarks[FOREHEAD_TOP]
        val noseTip = landmarks[NOSE_TIP]
        val chin = landmarks[CHIN_TIP]
        val leftCheek = landmarks[LEFT_CHEEKBONE]
        val rightCheek = landmarks[RIGHT_CHEEKBONE]

        val faceHeight = forehead.distanceTo(chin)
        val faceWidth = leftCheek.distanceTo(rightCheek)

        if (faceWidth == 0f) return 85

        val currentRatio = faceHeight / faceWidth
        const goldenTarget = 1.618f
        val ratioDiff = abs(currentRatio - goldenTarget) / goldenTarget

        val rawScore = 97.0 - (ratioDiff * 100.0 * 0.5)
        return min(98, max(83, rawScore.roundToInt()))
    }

    /**
     * 5.2.3 Calculate Jawline Sharpness & Angle Score (0 - 100)
     */
    fun calculateJawline(landmarks: List<FaceLandmark>): Int {
        if (landmarks.size < 468) return 87

        val jawLeft = landmarks[JAW_LEFT]
        val chin = landmarks[CHIN_TIP]
        val jawRight = landmarks[JAW_RIGHT]

        // Calculate jaw angle using trigonometry atan2
        val angleLeft = abs(atan2((chin.y - jawLeft.y).toDouble(), (chin.x - jawLeft.x).toDouble()))
        val angleRight = abs(atan2((chin.y - jawRight.y).toDouble(), (chin.x - jawRight.x).toDouble()))

        val angleDiff = abs(angleLeft - angleRight)
        val rawScore = 96.0 - (angleDiff * 15.0)

        return min(97, max(81, rawScore.roundToInt()))
    }

    /**
     * 5.2.4 Calculate Cheekbones Prominence Score (0 - 100)
     */
    fun calculateCheekbones(landmarks: List<FaceLandmark>): Int {
        if (landmarks.size < 468) return 88

        val leftCheek = landmarks[LEFT_CHEEKBONE]
        val rightCheek = landmarks[RIGHT_CHEEKBONE]
        val noseBridge = landmarks[NOSE_BRIDGE]

        val cheekWidth = leftCheek.distanceTo(rightCheek)
        val noseHeight = landmarks[FOREHEAD_TOP].distanceTo(landmarks[NOSE_TIP])

        if (noseHeight == 0f) return 86

        val ratio = cheekWidth / noseHeight
        val rawScore = 85.0 + (min(1.0f, ratio) * 11.0f)

        return min(97, max(82, rawScore.roundToInt()))
    }

    /**
     * 5.2.5 Calculate Potential Score (0 - 100)
     * Weighted average representing maximum aesthetic potential
     */
    fun calculatePotential(symmetry: Int, goldenRatio: Int, jawline: Int, cheekbones: Int): Int {
        val baseAvg = (symmetry + goldenRatio + jawline + cheekbones) / 4.0
        val potentialBoost = 4.0
        return min(99, (baseAvg + potentialBoost).roundToInt())
    }

    /**
     * Comprehensive Master Function: Calculates all scores on-device in milliseconds
     */
    fun calculateAllScores(
        landmarks: List<FaceLandmark>,
        bitmap: Bitmap,
        gender: String = "male"
    ): FaceScores {
        val symmetry = calculateSymmetry(landmarks)
        val goldenRatio = calculateGoldenRatio(landmarks)
        val jawline = calculateJawline(landmarks)
        val cheekbones = calculateCheekbones(landmarks)
        val skinQuality = SkinQualityAnalyzer.calculateSkinQuality(bitmap)

        // Gender trait score (Masculinity vs Femininity emphasis)
        val genderScore = if (gender == "female") {
            min(97, max(83, ((symmetry + cheekbones + skinQuality) / 3.0).roundToInt()))
        } else {
            min(97, max(83, ((jawline + cheekbones + symmetry) / 3.0).roundToInt()))
        }

        val overall = min(98, max(80, (
            symmetry * 0.25 +
            goldenRatio * 0.20 +
            jawline * 0.20 +
            cheekbones * 0.15 +
            skinQuality * 0.20
        ).roundToInt()))

        val potential = calculatePotential(symmetry, goldenRatio, jawline, cheekbones)

        return FaceScores(
            overall = overall,
            potential = potential,
            skinQuality = skinQuality,
            jawline = jawline,
            cheekbones = cheekbones,
            symmetry = symmetry,
            genderScore = genderScore
        )
    }
}
