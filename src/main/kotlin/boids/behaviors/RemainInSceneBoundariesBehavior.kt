package boids.behaviors

import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.ext.absValue
import boids.ext.compareTo

object RemainInSceneBoundariesBehavior : Behavior() {

    override val overridesLowerPriorityBehaviors = true

    override fun isEffective(boid: Boid, neighbors: Sequence<Boid>) =
            boid.seeAhead.x.absValue > HALF_SCENE_SIZE || boid.seeAhead.z.absValue > HALF_SCENE_SIZE

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.also { force ->
        force.set(0, 0, 0)
        with (boid.seeAhead) {
            if (x < -HALF_SCENE_SIZE) force.x = 1
            if (x > HALF_SCENE_SIZE) force.x = -1
            if (z < -HALF_SCENE_SIZE) force.z = 1
            if (z > HALF_SCENE_SIZE) force.z = -1
        }
    }
}