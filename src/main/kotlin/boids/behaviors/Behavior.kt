package boids.behaviors

import boids.BOID_MAX_ACCELERATION
import boids.BOID_MAX_VELOCITY
import boids.Boid
import boids.ext.isNoneZero
import boids.ext.zero
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

    /**
     * Auxiliary object to prevent object creation
     */
    private val result = Vector3()

    /**
     * If true and this behavior returns a non-zero force, the rest of the behaviors will be discarded
     */
    open val overridesLowerPriorityBehaviors = false

    open val weight = 1.0

    /**
     * This function can return false to cancel out this behavior for the given boid
     * The goal is to make it as lightweight as possible and decide if [getSteeringForce] should be called
     */
    open fun isEffective(boid: Boid) = true

    /**
     * This method will not be called if [isEffective] returns false
     */
    fun getSteeringForce(boid: Boid, neighbors: Iterator<Boid>) = result.apply {
        zero()

        computeSteeringForce(boid, neighbors, result)

        if (result.isNoneZero) result
            .normalize()
            //.multiplyScalar(BOID_MAX_ACCELERATION)
            .multiplyScalar(BOID_MAX_VELOCITY)
            .sub(boid.velocity)
            .clampLength(0, BOID_MAX_ACCELERATION)
    }

    abstract fun computeSteeringForce(boid: Boid, neighbors: Iterator<Boid>, result: Vector3)
}