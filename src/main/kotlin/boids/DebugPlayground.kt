package boids

import boids.behaviors.*
import boids.ext.*
import three.js.*
import kotlin.browser.window
import kotlin.math.PI
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DebugPlayground {

    private val clock = Clock()

    private val flock = Flock(NUM_BOIDS, listOf(
            //RemainInSceneBoundariesBehavior,
            CollisionAvoidanceBehavior,
            //WanderBehavior,
            //SeparationBehavior,
            //AlignmentBehavior,
            //CohesionBehavior,
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

    private val textObjects = TextObjects(::onTextObjectsCreated)

    private val plane = Mesh(
            geometry = PlaneGeometry(SCENE_SIZE, SCENE_SIZE).apply { rotateX(-HALF_PI) },
            material = 0x222222.toMeshPhongMaterial().apply { flatShading = true }
    )

    private val light1 = DirectionalLight(0xffffff, 1).apply {
        position.set(0, 10, -HALF_SCENE_SIZE)
        target.position.set(5, 0, 0)
    }

    private val light2 = HemisphereLight(0xffffff, 0x666666, 0.8)

    private val boid = Boid(Vector3(-10, 1, 100), PI, 0x0000ff).apply { update(0.0) }

    private val scene = Scene().apply {
        add(light1, light2, plane, gridHelper)
        add(textObjects)

        add(flock)

        //add(boid)
    }

    init {
        window.onresize = {
            camera.onResize()
            renderer.onResize()
        }
    }

    private fun onTextObjectsCreated(what: Object3D) {
        textObjects.sceneObject.children.forEach { child ->
            CollisionAvoidanceBehavior.add(child)
        }
    }

    fun animate() {
        val time = clock.getDelta().toDouble()

        cameraAnimator.update(time)

        flock.update(time)

        val f = CollisionAvoidanceBehavior.getSteeringForce(boid, emptySequence())
        if (f.isNonZero) boid.applySteeringForce(f)

        //boid.update(time)

        renderer.render(scene, camera)

        window.requestAnimationFrame { animate() }
    }
}
