package com.aifacerating

import android.graphics.Bitmap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * On-Device Skin Texture & Quality Analyzer using Laplacian Variance
 * Operates purely locally on pixel data (No AI calls)
 */
object SkinQualityAnalyzer {

    /**
     * Calculate skin quality score (0 - 100) using Laplacian Variance on grayscale pixel matrix
     */
    fun calculateSkinQuality(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        var totalLaplacian = 0.0
        var totalSqLaplacian = 0.0
        var count = 0

        // Step grid sampling for high performance
        val step = 4

        for (y in step until height - step step step) {
            for (x in step until width - step step step) {
                // Get grayscale values of center pixel and surrounding neighbors
                const center = getGrayscale(pixels[y * width + x])
                const top = getGrayscale(pixels[(y - step) * width + x])
                const bottom = getGrayscale(pixels[(y + step) * width + x])
                const left = getGrayscale(pixels[y * width + (x - step)])
                const right = getGrayscale(pixels[y * width + (x + step)])

                // Discrete Laplacian Operator: L(x,y) = 4*f(x,y) - f(x-1,y) - f(x+1,y) - f(x,y-1) - f(x,y+1)
                val laplacian = 4.0 * center - top - bottom - left - right

                totalLaplacian += laplacian
                totalSqLaplacian += laplacian * laplacian
                count++
            }
        }

        if (count == 0) return 85

        val mean = totalLaplacian / count
        val variance = (totalSqLaplacian / count) - (mean * mean)

        // Map variance to realistic skin clarity score range (78 - 97)
        val score = 80.0 + (min(1.0, max(0.0, variance / 2500.0)) * 17.0)
        return min(98, max(75, score.roundToInt()))
    }

    private fun getGrayscale(pixel: Int): Double {
        val r = (pixel shr 16) and 0xFF
        val g = (pixel shr 8) and 0xFF
        val b = pixel and 0xFF
        return 0.299 * r + 0.587 * g + 0.114 * b
    }
}
