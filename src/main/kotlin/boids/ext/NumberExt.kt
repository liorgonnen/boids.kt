package boids.ext

import three.js.Vector3
import kotlin.math.PI
import kotlin.math.abs
import kotlin.random.Random

inline val Number.absValue get() = abs(toDouble())

fun randomAngle(rangeInRadians: Double) = Random.nextDouble(-1.0, 1.0) * rangeInRadians
fun fraction(value: Double, min: Double, max: Double) = (value.coerceIn(min, max) - min) / (max - min)

fun Number.toDegrees() = this / PI * 180.0
fun Number.toRadians() = this * PI / 180.0
fun Number.sign() = toDouble().let { if (this < 0.0) -1.0 else if (this > 0.0) 1.0 else 0.0 }

operator fun Number.minus(other: Double) = toDouble() - other
operator fun Number.plus(other: Double) = toDouble() + other
operator fun Number.div(other: Double) = toDouble() / other
operator fun Number.times(other: Double) = toDouble() * other
operator fun Number.compareTo(other: Double): Int = toDouble().compareTo(other)

operator fun Double.minus(other: Number) = this - other.toDouble()
operator fun Double.plus(other: Number) = this + other.toDouble()

inline val Double.sqr get() = this * this

fun Double.toSpeedVector() = Vector3().setXZFromAngle(this)

fun Double.wrapTo2PI() = if (this < 0.0) (this + TWO_PI) else if (this > TWO_PI) (this - TWO_PI) else this

fun Double.isInAngleRange(startAngle: Double, endAngle: Double): Boolean {
    val start = startAngle.wrapTo2PI()
    val end = endAngle.wrapTo2PI()
    return if (start < end) this in start..end else this in end..start
}

fun Double.asRangeFraction(min: Double, max: Double) = fraction(this, min, max)

