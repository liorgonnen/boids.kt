package boids

import boids.ext.Z_AXIS
import boids.ext.compareTo
import boids.ext.toRadians
import three.js.Vector3
import kotlin.random.Random

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

    /**
     * Auxiliary object to prevent object creation
     */
    protected val result = Vector3()

    /**
     * If true and this behavior returns a non-zero force, the rest of the behaviors will be discarded
     */
    open val overridesLowerPriorityBehaviors = false

    open val weight = 1.0

    /**
     * This function can return false to cancel out this behavior for the given boid
     * The goal is to make it as lightweight as possible and decide if [getSteeringForce] should be called
     */
    open fun isEffective(boid: Boid, neighbors: Sequence<Boid>) = true

    /**
     * This method will not be called if [isEffective] returns false
     */
    abstract fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3
}

object WanderBehavior : Behavior() {

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        result.applyAxisAngle(Z_AXIS, Random.nextDouble() * 20.0.toRadians())
    }
}

object SeekBehavior : Behavior() {

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3 {
        result.set(-HALF_SCENE_SIZE, 0, 0).normalize().multiplyScalar(BOID_MAX_SPEED)
        return result
    }
}

object AlignmentBehavior : Behavior() {

    override val weight = 1.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3 {
        result.set(0, 0, 0)

        var count = 0
        neighbors.iterator().forEach { neighbor -> result.add(neighbor.steerDirection); count++ }

        return result
    }
}

object CohesionBehavior : Behavior() {

    private val averagePosition = Vector3()

    override val weight = 2.0

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): Vector3 {
        result.set(0, 0, 0)
        averagePosition.set(0, 0, 0)

        var count = 0
        neighbors.iterator().forEach { neighbor -> averagePosition.add(neighbor.position); count++ }

        if (count > 1) {
            averagePosition.divideScalar(count)
            result.subVectors(boid.position, averagePosition)
        }

        return result
    }

}

object RemainInSceneBoundariesBehavior : Behavior() {

    override val overridesLowerPriorityBehaviors = true

    override fun isEffective(boid: Boid, neighbors: Sequence<Boid>) = with (boid.position) {
        x < -HALF_SCENE_SIZE || x > HALF_SCENE_SIZE ||
        z < -HALF_SCENE_SIZE || z > HALF_SCENE_SIZE
    }

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.also { force ->
        force.set(0, 0, 0)
        with (boid.position) {
            if (x < -HALF_SCENE_SIZE) force.x = 1
            if (x > HALF_SCENE_SIZE) force.x = -1
            if (z < -HALF_SCENE_SIZE) force.z = 1
            if (z > HALF_SCENE_SIZE) force.z = -1
        }
    }
}