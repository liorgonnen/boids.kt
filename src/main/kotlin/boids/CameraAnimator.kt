package boids

import boids.ext.setXZFromAngle
import three.js.Camera
import three.js.Vector3
import kotlin.math.PI

class CameraAnimator(private val camera: Camera) {

    private var cameraAngle = 0.0
    private val cameraLookAt = Vector3()

    fun update(time: Double) {
        cameraAngle += time / 20.0
        camera.position.setXZFromAngle(cameraAngle).multiplyScalar(HALF_SCENE_SIZE).setY(HALF_SCENE_SIZE * 0.2)
        cameraLookAt.setXZFromAngle(cameraAngle + PI / 2).multiplyScalar(HALF_SCENE_SIZE * 0.2)
        camera.lookAt(cameraLookAt.x, 0, cameraLookAt.z)
    }
}