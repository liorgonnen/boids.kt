package boids.behaviors

import boids.Boid
import boids.ext.asAngle
import boids.ext.randomAngle
import boids.ext.setXZFromAngle
import boids.ext.toRadians
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

@ExperimentalTime
object WanderBehavior : Behavior() {

    private val timeMark = TimeSource.Monotonic.markNow()
    private var lastUpdateTime = timeNow()

    private fun timeNow() = timeMark.elapsedNow().inMilliseconds

    override fun isEffective(boid: Boid, neighbors: Sequence<Boid>): Boolean {
        val timeNow = timeNow()
        val effective = timeNow - lastUpdateTime > 500L
        if (effective) lastUpdateTime = timeNow

        return effective
    }

    override fun getSteeringForce(boid: Boid, neighbors: Sequence<Boid>) = result.apply {
        var angle = boid.motionState.headingAngle
        angle += randomAngle(30.0.toRadians())
        setXZFromAngle(angle)
    }
}
