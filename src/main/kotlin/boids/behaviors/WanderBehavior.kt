package boids.behaviors

import boids.Boid
import three.js.Vector3
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

@ExperimentalTime
object WanderBehavior : Behavior() {

    private val timeMark = TimeSource.Monotonic.markNow()
    private var lastUpdateTime = timeNow()

    private fun timeNow() = timeMark.elapsedNow().inMilliseconds

    override fun isEffective(boid: Boid): Boolean {
        val timeNow = timeNow()
        val effective = timeNow - lastUpdateTime > 500L
        if (effective) lastUpdateTime = timeNow

        return effective
    }

    override fun computeSteeringForce(boid: Boid, neighbors: Array<Boid>, result: Vector3) {
//        var angle = boid.motionState.headingAngle
//        angle += randomAngle(30.0.toRadians())
//        setXZFromAngle(angle)
    }
}
