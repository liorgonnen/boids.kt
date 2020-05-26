package boids.ext

import three.js.Vector3
import kotlin.math.cos
import kotlin.math.sin

private val auxVector = Vector3()

val Vector3.isZero get() = x == 0 && y == 0 && z == 0

inline val Vector3.isNoneZero get() = !isZero

fun Vector3.asString() = "[$x, $y, $z]"

inline fun Vector3.zero() = set(0.0, 0.0, 0.0)

fun Vector3.setXZFromAngle(angle: Double) = apply { set(sin(angle), 0, cos(angle)) }