package boids

import boids.behaviors.Behavior
import boids.behaviors.SteeringForce
import boids.ext.*
import three.js.Scene
import three.js.Vector3
import kotlin.math.min

class Flock(private val numBoids: Int, private val behaviors: List<Behavior>) {

    private val boids = (0 until numBoids).map { i ->
        val pos = Vector3(-HALF_SCENE_SIZE + i * (SCENE_SIZE / NUM_BOIDS), 3.0, HALF_SCENE_SIZE - i)
        val anchor = Vector3(0, 0, -HALF_SCENE_SIZE)
        Boid(position = pos, angle = anchor.sub(pos).asAngle())
    }.toTypedArray()

//    private val boids = arrayOf(
//        Boid(position = Vector3(-20, 0, HALF_SCENE_SIZE / 2), angle = 160.toRadians()),
//        Boid(position = Vector3(0, 0, HALF_SCENE_SIZE / 2), angle = 180.toRadians(), color = 0x00ff00),
//        Boid(position = Vector3(20, 0, HALF_SCENE_SIZE / 2), angle = 200.toRadians()),
//    )

    private val boidNeighbors = BoidNeighborsSequence(boids)

    // Util ity to prevent object creation
    private val totalForce = SteeringForce()

    fun update(deltaT: Double) = boids.forEach nextBoid@ { boid ->
        totalForce.zero()

        boid.preUpdate()

        // Reset the neighbors sequence to the next boid
        boidNeighbors.boid = boid

        var totalWeight = 0.0
        behaviors.forEach nextBehavior@ {
            if (!it.isEffective(boid, boidNeighbors)) return@nextBehavior

            val force: SteeringForce = it.getSteeringForce(boid, boidNeighbors)
            if (force.isZero) return@nextBehavior

            if (it.overridesLowerPriorityBehaviors) {
                boid.applySteeringForce(force)
                boid.update(deltaT)
                return@nextBoid
            }

            // Taking 1 as the minimum behavior weight, allows each weight to have a relative weight and still maintain
            // this weight in a normalized vector even if its the only active force
            totalWeight += min(1.0, it.weight)

            totalForce.add(force.multiplyScalar(it.weight))
        }

        if (totalForce.isNonZero) totalForce.divideScalar(totalWeight)

        boid.applySteeringForce(totalForce)

        boid.update(deltaT)
        boid.confineToScene()
    }

    fun addToScene(scene: Scene) = boids.forEach { it.addToScene(scene) }

    private fun Boid.confineToScene() {
        with (position) {
            if (x < -HALF_SCENE_SIZE) x = HALF_SCENE_SIZE
            if (x > HALF_SCENE_SIZE) x = -HALF_SCENE_SIZE
            if (z < -HALF_SCENE_SIZE) z = HALF_SCENE_SIZE
            if (z > HALF_SCENE_SIZE) z = -HALF_SCENE_SIZE
        }
    }
}

private class BoidNeighborsSequence(val boids: Array<Boid>, val distance: Double = BOID_SEE_AHEAD_DISTANCE) : Sequence<Boid> {

    private val iter = BoidNeighborsIterator()

    var boid: Boid? = null
        set(value) {
            field = value
            iter.reset()
        }

    override fun iterator() = iter

    private inner class BoidNeighborsIterator : Iterator<Boid> {

        private var nextIndex = -1

        private var index = 0

        private val size = boids.size

        fun reset() {
            index = 0
            nextIndex = -1
            findNext()
        }

        private fun findNext() {
            nextIndex = -1
            boid?.let { boid ->
                while (index < size && nextIndex == -1) {
                    if (boids[index] !== boid && boid.isInVisibleRange(boids[index].position, distance)) {
                        nextIndex = index
                    }

                    index++
                }
            }
        }

        override fun hasNext() = (nextIndex != -1)

        override fun next(): Boid {
            if (index >= size && nextIndex == -1) throw NoSuchElementException()

            return boids[nextIndex].also { findNext() }
        }

    }

}