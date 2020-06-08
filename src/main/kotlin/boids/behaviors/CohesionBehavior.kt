package boids.behaviors

import boids.Boid
import boids.ext.*
import three.js.Vector3

object CohesionBehavior : Behavior() {

    private val averagePosition = Vector3()
    private val predictedNeighborPosition = Vector3()

    override val weight = 2.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()
        averagePosition.zero()

        var count = 0
        neighbors.iterator().forEach { neighbor ->
            with (neighbor) { predictedNeighborPosition.setXZFromAngle(headingAngle).multiplyScalar(velocity) }
            averagePosition.add(predictedNeighborPosition).add(neighbor.position)
            count++
        }

        if (count > 0) {
            val targetAngle = averagePosition.divideScalar(count).sub(boid.position).asAngle()
            result.angularAcceleration = targetAngle - boid.headingAngle
        }
    }
}