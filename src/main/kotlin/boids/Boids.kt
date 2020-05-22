package boids

import boids.ext.*
import three.js.*
import kotlin.browser.window

class Boids {

    private val clock = Clock()
    private val camera = PerspectiveCamera(75, window.aspectRatio, CAMERA_NEAR, CAMERA_FAR).apply {
        position.y = HALF_SCENE_SIZE
        position.z = SCENE_SIZE
        lookAt(0.0, 0.0, 0.0)
    }

    private val renderer = WebGLRenderer().init()

    private val gridHelper = GridHelper(SCENE_SIZE, 10, 0x444444, 0x888888)

    private val flock = Flock(NUM_BOIDS, listOf(
        RemainInSceneBoundariesBehavior,
        AlignmentBehavior,
        CohesionBehavior,
        //SeekBehavior,
    ))

    private val scene = Scene().apply {
        add(DirectionalLight(0xffffff, 1).apply { position.set(-1, 2, 4) })
        add(AmbientLight(0x404040, 1))

        flock.addToScene(this)

        add(gridHelper)
    }

    init {
        window.onresize = {
            camera.onResize()
            renderer.onResize()
        }
    }

    fun animate() {
        val deltaT = clock.getDelta().toDouble()

        flock.update(deltaT)

        renderer.render(scene, camera)

        window.requestAnimationFrame { animate() }
    }
}
