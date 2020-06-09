package boids.behaviors

import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import boids.CollisionDetector
import boids.ext.compareTo
import boids.ext.normalized
import three.js.Box3
import three.js.Object3D
import three.js.Vector3

abstract class AbsCollisionAvoidanceBehavior : Behavior() {

    private val obstacles = ArrayList<Box3>()

    private val collisionPoint = Vector3()

    override val overridesLowerPriorityBehaviors = true

    fun add(obstacle: Box3) { obstacles += obstacle }

    fun add(obstacle: Object3D) { obstacles += Box3().setFromObject(obstacle) }

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {
        obstacles.minBy { it.distanceToPoint(boid.position).toDouble() }?.let { obstacle ->
            val collides = CollisionDetector.collides(boid.position, boid.velocity, BOID_SEE_AHEAD_DISTANCE, obstacle, collisionPoint)

            if (collides) boid.velocity.apply {
                if (z < 0.0 && collisionPoint.z == obstacle.max.z) result.set(x.normalized, 0, 1.0) // Approaching th wall from the front
                else if (z > 0.0 && collisionPoint.z == obstacle.min.z) result.set(x.normalized, 0, -1.0) // Approaching th wall from the front
                else if (x > 0.0 && collisionPoint.x == obstacle.min.x) result.set(-1.0, 0, z.normalized) // Approaching wall from the left
                else if (x < 0.0 && collisionPoint.x == obstacle.max.x) result.set(1.0, 0, z.normalized) // Approaching wall from the left
            }
        }
    }
}

object CollisionAvoidanceBehavior : AbsCollisionAvoidanceBehavior()
