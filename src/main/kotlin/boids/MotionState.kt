package boids

import boids.Math.isInVisibleRange
import boids.ext.*
import three.js.Quaternion
import three.js.Vector3
import kotlin.math.PI

class MotionState(initialPosition: Vector3?, initialAngle: Double) {
    companion object {
        private val auxVector = Vector3()
    }

    val position = initialPosition ?: Vector3()

    private val currentHeading = Quaternion().setFromAxisAngle(Y_AXIS, initialAngle)
    private val targetHeading = Quaternion().copy(currentHeading)

    val headingDirection = Vector3()
    var headingAngle = 0.0
        private set

    private val acceleration = Vector3()
    private val velocity = Vector3().setXZFromAngle(initialAngle).multiplyScalar(BOID_MAX_SPEED)

    init {
        updateHeadingDirection()
    }

    fun applySteeringForce(direction: Vector3) {
        acceleration.copy(direction).multiplyScalar(BOID_MAX_ACCELERATION)
    }

    fun update(time: Double) {
        if (acceleration.isNoneZero) {
            velocity.add(acceleration.multiplyScalar(time))
            velocity.clampLength(0, BOID_MAX_SPEED)
        }

        targetHeading.setFromDirection(velocity)

        if (!currentHeading.equals(targetHeading)) {
            currentHeading.rotateTowards(targetHeading, time * BOID_ROTATION_SPEED)
            updateHeadingDirection()
        }

        val actualFlightVelocity = velocity.length().toDouble().coerceIn(BOID_MIN_SPEED, BOID_MAX_SPEED)

        position.add(auxVector.copy(headingDirection).multiplyScalar(actualFlightVelocity * time))
    }

    fun isInVisibleRange(target: Vector3, maxDistance: Double) = isInVisibleRange(headingAngle, position, target, maxDistance)

    private fun updateHeadingDirection() {
        headingDirection.fromQuaternion(currentHeading).normalize()
        headingAngle = headingDirection.asAngle()
    }
}