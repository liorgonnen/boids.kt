package boids

import boids.ext.minus
import boids.ext.plus
import three.js.Vector3

/**
 * A [Behavior] describes some characteristic that affects a boid's steering behavior.
 * It is stateless and is common to all boids in a flock.
 * The behavior can inspect all the boids in the flock to determine its desired effect on any given boid.
 * Although all the boids can be inspected, a behavior should limit its inspection only to boids in a certain
 * vicinity to a given boid to reflect one (or more) of the boid's senses (as opposed to boid's superpowers)
 * If the behavior needs to hold boid-specific information, this information should be part of the [Boid] class.
 * Although this doesn't demonstrate good encapsulation, the thinking behind this is that:
 * 1. Practically- This allows us to hold a single behavior instance per flock, instead of per boid, thus significantly
 * reducing object-creation and memory footprint, especially if we take into account that most behaviors do NOT need
 * per boid information
 * 2. Conceptually-
 */
abstract class Behavior {

    // Prevent object creation
    protected val result = Vector3()

    open fun isEffective(boid: Boid, flock: Array<Boid>) = true

    abstract fun getSteeringForce(boid: Boid, flock: Array<Boid>): Vector3
}

object WanderBehavior : Behavior() {

    override fun getSteeringForce(boid: Boid, flock: Array<Boid>): Vector3 {
        TODO("Not yet implemented")
    }

}

object SeekBehavior : Behavior() {

    override fun getSteeringForce(boid: Boid, flock: Array<Boid>): Vector3 {
        result.set(-HALF_SCENE_SIZE, 0, 0).normalize().multiplyScalar(BOID_MAX_SPEED)
        return result
    }
}

object RemainInSceneBoundariesBehavior : Behavior() {

    override fun isEffective(boid: Boid, flock: Array<Boid>) = with (boid.position) {
        x - BOID_MAX_SPEED < -HALF_SCENE_SIZE ||
        x + BOID_MAX_SPEED > HALF_SCENE_SIZE ||
        z - BOID_MAX_SPEED < -HALF_SCENE_SIZE ||
        z + BOID_MAX_SPEED > HALF_SCENE_SIZE
    }

    override fun getSteeringForce(boid: Boid, flock: Array<Boid>) = result.also { force ->
        force.set(0, 0, 0)
        with (boid.position) {
            if (x - BOID_MAX_SPEED < -HALF_SCENE_SIZE) force.x = 1
            if (x + BOID_MAX_SPEED > HALF_SCENE_SIZE) force.x = -1
            if (z - BOID_MAX_SPEED < -HALF_SCENE_SIZE) force.z = 1
            if (z + BOID_MAX_SPEED > HALF_SCENE_SIZE) force.z = -1
        }
    }
}