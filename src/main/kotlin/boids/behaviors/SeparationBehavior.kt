package boids.behaviors

import boids.BOID_MIN_DISTANCE
import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import boids.ext.asAngle
import boids.ext.asRangeFraction
import boids.ext.setXZFromAngle
import boids.ext.zero
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow

object SeparationBehavior : Behavior() {

    private val direction = Vector3()

    override val weight = 1.0

    private val auxVector = Vector3()
    private val predictedNeighborPosition = Vector3()

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()
        auxVector.zero()

        neighbors.minBy { it.distanceTo(boid) }?.let { nearestBoid ->
            val distance = boid.distanceTo(nearestBoid)
            val distanceFraction = distance.asRangeFraction(BOID_MIN_DISTANCE, BOID_MIN_DISTANCE * 2)
            val decay = (distanceFraction - 1.0).pow(4)
            result.angularAcceleration = (auxVector.copy(boid.position).sub(nearestBoid.position).asAngle() - boid.headingAngle) * decay
        }
    }
}