package boids

import boids.ext.*
import three.js.Vector3

object Math {
    private val auxVector = Vector3()
    fun isInVisibleRange(sourceHeadingAngle: Double, sourcePosition: Vector3, target: Vector3, maxDistance: Double): Boolean {
        val angle = sourceHeadingAngle.wrapTo2PI()
        val halfVisibleRegionAngle = BOID_VISIBLE_REGION / 2.0
        val targetAngle = auxVector.subVectors(target, sourcePosition).asAngle()
        val visibleRegionStartAngle = angle - halfVisibleRegionAngle
        val visibleRegionEndAngle = angle + halfVisibleRegionAngle

        inline fun targetInAngularRange() = targetAngle.isInAngleRange(visibleRegionStartAngle, visibleRegionEndAngle)
        inline fun targetInDistance() = sourcePosition.distanceToSquared(target) <= maxDistance.sqr

        return targetInAngularRange() && targetInDistance()
    }
}