package boids.behaviors

import boids.BOID_MAX_ACCELERATION
import boids.Boid
import boids.ext.asString
import boids.ext.zero
import three.js.Vector3

object CohesionBehavior : Behavior() {

    private val averagePosition = Vector3()

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()
        averagePosition.zero()

        var count = 0
        neighbors.iterator().forEach {
            neighbor -> averagePosition.add(neighbor.position)
            count++
        }

        if (count > 0) {
            averagePosition.divideScalar(count)
            subVectors(averagePosition, boid.position).normalize().multiplyScalar(BOID_MAX_ACCELERATION)
        }
    }

}