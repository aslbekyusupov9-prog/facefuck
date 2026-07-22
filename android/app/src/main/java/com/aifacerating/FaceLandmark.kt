package com.aifacerating

/**
 * 3D Face Landmark representation (x, y, z in normalized coordinates)
 */
data class FaceLandmark(
    val x: Float,
    val y: Float,
    val z: Float = 0f
) {
    /**
     * Calculate 2D Euclidean distance to another landmark
     */
    fun distanceTo(other: FaceLandmark): Float {
        val dx = x - other.x
        val dy = y - other.y
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    /**
     * Calculate 3D Euclidean distance to another landmark
     */
    fun distanceTo3D(other: FaceLandmark): Float {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return Math.sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()
    }
}
