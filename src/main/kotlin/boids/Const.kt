package boids

import kotlin.math.PI

const val DEBUG = false

// Scene
const val SCENE_SIZE = 400.0
const val HALF_SCENE_SIZE = SCENE_SIZE / 2.0
const val CAMERA_NEAR = 0.1
const val CAMERA_FAR = SCENE_SIZE * 4

const val NUM_BOIDS = 100

// Must be smaller than [BOID_SEE_AHEAD_DISTANCE]
const val BOID_MIN_DISTANCE = 10.0

// Must be larger than [BOID_MIN_DISTANCE]
const val BOID_SEE_AHEAD_DISTANCE = 40.0

const val BOID_MIN_VELOCITY = 7.0
const val BOID_MAX_VELOCITY = 40.0
const val BOID_MAX_ACCELERATION = 0.5 * BOID_MAX_VELOCITY
const val BOID_VISIBLE_REGION = PI * 0.75
const val BOID_MAX_ROLL = 45.0 / 180.0 * PI

const val BOID_DEFAULT_COLOR = 0x0000ff

