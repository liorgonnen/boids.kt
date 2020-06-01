package boids.behaviors

import boids.BOID_MIN_DISTANCE
import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import boids.ext.asRangeFraction
import boids.ext.zero
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow

object SeparationBehavior : Behavior() {

    private val direction = Vector3()

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        neighbors.iterator().forEach { neighbor ->
            val distance = neighbor.position.distanceTo(boid.position).toDouble()
            val similarOrientation = abs(neighbor.motionState.headingAngle - boid.motionState.headingAngle) <= PI / 2.0
            if (distance != 0.0 && distance <  BOID_MIN_DISTANCE * 3.0 && similarOrientation) {
                val distanceFraction = distance.asRangeFraction(BOID_MIN_DISTANCE, BOID_MIN_DISTANCE * 3)
                val decay = (distanceFraction - 1.0).pow(4)

                direction.subVectors(boid.position, neighbor.position)
                add(direction.multiplyScalar(decay))
            }
        }
    }
}