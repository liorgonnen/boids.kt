package boids

import boids.ext.*
import three.js.*
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
class Boid(position: Vector3? = null, angle: Double = 0.0, color: Int = BOID_DEFAULT_COLOR) {

    companion object {

        private var count = 0

        private fun uniqueId() = count.apply { count++ }

        private val NOSE_Z = 7.0

        private val geometry = Geometry().apply {
            vertices = arrayOf(
                Vector3( 0,  0, -1), // 0 back
                Vector3( 0,  0,  NOSE_Z), // 1 front
                Vector3(-2,  0,  0), // 2 left
                Vector3( 2,  0,  0), // 3 right
                Vector3( 0,  1,  0), // 4 top
                Vector3( 0, -1,  0), // 5 bottom
            )

            faces = arrayOf(
                Face3(0, 2, 4), // back top left
                Face3(0, 4, 3), // back top right
                Face3(0, 5, 2), // back bottom left
                Face3(0, 3, 5), // back bottom right
                Face3(4, 2, 1), // front top left
                Face3(4, 1, 3), // front top right
                Face3(2, 5, 1), // front bottom left
                Face3(1, 5, 3), // front bottom right
            )

            computeFaceNormals()
            computeBoundingSphere()
        }

        private val defaultMaterial = BOID_DEFAULT_COLOR.toMeshPhongMaterial()
    }

    val id = uniqueId()

    val seeAhead = Vector3()

    val position get() = obj3D.position

    val motionState = MotionState(position, angle)

    private val worldDirection = Vector3()
    private val seeAheadHelper = ArrowHelper(worldDirection, Vector3(0, 0, NOSE_Z), BOID_SEE_AHEAD_DISTANCE, 0xff0000)
    private val material = if (color == BOID_DEFAULT_COLOR) defaultMaterial else color.toMeshPhongMaterial()

    /*private*/ val obj3D = Mesh(geometry, material)

    fun addToScene(scene: Scene) {
        scene.add(obj3D)
        //scene.add(seeAheadHelper)
    }

    fun setSteerDirection(direction: Vector3) {
        motionState.applySteeringForce(direction)
    }

    fun preUpdate() {
        updateSeeAheadVector()
    }

    fun update(deltaT: Double) = with (obj3D) {
        motionState.update(deltaT)

        obj3D.position.copy(motionState.position)

        // TEMP orientation HACK
        obj3D.rotation.y = motionState.headingAngle
        obj3D.rotation.z = motionState.roll
    }

     private fun updateSeeAheadVector() {
         obj3D.getWorldDirection(worldDirection)
         seeAhead.set(0, 0, NOSE_Z + BOID_SEE_AHEAD_DISTANCE).applyMatrix4(obj3D.matrixWorld)
         seeAheadHelper.position.set(0, 0, NOSE_Z).applyMatrix4(obj3D.matrixWorld)
         seeAheadHelper.setDirection(worldDirection)

         val outOfScene = seeAhead.z >= HALF_SCENE_SIZE || seeAhead.z < -HALF_SCENE_SIZE ||
                          seeAhead.x >= HALF_SCENE_SIZE || seeAhead.x < -HALF_SCENE_SIZE

         seeAheadHelper.setColor(if (outOfScene) 0xff0000 else 0x00ff00)
     }

    private fun randomDirection() = Random.nextDouble()
}
