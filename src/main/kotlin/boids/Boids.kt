package boids

import boids.behaviors.*
import boids.ext.*
import three.js.*
import kotlin.browser.window
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.time.ExperimentalTime

@ExperimentalTime
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
        //RemainInSceneBoundariesBehavior,
        //WanderBehavior,
        SeparationBehavior,
        AlignmentBehavior,
        CohesionBehavior,
        //SeekBehavior,
    ))

    private val scene = Scene().apply {
        add(DirectionalLight(0xffffff, 1).apply { position.set(-1, 2, 4) })
        add(AmbientLight(0x404040, 1))

        flock.addToScene(this)

        add(gridHelper)

//        val r = HALF_SCENE_SIZE
//        val count = 30
//        for (angle in 0 until 360 step 360/count) {
//            val mesh = Mesh(CircleGeometry(2.0), MeshPhongMaterial().apply { color = Color(angle / 360.0 / 0.6 + 0.4, 0, 0) })
//            mesh.position.x = r * sin(angle.toRadians())
//            mesh.position.z = r * cos(angle.toRadians())
//            mesh.rotation.x = -PI / 2.0
//            console.log("a=$angle a*=${mesh.position.asAngle().toDegrees().roundToInt()}")
//            add(mesh)
//        }
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
