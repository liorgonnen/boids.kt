package boids

import boids.ext.*
import three.js.*
import kotlin.math.PI
import kotlin.random.Random

/**
 * Thoughts about boid's flight:
 * A boid is always at-flight. It cannot stop or have zero-speed at any given time
 * A boid has some natural "cruise speed"
 * When a boid is flying forward (relative to itself), if its current speed is lower than its cruising speed, it will
 * accelerate, until it reaches that speed
 *
 * Should the cruising speed be the boid's maximum speed? I'm thinking that it should be lower, and the only time a
 * boid will accelerate to the max speed it to join a distant flock
 *
 * When a boid needs to change its steering direction it will decelerate. The bigger the angle between the boids
 * current direction and its new steering direction, the more it will decelerate
 */
class Boid {

    companion object {

        private val geometry = Geometry().apply {
            vertices = arrayOf(
                Vector3( 0,  0, -1), // 0 front
                Vector3( 0,  0,  7), // 1 back
                Vector3(-2,  0,  0), // 2 left
                Vector3( 2,  0,  0), // 3 right
                Vector3( 0,  1,  0), // 4 top
                Vector3( 0, -1,  0), // 5 bottom
            )

            faces = arrayOf(
                Face3(0, 2, 4), // front top left
                Face3(0, 4, 3), // front top right
                Face3(0, 5, 2), // front bottom left
                Face3(0, 3, 5), // front bottom right
                Face3(4, 2, 1), // back top left
                Face3(4, 1, 3), // back top right
                Face3(2, 5, 1), // back bottom left
                Face3(1, 5, 3), // back bottom right
            )

            computeFaceNormals()
        }

        private val material = MeshPhongMaterial().apply { color = Color(0x0000ff) }
    }

    val steerDirection = Vector3()
    private val steerQuaternion = Quaternion()

    private var speed = BOID_MAX_SPEED
    private var acceleration = BOID_ACCELERATION

    val obj3D = Mesh(geometry, material)

    inline val position get() = obj3D.position

    init {
        obj3D.rotation.y = randomDirection()
    }

    fun setSteerDirection(direction: Vector3) {
        steerDirection.copy(direction)
        steerQuaternion.setFromDirection(direction)
    }

    fun update(deltaT: Double) = with (obj3D) {
        updateAcceleration()
        updateSpeed(deltaT)

        if (steerDirection.isNoneZero && !quaternion.equals(steerQuaternion)) {
            quaternion.rotateTowards(steerQuaternion, deltaT * BOID_ROTATION_SPEED)
        }

        translateZ(deltaT * speed)
    }

    private fun updateAcceleration() {
        val angle = steerQuaternion.dotAngle(obj3D.quaternion) // angle will be 0..PI

        val accelerationRange = if (angle <= BOID_MAX_ACCELERATION_ANGLE) BOID_MAX_ACCELERATION_ANGLE else PI - BOID_MAX_ACCELERATION_ANGLE
        val accelerationFraction = (BOID_MAX_ACCELERATION_ANGLE - angle) / accelerationRange
        acceleration = BOID_ACCELERATION * accelerationFraction
    }

    private fun updateSpeed(deltaT: Double) {
        speed = (speed + acceleration * deltaT).coerceIn(BOID_MIN_SPEED, BOID_MAX_SPEED)
    }

    private fun randomDirection() = Random.nextDouble()
}
