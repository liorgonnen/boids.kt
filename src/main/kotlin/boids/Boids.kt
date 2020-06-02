package boids

import boids.behaviors.AlignmentBehavior
import boids.behaviors.CohesionBehavior
import boids.behaviors.RemainInSceneBoundariesBehavior
import boids.behaviors.SeparationBehavior
import boids.ext.*
import three.js.*
import kotlin.browser.window
import kotlin.math.PI
import kotlin.time.ExperimentalTime

@ExperimentalTime
class Boids {

    private val clock = Clock()

    private val flock = Flock(NUM_BOIDS, listOf(
        //RemainInSceneBoundariesBehavior,
        //WanderBehavior,
        SeparationBehavior,
        AlignmentBehavior,
        CohesionBehavior,
        //SeekBehavior,
    ))

    private val camera = PerspectiveCamera(75, window.aspectRatio, CAMERA_NEAR, CAMERA_FAR).apply {
        position.y = HALF_SCENE_SIZE * 0.5
        position.z = HALF_SCENE_SIZE
        lookAt(0.0, 0.0, 0.0)
    }

    private var cameraAngle = 0.0
    private val cameraLookAt = Vector3()

    private val renderer = WebGLRenderer().init()

    private val gridHelper = GridHelper(SCENE_SIZE, 10, 0x444444, 0x888888)

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
        val time = clock.getDelta().toDouble()

        updateCamera(time)

        flock.update(time)

        renderer.render(scene, camera)

        window.requestAnimationFrame { animate() }
    }

    private fun updateCamera(time: Double) {
        cameraAngle += time / 20.0
        camera.position.setXZFromAngle(cameraAngle).multiplyScalar(HALF_SCENE_SIZE).setY(HALF_SCENE_SIZE * 0.2)
        cameraLookAt.setXZFromAngle(cameraAngle + PI / 2).multiplyScalar(HALF_SCENE_SIZE * 0.2)
        camera.lookAt(cameraLookAt.x, 0, cameraLookAt.z)
    }
}
