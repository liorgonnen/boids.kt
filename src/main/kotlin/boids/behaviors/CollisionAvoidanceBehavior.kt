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

    val obstacles = ArrayList<Box3>()

    override val overridesLowerPriorityBehaviors = true

    fun add(obstacle: Box3) { obstacles += obstacle }

    fun add(obstacle: Object3D) { obstacles += Box3().setFromObject(obstacle) }

    override fun isEffective(boid: Boid)
            = boid.collidesWithAny(COLLISION_WHISKER_ANGLE) || boid.collidesWithAny(-COLLISION_WHISKER_ANGLE)

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {

//        // Left whisker collision detection
//        boid.findEvasionAngle(COLLISION_WHISKER_ANGLE).also { angle ->
//            if (angle != 0.0) return result.apply { angularAcceleration = angle }
//        }
//
//        // Right whisker collision detection
//        boid.findEvasionAngle(-COLLISION_WHISKER_ANGLE).also { angle ->
//            if (angle != 0.0) return result.apply { angularAcceleration = angle }
//        }
    }

    private fun Boid.findEvasionAngle(deltaAngle: Double)
            = CollisionDetector.findEvasionAngle(position, headingAngle + deltaAngle, BOID_SEE_AHEAD_DISTANCE, obstacles)

    private fun Boid.collidesWithAny(deltaAngle: Double)
            = CollisionDetector.collidesAny(position, headingAngle + deltaAngle, BOID_SEE_AHEAD_DISTANCE, obstacles)
}

object CollisionAvoidanceBehavior : AbsCollisionAvoidanceBehavior()
