package boids.behaviors

import boids.BOID_MAX_SPEED
import boids.Boid
import boids.HALF_SCENE_SIZE
import three.js.Vector3

object SeekBehavior : Behavior() {

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3 {
        result.set(-HALF_SCENE_SIZE, 0, 0).normalize().multiplyScalar(BOID_MAX_SPEED)
        return result
    }
}
