package boids.ext

import three.js.Color
import three.js.ShadowMapType
import three.js.WebGLRenderer
import kotlin.browser.document
import kotlin.browser.window

inline fun WebGLRenderer.onResize() {
    setSize(window.innerWidth, window.innerHeight)
}

fun WebGLRenderer.init(
    enableShadows: Boolean = false,
    appendDomElement: Boolean = true,
    clearColor: Int = 0
) = apply {
    setSize(window.innerWidth, window.innerHeight)
    setPixelRatio(window.devicePixelRatio)

    if (appendDomElement) document.body?.appendChild(domElement)
    if (enableShadows) shadowMapEnabled = true
    if (clearColor != 0) setClearColor(Color(clearColor))
}