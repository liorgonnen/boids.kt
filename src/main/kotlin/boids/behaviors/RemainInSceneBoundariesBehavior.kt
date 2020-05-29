package boids.behaviors

import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.ext.*
import three.js.Vector3
import kotlin.math.roundToInt

object RemainInSceneBoundariesBehavior : Behavior() {

    override val weight = 10.0

    override fun isEffective(boid: Boid, neighbors: Sequence<Boid>) =
            boid.seeAhead.x.absValue > HALF_SCENE_SIZE || boid.seeAhead.z.absValue > HALF_SCENE_SIZE

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3 {
        result.zero()
        val angle = boid.motionState.headingAngle
        boid.seeAhead.apply {
            when {
                x < -HALF_SCENE_SIZE -> {
                    if (angle < DIR_WEST) result.set(0, 0, -HALF_SCENE_SIZE) else result.set(0, 0, HALF_SCENE_SIZE)
                }
                x > HALF_SCENE_SIZE -> {
                    if (angle < DIR_EAST) result.set(0, 0, HALF_SCENE_SIZE) else result.set(0, 0, -HALF_SCENE_SIZE)
                }
                z < -HALF_SCENE_SIZE -> {
                    if (angle > DIR_NORTH) result.set(-HALF_SCENE_SIZE, 0, 0) else result.set(HALF_SCENE_SIZE, 0, 0)
                }
                z > HALF_SCENE_SIZE -> {
                    if (angle > 0.0) result.set(HALF_SCENE_SIZE, 0, 0) else result.set(-HALF_SCENE_SIZE, 0, 0)
                }
            }
        }

        if (result.x != 0 || result.z != 0) result.sub(boid.position)

        return result
    }
}
