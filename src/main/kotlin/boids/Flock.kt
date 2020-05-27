package boids

import boids.behaviors.Behavior
import boids.behaviors.SeparationBehavior
import boids.ext.*
import three.js.Scene
import three.js.Vector3

class Flock(private val numBoids: Int, private val behaviors: List<Behavior>) {

    private val count = 20

    private val boids = (0 until count).map { i ->
        val pos = Vector3(-HALF_SCENE_SIZE + i * (SCENE_SIZE / count), 0, HALF_SCENE_SIZE)
        val anchor = Vector3(0, 0, -HALF_SCENE_SIZE)
        Boid(position = pos, angle = anchor.sub(pos).asAngle())
    }.toTypedArray()
//    private val boids = arrayOf(
//            Boid(position = Vector3(0, 0, HALF_SCENE_SIZE), angle = 180.toRadians()),
//            Boid(position = Vector3(-20, 0, HALF_SCENE_SIZE), angle = 160.toRadians()),
//            Boid(position = Vector3(20, 0, HALF_SCENE_SIZE), angle = 200.toRadians()),
//    )

    private val boidNeighbors = BoidNeighborsSequence(boids)

    // Util ity to prevent object creation
    private val totalForce = Vector3()

    fun update(deltaT: Double) = boids.forEach nextBoid@ { boid ->
        totalForce.set(0, 0, 0)

        boid.preUpdate()

        // Reset the neighbors sequence to the next boid
        boidNeighbors.boid = boid

        behaviors.forEach nextBehavior@ {
            if (!it.isEffective(boid, boidNeighbors)) return@nextBehavior

            val force = it.getSteeringForce(boid, boidNeighbors)

            if (force.isZero) return@nextBehavior

            if (it.overridesLowerPriorityBehaviors) {
                boid.setSteerDirection(force)
                boid.update(deltaT)
                return@nextBoid
            }

            totalForce.add(force.multiplyScalar(it.weight))
        }

        if (totalForce.isNoneZero) boid.setSteerDirection(totalForce)

        boid.update(deltaT)
        boid.confineToScene()
    }

    fun addToScene(scene: Scene) = boids.forEach { it.addToScene(scene) }

    private fun Boid.confineToScene() {
        with (motionState.position) {
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
                    if (boids[index] !== boid && boid.motionState.isInVisibleRange(boids[index].position, distance)) {
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