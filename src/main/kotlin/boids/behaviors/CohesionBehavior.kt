package boids.behaviors

import boids.Boid
import three.js.Vector3

object CohesionBehavior : Behavior() {

    private val averagePosition = Vector3()

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3 {
        result.set(0, 0, 0)
        averagePosition.set(0, 0, 0)

        var count = 0
        neighbors.iterator().forEach { neighbor -> averagePosition.add(neighbor.position); count++ }

        if (count > 0) {
            averagePosition.divideScalar(count)
            result.subVectors(boid.position, averagePosition)
        }

        return result
    }

}