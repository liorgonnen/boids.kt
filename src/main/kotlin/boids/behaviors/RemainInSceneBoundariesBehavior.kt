package boids.behaviors

import boids.BOID_MAX_VELOCITY
import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.ext.compareTo
import boids.ext.normalized
import three.js.Vector3

object RemainInSceneBoundariesBehavior : Behavior() {

    private const val MIN_DISTANCE = BOID_MAX_VELOCITY
    private const val FAR = -HALF_SCENE_SIZE + MIN_DISTANCE
    private const val NEAR = HALF_SCENE_SIZE - MIN_DISTANCE
    private const val LEFT = -HALF_SCENE_SIZE + MIN_DISTANCE
    private const val RIGHT = HALF_SCENE_SIZE - MIN_DISTANCE

    override val overridesLowerPriorityBehaviors = true

    override fun computeSteeringForce(boid: Boid, neighbors: Iterator<Boid>, result: Vector3) {
        when {
            boid.position.z < FAR -> result.set(boid.velocity.x.normalized, 0, 1.0)
            boid.position.z > NEAR -> result.set(boid.velocity.x.normalized, 0, -1.0)
            boid.position.x < LEFT -> result.set(1.0, 0, boid.velocity.z.normalized)
            boid.position.x > RIGHT -> result.set(-1.0, 0, boid.velocity.z.normalized)
        }
    }
}
