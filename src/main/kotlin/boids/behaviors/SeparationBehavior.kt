package boids.behaviors

import boids.*
import boids.ext.*
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt

object SeparationBehavior : Behavior() {

    private val direction = Vector3()

    override val weight = 0.5

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        neighbors.iterator().forEach { neighbor ->
            val distance = neighbor.position.distanceTo(boid.position).toDouble()
            val similarOrientation = abs(neighbor.motionState.headingAngle - boid.motionState.headingAngle) <= PI / 2.0

            if (distance != 0.0 && similarOrientation) {
                direction.subVectors(boid.position, neighbor.position)

                val distanceFraction = distance.asRangeFraction(BOID_MIN_DISTANCE, BOID_SEE_AHEAD_DISTANCE)
                val decay = 1.0 - distanceFraction.sqr
                add(direction.multiplyScalar(decay))
            }
        }
    }
}