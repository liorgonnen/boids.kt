package boids

import boids.ext.*
import three.js.Face3
import three.js.Geometry
import three.js.Mesh
import three.js.Vector3
import kotlin.math.max
import kotlin.math.min

class Boid(position: Vector3 = Vector3(), angle: Double = 0.0, color: Int = BOID_DEFAULT_COLOR) : Object3DHolder() {

    val id = uniqueId()

    var position: Vector3
        get() = sceneObject.position
        private set(value) { sceneObject.position.copy(value) }

    private val material = if (color == BOID_DEFAULT_COLOR) defaultMaterial else color.toMeshPhongMaterial()

    override val sceneObject = Mesh(geometry, material).apply { castShadow = SHADOWS_ENABLED }

    val velocity = angle.toSpeedVector().multiplyScalar(BOID_MAX_VELOCITY)

    var headingAngle = angle
        private set

    var roll = 0.0
        private set

    private var targetRoll = 0.0

    private var steeringForce = Vector3()

    init {
        this.position = position
    }

    fun applySteeringForce(force: Vector3) {
        steeringForce.copy(force)
        if (steeringForce.isZero) velocity.multiplyScalar(1.1).clampLength(0, BOID_MAX_VELOCITY)
    }

    fun update(time: Double) {
        updateVelocityAndHeading(time)

        updateRoll(time)

        updateSceneObject(time)
    }

    private fun updateVelocityAndHeading(time: Double) {
        velocity.add(auxVector.copy(steeringForce).multiplyScalar(time)).clampLength(0, BOID_MAX_VELOCITY)
        headingAngle = velocity.asAngle()
    }

    private fun updateRoll(time: Double) {
        val deltaAngle = velocity.asAngle() - steeringForce.asAngle()
        val angleFraction = deltaAngle.coerceIn(-HALF_PI, HALF_PI) / HALF_PI
        val forceFraction = steeringForce.lengthSq() / BOID_MAX_VELOCITY_SQR
        targetRoll = angleFraction * forceFraction * BOID_MAX_ROLL

        if (roll < targetRoll) roll = min(roll + time, targetRoll)
        if (roll > targetRoll) roll = max(roll - time, targetRoll)
    }

    private fun updateSceneObject(time: Double) = with (sceneObject) {
        position.add(auxVector.copy(velocity).multiplyScalar(time))
        rotation.y = headingAngle
        rotation.z = roll
    }

    fun isInVisibleRange(target: Vector3, maxDistance: Double): Boolean {
        val halfVisibleRegionAngle = BOID_VISIBLE_REGION / 2.0
        val targetAngle = auxVector.subVectors(target, position).asAngle()
        val visibleRegionStartAngle = headingAngle - halfVisibleRegionAngle
        val visibleRegionEndAngle = headingAngle + halfVisibleRegionAngle

        inline fun targetInAngularRange() = targetAngle.isInAngleRange(visibleRegionStartAngle, visibleRegionEndAngle)
        inline fun targetInDistance() = position.distanceToSquared(target) <= maxDistance.sqr

        return targetInAngularRange() && targetInDistance()
    }

    fun distanceTo(other: Boid) = position.distanceTo(other.position).toDouble()

    companion object {

        private var count = 0

        private fun uniqueId() = count.apply { count++ }

        private val auxVector = Vector3()

        private const val NOSE_Z = 7.0

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
}
