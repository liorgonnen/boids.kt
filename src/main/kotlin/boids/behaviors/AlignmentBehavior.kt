package boids.behaviors

import boids.ALIGNMENT_EPSILON
import boids.BOID_MAX_ANGULAR_ACCELERATION
import boids.Boid
import boids.ext.absValue
import boids.ext.roundAngleDegrees
import boids.ext.toRadians
import boids.ext.zero
import kotlin.math.PI
import kotlin.math.abs

object AlignmentBehavior : Behavior() {

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): SteeringForce {
        result.zero()

        var count = 0
        var targetAngle = 0.0
        neighbors.iterator().forEach { neighbor ->
            targetAngle += neighbor.headingAngle
            count++
        }

        if (count == 0) return result

        targetAngle /= count

        val deltaAngle = targetAngle - boid.headingAngle

        if (deltaAngle.absValue < ALIGNMENT_EPSILON) return result

        result.angularAcceleration = deltaAngle

        return result
    }
}

