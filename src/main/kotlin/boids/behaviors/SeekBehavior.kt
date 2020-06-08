package boids.behaviors

import boids.Boid

object SeekBehavior : Behavior() {

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
//        result.set(-HALF_SCENE_SIZE, 0, 0).normalize().multiplyScalar(BOID_MAX_SPEED)
//        return result
    }
}
