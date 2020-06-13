package boids.behaviors

import boids.BOID_MIN_DISTANCE
import boids.Boid
import boids.ext.asRangeFraction
import three.js.Vector3

object SeparationBehavior : Behavior() {

    private val auxVector = Vector3()

    override val weight = 2.0

    override fun computeSteeringForce(boid: Boid, neighbors: Iterator<Boid>, result: Vector3) {
        var count = 0
        neighbors.forEach { neighbor ->
            val distance = boid.distanceTo(neighbor)
            if (distance < BOID_MIN_DISTANCE) {
                val distanceFraction = distance.asRangeFraction(BOID_MIN_DISTANCE, BOID_MIN_DISTANCE * 2)
                val decay = 1.0 - distanceFraction
                result.add(auxVector.subVectors(boid.position, neighbor.position).normalize().multiplyScalar(decay))
                count++
            }
        }

        if (count > 0) result.divideScalar(count.toDouble())
    }
}