package boids.ext

import kotlin.math.PI

fun Number.toDegrees() = this / PI * 180.0

operator fun Number.minus(other: Double) = toDouble() - other
operator fun Number.plus(other: Double) = toDouble() + other
operator fun Number.div(other: Double) = toDouble() / other
operator fun Number.times(other: Double) = toDouble() * other
operator fun Number.compareTo(other: Double): Int = toDouble().compareTo(other)

operator fun Double.minus(other: Number) = this - other.toDouble()
operator fun Double.plus(other: Number) = this + other.toDouble()

