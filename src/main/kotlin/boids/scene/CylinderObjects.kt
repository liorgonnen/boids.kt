package boids.scene

import boids.*
import boids.ext.toMeshPhongMaterial
import three.js.CylinderGeometry
import three.js.Group
import three.js.Mesh
import kotlin.random.Random

class CylinderObjects: Object3DHolder() {

    companion object {
        private const val RADIUS = 5.0
        private const val SEGMENTS = 20
        private const val BINS = 4
        private const val BIN_SIZE = SCENE_SIZE / BINS
    }

    override val sceneObject = Group()

    init {
        createObjects()
    }

    private fun createObjects() {
        for (z in 0 until BINS)
            for (x in 0 until BINS)
                createCylinder(-HALF_SCENE_SIZE + (x * BIN_SIZE) + random(), -HALF_SCENE_SIZE + z * BIN_SIZE + random())
    }

    private fun random() = Random.nextDouble(RADIUS * 4, BIN_SIZE - RADIUS * 4)

    private fun createCylinder(posX: Double, posZ: Double) {
        val geometry = CylinderGeometry(RADIUS, RADIUS, height = Random.nextDouble(HALF_SCENE_SIZE / 10, HALF_SCENE_SIZE / 4), SEGMENTS)
        val material = CYLINDER_COLOR.toMeshPhongMaterial()
        val mesh = Mesh(geometry, material).apply {
            position.set(posX, 0, posZ)
            castShadow = SHADOWS_ENABLED
        }
        sceneObject.add(mesh)
    }
}