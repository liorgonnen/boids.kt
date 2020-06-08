package boids.behaviors

import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import three.js.Vector
import three.js.Vector3

object CohesionBehavior : Behavior() {

    //private val auxVector = Vector3()

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {
        var count = 0
        neighbors.forEach { neighbor ->
            if (boid.isInVisibleRange(neighbor.position, BOID_SEE_AHEAD_DISTANCE)) {
                result.add(neighbor.position)
                count++
            }
        }

        if (count > 0) result.divideScalar(count).sub(boid.position)
    }
}