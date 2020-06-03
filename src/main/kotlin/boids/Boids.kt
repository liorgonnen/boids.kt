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

    private val cameraAnimator = CameraAnimator(camera)

    private val renderer = WebGLRenderer().init()

    private val gridHelper = GridHelper(SCENE_SIZE, 10, 0x606060, 0x666666).apply { position.y = 0.1 }

    private val textObjects = TextObjects()

    private val plane = Mesh(
        geometry = PlaneGeometry(SCENE_SIZE, SCENE_SIZE).apply { rotateX(-HALF_PI) },
        material = 0x222222.toMeshPhongMaterial().apply { flatShading = true }
    )

    private val directionalLight = DirectionalLight(0xffffff, 1).apply {
        position.set(0, 10, -HALF_SCENE_SIZE)
        target.position.set(5, 0, 0)
    }

    private val scene = Scene().apply {
        add(directionalLight)
        add(HemisphereLight(0xffffff, 0x666666, 0.8))

        add(plane)
        add(gridHelper)

        flock.addToScene(this)
        textObjects.addToScene(this)
    }

    init {
        window.onresize = {
            camera.onResize()
            renderer.onResize()
        }
    }

    fun animate() {
        val time = clock.getDelta().toDouble()

        cameraAnimator.update(time)

        flock.update(time)

        renderer.render(scene, camera)

        window.requestAnimationFrame { animate() }
    }
}
