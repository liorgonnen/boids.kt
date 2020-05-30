package boids

import kotlin.math.PI

// Scene
const val SCENE_SIZE = 300.0
const val HALF_SCENE_SIZE = SCENE_SIZE / 2.0
const val CAMERA_NEAR = 0.1
const val CAMERA_FAR = SCENE_SIZE * 2

const val NUM_BOIDS = 50

// Must be smaller than [BOID_SEE_AHEAD_DISTANCE]
const val BOID_MIN_DISTANCE = 5.0

// Must be larger than [BOID_MIN_DISTANCE]
const val BOID_SEE_AHEAD_DISTANCE = 20.0

const val BOID_MIN_SPEED = 40.0
const val BOID_MAX_SPEED = 50.0
const val BOID_MAX_SPEED_SQR = BOID_MAX_SPEED * BOID_MAX_SPEED
const val BOID_MAX_ACCELERATION = 0.8 * BOID_MAX_SPEED
const val BOID_ROTATION_SPEED = PI / 2.0
const val BOID_VISIBLE_REGION = PI * 1.5

const val BOID_DEFAULT_COLOR = 0x0000ff

/**
 * The maximum angle delta a boid needs steer to while still accelerating.
 * A higher angle change would cause the boid to decelerate
 */
const val BOID_MAX_ACCELERATION_ANGLE = 30.0 / 180 * PI