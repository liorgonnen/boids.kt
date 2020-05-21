package boids.ext

import org.w3c.dom.Window

val Window.aspectRatio get() = this.innerWidth.toDouble() / this.innerHeight
