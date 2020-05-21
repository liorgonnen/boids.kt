package boids

import boids.ext.compareTo
import three.js.Scene
import three.js.Vector3

class Flock(private val numBoids: Int, private val behaviors: List<Behavior>) {

    private val boids = Array(numBoids) { Boid() }

    // Utility to prevent object creation
    private val totalForce = Vector3()

    fun update(deltaT: Double) {
        totalForce.set(0, 0, 0)

        boids.forEach { boid ->
            behaviors.forEach { behavior ->
                if (behavior.isEffective(boid, boids)) totalForce.add(behavior.getSteeringForce(boid, boids))
            }

            if (totalForce.length() > 0.0) boid.setSteerDirection(totalForce)

            boid.update(deltaT)
        }
    }

    fun addToScene(scene: Scene) = boids.forEach { scene.add(it.obj3D) }
}