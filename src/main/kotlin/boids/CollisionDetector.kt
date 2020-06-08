package boids

import boids.ext.*
import three.js.Box3
import three.js.Ray
import three.js.Vector
import three.js.Vector3
import kotlin.math.PI

object CollisionDetector {

    private val ANGLE_CHANGE = 10.toRadians()

    private val box = Box3()

    private val ray = Ray()

    private val direction = Vector3()

    private val intersectionPoint = Vector3()

    fun collidesAny(sourcePosition: Vector3, sourceDirection: Double, maxDistance: Double, obstacles: Iterable<Box3>)
            = obstacles.any { collides(sourcePosition, sourceDirection, maxDistance, it) }

    fun collidesAny(sourcePosition: Vector3, sourceDirection: Double, maxDistance: Double, vararg obstacles: Box3)
        = obstacles.any { collides(sourcePosition, sourceDirection, maxDistance, it) }

    fun collides(sourcePosition: Vector3, sourceDirection: Double, maxDistance: Double, obstacle: Box3): Boolean {
        direction.setXZFromAngle(sourceDirection).normalize()
        ray.set(sourcePosition, direction)
        box.copy(obstacle)

        val intersects = ray.intersectBox(box, intersectionPoint) != null

        return (intersects && intersectionPoint.distanceToSquared(sourcePosition) < maxDistance.sqr)
    }

    fun findEvasionAngle(sourcePosition: Vector3, sourceDirection: Double, maxDistance: Double, obstacles: Iterable<Box3>): Double {
        var angleShift = 0.0

        if (collidesAny(sourcePosition, sourceDirection + angleShift, maxDistance, obstacles) && angleShift < PI) {
            //angleShift = if (angleShift > 0) -angleShift else -angleShift + ANGLE_CHANGE
            angleShift = -PI / 2.0
        }

        return angleShift
    }
}
