package boids.behaviors

import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import boids.CollisionDetector
import three.js.Box3
import three.js.Object3D
import three.js.Vector3
import kotlin.math.PI

abstract class AbsCollisionAvoidanceBehavior : Behavior() {

    companion object {
        private const val COLLISION_WHISKER_ANGLE = 10.0 / 180.0 * PI
    }

    private val obstacles = ArrayList<Box3>()

    private val collisionPoint = Vector3()

    override val overridesLowerPriorityBehaviors = true

    fun add(obstacle: Box3) { obstacles += obstacle }

    fun add(obstacle: Object3D) { obstacles += Box3().setFromObject(obstacle) }

    //override fun isEffective(boid: Boid)
           // = boid.collidesWithAny(COLLISION_WHISKER_ANGLE) || boid.collidesWithAny(-COLLISION_WHISKER_ANGLE)

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {

        obstacles.forEach { obstacle ->
            val collides = CollisionDetector.collides(boid.position, boid.velocity, BOID_SEE_AHEAD_DISTANCE, obstacle, collisionPoint)
            if (collides) {

            }
        }
    }

//    private fun Boid.collidesWithAny(deltaAngle: Double)
//            = CollisionDetector.collidesAny(position, headingAngle + deltaAngle, BOID_SEE_AHEAD_DISTANCE, obstacles)
}

object CollisionAvoidanceBehavior : AbsCollisionAvoidanceBehavior()
