package boids

import boids.behaviors.*
import boids.ext.*
import boids.scene.CheckerBoardPlane
import boids.scene.TextObjects
import three.js.*
import kotlin.browser.window

class Boids {

    private val clock = Clock()

    private val flock = Flock(NUM_BOIDS, listOf(
        RemainInSceneBoundariesBehavior,
        //CollisionAvoidanceBehavior,
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

    private val renderer = WebGLRenderer().init(enableShadows = true, clearColor = SKY_COLOR)

    private val textObjects = TextObjects(::onTextObjectsCreated)

    private val plane = CheckerBoardPlane.create()

    private val light1 = DirectionalLight(0xaaaaaa, 1).apply {
        position.set(0, HALF_SCENE_SIZE / 2, HALF_SCENE_SIZE / 4)
        castShadow = SHADOWS_ENABLED
        shadow.camera.near = 0.5
        shadow.camera.far = SCENE_SIZE
        shadow.camera.left = -HALF_SCENE_SIZE
        shadow.camera.bottom = -HALF_SCENE_SIZE
        shadow.camera.right = HALF_SCENE_SIZE
        shadow.camera.top = HALF_SCENE_SIZE
//        shadow.mapSize.width = 1024
//        shadow.mapSize.height = 1024
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
        val time = clock.getDelta().toDouble()

        cameraAnimator.update(time, flock.centerOfGravity)

        flock.update(time)

        renderer.render(scene, camera)

        window.requestAnimationFrame { animate() }
    }
}
