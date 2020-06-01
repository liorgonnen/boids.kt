package boids.behaviors

import boids.Boid
import boids.ext.zero
import kotlin.math.PI
import kotlin.math.abs

object AlignmentBehavior : Behavior() {

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        var count = 0
        neighbors.iterator().forEach { neighbor ->
            val similarOrientation = abs(neighbor.motionState.headingAngle - boid.motionState.headingAngle) <= PI

            if (similarOrientation) {
                add(neighbor.motionState.headingDirection)
                count++
            }
        }

        // if count is zero- no neighbors. If 1, no need to divide
        if (count > 1) divideScalar(count)
    }
}

