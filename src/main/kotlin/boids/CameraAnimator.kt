package boids

import boids.ext.setXZFromAngle
import three.js.Camera
import three.js.Vector3
import kotlin.math.PI

class CameraAnimator(private val camera: Camera) {

    companion object {
        private val CAMERA_HEIGHT = if (DEBUG) HALF_SCENE_SIZE * 2 else HALF_SCENE_SIZE / 3
    }

    private var cameraAngle = 0.0
    private val cameraLookAt = Vector3()

    fun update(time: Double) {
        cameraAngle += time / 15.0
        camera.position.setXZFromAngle(cameraAngle).multiplyScalar(HALF_SCENE_SIZE).setY(CAMERA_HEIGHT)
        cameraLookAt.setXZFromAngle(cameraAngle + PI / 2).multiplyScalar(HALF_SCENE_SIZE * 0.2)
        camera.lookAt(cameraLookAt.x, 0, cameraLookAt.z)
    }
}