package boids.behaviors

import boids.BOID_MAX_ACCELERATION
import boids.Boid
import boids.ext.zero
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.abs

object AlignmentBehavior : Behavior() {

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        var count = 0
        neighbors.iterator().forEach { neighbor ->
            val similarOrientation = abs(neighbor.motionState.headingAngle - boid.motionState.headingAngle) <= PI / 2.0

            if (similarOrientation) {
                result.add(neighbor.motionState.headingDirection)
                count++
            }
        }
    }
}

