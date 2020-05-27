package boids.behaviors

import boids.BOID_MAX_ACCELERATION
import boids.Boid
import boids.ext.zero
import three.js.Vector3

object AlignmentBehavior : Behavior() {

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        var count = 0
        neighbors.iterator().forEach {
            neighbor -> result.add(neighbor.motionState.velocity)
            count++
        }

        normalize().multiplyScalar(BOID_MAX_ACCELERATION)
    }
}

