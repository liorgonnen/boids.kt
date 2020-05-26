package boids

import boids.behaviors.Behavior
import boids.ext.compareTo
import boids.ext.isNoneZero
import boids.ext.isZero
import boids.ext.minus
import three.js.Scene
import three.js.Vector3
import kotlin.math.abs

class Flock(private val numBoids: Int, private val behaviors: List<Behavior>) {

    private val boids = Array(numBoids) { Boid() }

    private val boidNeighbors = BoidNeighborsSequence(boids)

    // Utility to prevent object creation
    private val totalForce = Vector3()

    fun update(deltaT: Double) = boids.forEach nextBoid@ { boid ->
        totalForce.set(0, 0, 0)

        boid.preUpdate()

        // Reset the neighbors sequence to the next boid
        boidNeighbors.boid = boid

        behaviors.forEach nextBehavior@ {
            if (!it.isEffective(boid, boidNeighbors)) return@nextBehavior

            val force = it.getSteeringForce(boid, boidNeighbors).normalize()

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
    }

    fun addToScene(scene: Scene) = boids.forEach { it.addToScene(scene) }
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

        private var distanceSquared = distance * distance

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
                    if (boids[index] !== boid &&
                        boids[index].position.distanceToSquared(boid.position) <= distanceSquared) {

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