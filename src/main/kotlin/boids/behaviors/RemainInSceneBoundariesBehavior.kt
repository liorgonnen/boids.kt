package boids.behaviors

import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.ext.*
import three.js.Vector3
import kotlin.math.roundToInt

object RemainInSceneBoundariesBehavior : Behavior() {

    override val weight = 5.0

    override fun isEffective(boid: Boid, neighbors: Sequence<Boid>) =
            boid.seeAhead.x.absValue > HALF_SCENE_SIZE || boid.seeAhead.z.absValue > HALF_SCENE_SIZE

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.copy(boid.position).negate()
}
