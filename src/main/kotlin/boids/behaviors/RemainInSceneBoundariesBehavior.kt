package boids.behaviors

import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.ext.*
import three.js.Vector3

object RemainInSceneBoundariesBehavior : Behavior() {

    private val auxVector = Vector3()

    override val weight = 5.0

    override fun isEffective(boid: Boid, neighbors: Sequence<Boid>) =
            boid.seeAhead.x.absValue > HALF_SCENE_SIZE || boid.seeAhead.z.absValue > HALF_SCENE_SIZE

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        result.zero()
        var angle = boid.headingAngle - auxVector.copy(boid.position).negate().asAngle()

        boid.seeAhead.apply {
            when {
                x < -HALF_SCENE_SIZE -> {
                    if (angle < DIR_WEST) angle -= HALF_PI else angle += HALF_PI
                    result.angularAcceleration = angle - boid.headingAngle
                }
                x > HALF_SCENE_SIZE -> {
                    if (angle < DIR_EAST) angle -= HALF_PI else angle += HALF_PI
                    result.angularAcceleration = angle - boid.headingAngle
                }
                z < -HALF_SCENE_SIZE -> {
                    if (angle > DIR_NORTH) angle += HALF_PI else angle -= HALF_PI
                    result.angularAcceleration = angle - boid.headingAngle
                }
                z > HALF_SCENE_SIZE -> {
                    if (angle > 0.0) angle += HALF_PI else angle -= HALF_PI
                    result.angularAcceleration = angle - boid.headingAngle
                }
            }
        }
    }
}
