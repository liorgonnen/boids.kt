package boids

import boids.ext.*
import three.js.*
import kotlin.browser.window
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DebugPlayground {

    private val clock = Clock()
    private val camera = PerspectiveCamera(75, window.aspectRatio, CAMERA_NEAR, CAMERA_FAR).apply {
        position.y = HALF_SCENE_SIZE
        position.z = SCENE_SIZE
        lookAt(0.0, 0.0, 0.0)
    }

    private val renderer = WebGLRenderer().init()

    private val gridHelper = GridHelper(SCENE_SIZE, 10, 0x444444, 0x888888)

    private val indicatorCircles = IndicatorCircles()

    private val boid = Boid(position = Vector3(0, 0, 0), angle = 180.toRadians())

//    private val flock = Flock(NUM_BOIDS, listOf(
//            RemainInSceneBoundariesBehavior,
//            //WanderBehavior,
//            SeparationBehavior,
//            //AlignmentBehavior,
//            //CohesionBehavior,
//            //SeekBehavior,
//    ))

    private val scene = Scene().apply {
        add(DirectionalLight(0xffffff, 1).apply { position.set(-1, 2, 4) })
        add(AmbientLight(0x404040, 1))

        //flock.addToScene(this)
        indicatorCircles.addToScene(this)
        boid.addToScene(this)

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

        //flock.update(deltaT)

        renderer.render(scene, camera)

        window.requestAnimationFrame { animate() }

        boid.motionState.update(deltaT)
        boid.obj3D.rotation.y += deltaT

        val t = Vector3(0, 0, 1)
        val d = BOID_SEE_AHEAD_DISTANCE
        for (a in 0 until 360) {
            t.z = d * cos(a.toRadians())
            t.x = d * sin(a.toRadians())
            val visible = Math.isInVisibleRange(boid.obj3D.rotation.y.toDouble(), boid.position, t, d * 1.1)
            indicatorCircles[a] = if (visible) 0x00ff00 else 0xff0000
        }
    }
}

class IndicatorCircles() {

    private val circles = Array<Mesh>(360, ::createCircle)

    fun addToScene(scene: Scene) = circles.forEach { scene.add(it) }

    operator fun set(index: Int, color: Int) {
        (circles[index].material as MeshPhongMaterial).color = Color(color)
    }

    private fun createCircle(index: Int)
        = Mesh(CircleGeometry(0.4), MeshPhongMaterial().apply { color = Color(0x0000ff) }).apply {
            position.x = HALF_SCENE_SIZE * sin(index.toRadians())
            position.z = HALF_SCENE_SIZE * cos(index.toRadians())
            rotation.x = -PI / 2.0
        }
}
