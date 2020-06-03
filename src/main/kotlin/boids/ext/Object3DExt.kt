package boids.ext

import three.js.Object3D

inline operator fun Object3D.plusAssign(child: Object3D) { add(child) }
