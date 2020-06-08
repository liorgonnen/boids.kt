package boids.behaviors

import boids.*
import boids.ext.*
import three.js.Box3
import three.js.Vector3
import kotlin.math.PI

object RemainInSceneBoundariesBehavior : AbsCollisionAvoidanceBehavior() {

    private const val WALL_THICKNESS = 100.0

    private const val CENTER_POINT = HALF_SCENE_SIZE + WALL_THICKNESS / 2
    private val LEFT_CENTER = Vector3(-CENTER_POINT, 0, 0)
    private val RIGHT_CENTER = Vector3(CENTER_POINT, 0, 0)
    private val TOP_CENTER = Vector3(0, 0, -CENTER_POINT)
    private val BOTTOM_CENTER = Vector3(0, 0, CENTER_POINT)

    private val H_SIZE = Vector3(SCENE_SIZE + WALL_THICKNESS * 2, SCENE_SIZE, WALL_THICKNESS)
    private val V_SIZE = Vector3(WALL_THICKNESS, SCENE_SIZE, SCENE_SIZE)

    private val LEFT_WALL = Box3().setFromCenterAndSize(LEFT_CENTER, V_SIZE)
    private val RIGHT_WALL = Box3().setFromCenterAndSize(RIGHT_CENTER, V_SIZE)
    private val TOP_WALL = Box3().setFromCenterAndSize(TOP_CENTER, H_SIZE)
    private val BOTTOM_WALL = Box3().setFromCenterAndSize(BOTTOM_CENTER, H_SIZE)

    init {
        arrayOf(LEFT_WALL, RIGHT_WALL, TOP_WALL, BOTTOM_WALL).forEach { add(it) }
    }
}
