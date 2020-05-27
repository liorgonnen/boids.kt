package boids.ext

import three.js.Vector3
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

val Vector3.isZero get() = x == 0 && y == 0 && z == 0

inline val Vector3.isNoneZero get() = !isZero

inline fun Vector3.zero() = set(0.0, 0.0, 0.0)

/**
 * Angle zero is towards the positive Z-Axis, so actually x serves s the Y-axis, that's why we pass it as the first
 * parameter to [atan2]
 */
inline fun Vector3.asAngle() = atan2(x.toDouble(), z.toDouble()).let { if (it < 0.0) it + TWO_PI else it }

fun Vector3.setXZFromAngle(angle: Double) = apply { set(sin(angle), 0, cos(angle)) }

fun Vector3.asString() = "[$x, $y, $z]"
