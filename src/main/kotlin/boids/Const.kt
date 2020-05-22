package boids

import kotlin.math.PI

// Scene
const val SCENE_SIZE = 100.0
const val HALF_SCENE_SIZE = SCENE_SIZE / 2.0
const val CAMERA_NEAR = 0.1
const val CAMERA_FAR = SCENE_SIZE * 2

const val NUM_BOIDS = 50

const val BOID_MIN_SPEED = 10.0
const val BOID_MAX_SPEED = 20.0
const val BOID_ROTATION_SPEED = 1.5
const val BOID_ACCELERATION = 0.2 * BOID_MAX_SPEED
const val BOID_SEE_AHEAD_DISTANCE = 8.0

/**
 * The maximum angle delta a boid needs steer to while still accelerating.
 * A higher angle change would cause the boid to decelerate
 */
const val BOID_MAX_ACCELERATION_ANGLE = 30.0 / 180 * PI