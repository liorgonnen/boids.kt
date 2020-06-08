package boids.behaviors

import boids.*
import three.js.Vector3

object AlignmentBehavior : Behavior() {

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {
        var count = 0
        neighbors.iterator().forEach { neighbor ->
            result.add(neighbor.velocity)
            count++
        }

        if (count > 0) result.divideScalar(count.toDouble())
    }
}

