package boids.behaviors

import boids.Boid
import boids.ext.zero
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.abs

object CohesionBehavior : Behavior() {

    private val averagePosition = Vector3()

    override val weight = 3.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()
        averagePosition.zero()

        var count = 0
        neighbors.iterator().forEach { neighbor ->
            val similarOrientation = abs(neighbor.motionState.headingAngle - boid.motionState.headingAngle) <= PI
            if (similarOrientation) {
                averagePosition.add(neighbor.position)
                count++
            }
        }

        if (count >= 1) {
            averagePosition.divideScalar(count)
            subVectors(averagePosition, boid.position)
        }
    }
}