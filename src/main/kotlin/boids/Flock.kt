package boids

import boids.behaviors.Behavior
import boids.ext.*
import three.js.Group
import three.js.Vector3
import kotlin.math.min

class Flock(numBoids: Int, private val behaviors: List<Behavior>) : Object3DHolder() {

    override val sceneObject = Group()

    private val boids = (0 until numBoids).map { i ->
        val pos = Vector3(-HALF_SCENE_SIZE + i * (SCENE_SIZE / NUM_BOIDS), 3.0, HALF_SCENE_SIZE - i)
        val anchor = Vector3(0, 0, -HALF_SCENE_SIZE)
        Boid(position = pos, angle = anchor.sub(pos).asAngle())
    }.toTypedArray()

//    private val boids = arrayOf(
//        Boid(position = Vector3(-10, 3, HALF_SCENE_SIZE - 1), angle = 160.toRadians()),
//        Boid(position = Vector3(0, 3, HALF_SCENE_SIZE), angle = 180.toRadians(), color = 0x00ff00),
//        Boid(position = Vector3(10, 3, HALF_SCENE_SIZE - 3), angle = 200.toRadians()),
//    )

    private val totalForce = Vector3() // Utility to prevent object creation

    init {
        boids.forEach { sceneObject.add(it) }
    }

    operator fun get(index: Int) = boids[index]

    private val _centerOfGravity = Vector3()
    val centerOfGravity get(): Vector3 {
        _centerOfGravity.zero()
        boids.forEach { _centerOfGravity.add(it.position) }
        _centerOfGravity.divideScalar(boids.size)
        return _centerOfGravity
    }

    fun update(time: Double) = boids.forEach nextBoid@ { boid ->
        totalForce.zero()

        var totalWeight = 0.0
        behaviors.forEach nextBehavior@ {
            if (!it.isEffective(boid)) return@nextBehavior

            val force = it.getSteeringForce(boid, boids)
            if (force.isZero) return@nextBehavior

            if (it.overridesLowerPriorityBehaviors) {
                boid.applySteeringForce(force)
                boid.update(time)
                return@nextBoid
            }

            // Taking 1 as the minimum behavior weight, allows each weight to have a relative weight and still maintain
            // this weight in a normalized vector even if its the only active force
            totalWeight += min(1.0, it.weight)

            totalForce.add(force.multiplyScalar(it.weight))
        }

        if (totalWeight > 0.0) totalForce.divideScalar(totalWeight)

        boid.applySteeringForce(totalForce)

        boid.update(time)
        //boid.confineToScene()
    }

    private fun Boid.confineToScene() {
        with (position) {
            if (x < -HALF_SCENE_SIZE) x = HALF_SCENE_SIZE
            if (x > HALF_SCENE_SIZE) x = -HALF_SCENE_SIZE
            if (z < -HALF_SCENE_SIZE) z = HALF_SCENE_SIZE
            if (z > HALF_SCENE_SIZE) z = -HALF_SCENE_SIZE
        }
    }
}