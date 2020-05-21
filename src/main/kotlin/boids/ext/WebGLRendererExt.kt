package boids.ext

import three.js.WebGLRenderer
import kotlin.browser.document
import kotlin.browser.window

inline fun WebGLRenderer.onResize() {
    setSize(window.innerWidth, window.innerHeight)
}

fun WebGLRenderer.init(appendDomElement: Boolean = true): WebGLRenderer {
    if (appendDomElement) document.body?.appendChild(domElement)

    setSize(window.innerWidth, window.innerHeight)
    setPixelRatio(window.devicePixelRatio)
    return this
}