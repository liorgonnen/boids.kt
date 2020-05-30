package boids.behaviors

import boids.BOID_MIN_DISTANCE
import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import boids.ext.asRangeFraction
import boids.ext.zero
import three.js.Vector3
import kotlin.math.pow

object SeparationBehavior : Behavior() {

    private val direction = Vector3()

    override val weight = 0.5

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        neighbors.iterator().forEach { neighbor ->
            val distance = neighbor.position.distanceTo(boid.position).toDouble()

            if (distance != 0.0) {
                direction.subVectors(boid.position, neighbor.position)

                val distanceFraction = distance.asRangeFraction(BOID_MIN_DISTANCE, BOID_SEE_AHEAD_DISTANCE)
                val decay = (distanceFraction - 1.0).pow(4)
                add(direction.multiplyScalar(decay))
            }
        }
    }
}