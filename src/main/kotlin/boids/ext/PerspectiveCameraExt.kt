package boids.ext

import three.js.PerspectiveCamera
import kotlin.browser.window

inline fun PerspectiveCamera.onResize() {
    aspect = window.aspectRatio
    updateProjectionMatrix()
}
