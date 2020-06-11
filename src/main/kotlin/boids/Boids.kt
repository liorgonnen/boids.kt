package boids

import boids.behaviors.*
import boids.ext.add
import boids.ext.aspectRatio
import boids.ext.init
import boids.ext.onResize
import boids.scene.CheckerBoardPlane
import boids.scene.TextObjects
import stats.js.Stats
import stats.js.ext.measure
import three.js.*
import kotlin.browser.document
import kotlin.browser.window

class Boids {

    private val clock = Clock()

    private val flock = Flock(NUM_BOIDS, listOf(
        RemainInSceneBoundariesBehavior,
        CollisionAvoidanceBehavior,
        //WanderBehavior,
        SeparationBehavior,
        AlignmentBehavior,
        CohesionBehavior,
        //SeekBehavior,
    ))

    private val camera = PerspectiveCamera(75, window.aspectRatio, CAMERA_NEAR, CAMERA_FAR).apply {
        position.y = HALF_SCENE_SIZE
        position.z = HALF_SCENE_SIZE * 1.5
        lookAt(0.0, 0.0, 0.0)
    }

    private val cameraAnimator = CameraAnimator(camera)

    private val renderer = WebGLRenderer().init(
        enableShadows = SHADOWS_ENABLED,
        clearColor = SKY_COLOR
    )

    private val stats = Stats().apply {
        showPanel(0) // 0: fps, 1: ms, 2: mb, 3+: custom
        document.body?.appendChild(domElement)
        with (domElement.style) {
            position="fixed"
            top="0px"
            left="0px"
        }
    }

    private val textObjects = TextObjects(::onTextObjectsCreated)

    private val plane = CheckerBoardPlane.create()

    private val light1 = DirectionalLight(0xaaaaaa, 1).apply {
        position.set(0, HALF_SCENE_SIZE / 2, HALF_SCENE_SIZE / 4)
        castShadow = SHADOWS_ENABLED
        with (shadow.camera) {
            near = 0.5
            far = SCENE_SIZE
            left = -HALF_SCENE_SIZE
            bottom = -HALF_SCENE_SIZE
            right = HALF_SCENE_SIZE
            top = HALF_SCENE_SIZE
        }
    }

    private val light2 = HemisphereLight(0xffffff, 0x666666, 0.8)

    private val scene = Scene().apply {
        add(light1)
        add(light2)
        add(plane)
        add(textObjects)
        add(flock)
    }

    init {
        window.onresize = {
            camera.onResize()
            renderer.onResize()
        }
    }

    private fun onTextObjectsCreated(what: Object3D) {
        textObjects.sceneObject.children.forEach { CollisionAvoidanceBehavior.add(it) }
    }

    fun animate() {
        stats.measure {
            val time = clock.getDelta().toDouble()

            cameraAnimator.update(time, flock.centerOfGravity)

            flock.update(time)

            renderer.render(scene, camera)
        }

        window.requestAnimationFrame { animate() }
    }
}
