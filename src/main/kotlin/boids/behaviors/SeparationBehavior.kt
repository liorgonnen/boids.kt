package boids.behaviors

import boids.BOID_MIN_DISTANCE
import boids.Boid
import boids.ext.compareTo
import boids.ext.sqr
import boids.ext.zero
import three.js.Vector3
import kotlin.math.PI
import kotlin.math.roundToInt

object SeparationBehavior : Behavior() {

    private val direction = Vector3()

    //private const val decayCoefficient = 0.001

    override val weight = 0.5

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        zero()

        neighbors.iterator().forEach { neighbor ->
            val distanceSqr = neighbor.position.distanceToSquared(boid.position).toDouble()
            if (distanceSqr != 0.0 &&
                distanceSqr < BOID_MIN_DISTANCE.sqr &&
                neighbor.position.angleTo(boid.position) <= PI / 2.0) {

                direction.copy(boid.position).sub(neighbor.position)
                result.add(direction.normalize())
            }
        }
    }
}