package boids.ext

import three.js.Vector3

val Vector3.isZero get() = x == 0 && y == 0 && z == 0

inline val Vector3.isNoneZero get() = !isZero