package boids.behaviors

import boids.*
import boids.ext.asRangeFraction
import boids.ext.compareTo
import boids.ext.sqr
import boids.ext.zero
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.roundToInt

object SeparationBehavior : Behavior() {

    private val direction = Vector3()

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        neighbors.iterator().forEach { neighbor ->
            val distance = neighbor.position.distanceTo(boid.position).toDouble()

            if (distance != 0.0 && neighbor.position.angleTo(boid.position) <= PI / 2.0) {

                direction.copy(boid.position).sub(neighbor.position)
                val distanceFraction = distance.asRangeFraction(BOID_MIN_DISTANCE, BOID_SEE_AHEAD_DISTANCE)
                val decay = 1.0 - distanceFraction.sqr
                result.add(direction.normalize().multiplyScalar(BOID_MAX_ACCELERATION * decay))
            }
        }
    }
}