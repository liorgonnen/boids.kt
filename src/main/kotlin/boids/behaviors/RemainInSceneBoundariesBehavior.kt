package boids.behaviors

import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.ext.absValue
import boids.ext.compareTo
import three.js.Vector3

object RemainInSceneBoundariesBehavior : Behavior() {

    private const val MIN_DISTANCE = 10.0

    override val overridesLowerPriorityBehaviors = true

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {
        if (boid.position.z < -HALF_SCENE_SIZE + MIN_DISTANCE) {
            result.z = boid.velocity.z.absValue
        }

        if (boid.position.z > HALF_SCENE_SIZE - MIN_DISTANCE) {
            result.z = -boid.velocity.z.absValue
        }

        if (boid.position.x < -HALF_SCENE_SIZE + MIN_DISTANCE) {
            result.x = boid.velocity.x.absValue
        }

        if (boid.position.x > HALF_SCENE_SIZE - MIN_DISTANCE) {
            result.x = -boid.velocity.x.absValue
        }
    }


}
