package boids

import boids.ext.*
import three.js.Vector3

class MotionState(initialPosition: Vector3?, initialAngle: Double) {
    companion object {
        private val auxVector = Vector3()
    }

    val position = initialPosition ?: Vector3()
    val velocity = initialAngle.toSpeedVector().normalize().multiplyScalar(BOID_MAX_SPEED)

    private val acceleration = Vector3()

    fun setSteerDirection(direction: Vector3) {
        acceleration.copy(direction)//.normalize().multiplyScalar(BOID_MAX_SPEED)
    }

    fun update(time: Double) {
        position.add(auxVector.copy(velocity).multiplyScalar(time))

        velocity.add(auxVector.copy(acceleration).multiplyScalar(time))
        if (velocity.lengthSq() >= BOID_MAX_SPEED_SQR) velocity.normalize().multiplyScalar(BOID_MAX_SPEED)
    }

    fun isInVisibleRange(target: Vector3, maxDistance: Double): Boolean {
        val headingAngle = velocity.asAngle()
        val halfVisibleRegionAngle = BOID_VISIBLE_REGION / 2.0
        val targetAngle = auxVector.subVectors(position, target).asAngle()
        val visibleRegionStartAngle = headingAngle - halfVisibleRegionAngle
        val visibleRegionEndAngle = headingAngle + halfVisibleRegionAngle
        inline fun targetInAngularRange() = targetAngle.isInAngleRange(visibleRegionStartAngle, visibleRegionEndAngle)
        inline fun targetInDistance() = position.distanceToSquared(target) <= maxDistance.sqr

        return targetInAngularRange() && targetInDistance()
    }
}