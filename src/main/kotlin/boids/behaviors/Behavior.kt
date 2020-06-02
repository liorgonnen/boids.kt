package boids.behaviors

import boids.BOID_MAX_ANGULAR_ACCELERATION
import boids.Boid
import boids.ext.isZero
import boids.ext.zero
import three.js.Vector3

open class SteeringForce(linearAcceleration: Double = 0.0, angularAcceleration: Double = 0.0) {

    val isZero get() = linearAcceleration == 0.0 && angularAcceleration == 0.0

    val isNonZero get() = !isZero

    var linearAcceleration = linearAcceleration
        set(value) {
            field = value
        }

    var angularAcceleration = angularAcceleration
        set(value) {
            field = value.coerceIn(-BOID_MAX_ANGULAR_ACCELERATION, BOID_MAX_ANGULAR_ACCELERATION)
        }

    fun zero() {
        linearAcceleration = 0.0
        angularAcceleration = 0.0
    }

    fun copy(other: SteeringForce) {
        linearAcceleration = other.linearAcceleration
        angularAcceleration = other.angularAcceleration
    }

    fun add(other: SteeringForce) {
        linearAcceleration += other.linearAcceleration
        angularAcceleration += other.angularAcceleration
    }

    fun multiplyScalar(scalar: Double) = apply {
        linearAcceleration *= scalar
        angularAcceleration *= scalar
    }

    fun divideScalar(scalar: Double) = apply {
        linearAcceleration /= scalar
        angularAcceleration /= scalar
    }
}

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
    protected val result = SteeringForce()

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
    abstract fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>): SteeringForce
}