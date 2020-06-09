package boids

import boids.ext.compareTo
import boids.ext.setXZFromAngle
import boids.ext.sqr
import three.js.Box3
import three.js.Ray
import three.js.Vector3

object CollisionDetector {

    private val box = Box3()

    private val ray = Ray()

    private val direction = Vector3()

    private val auxIntersectionPoint = Vector3()

    fun collides(
        sourcePosition: Vector3,
        sourceDirection: Vector3,
        maxDistance: Double,
        obstacle: Box3, intersectionPoint: Vector3? = null,
    ): Boolean {

        direction.copy(sourceDirection).normalize()
        ray.set(sourcePosition, direction)
        box.copy(obstacle)

        val intersection = intersectionPoint ?: auxIntersectionPoint

        val intersects = ray.intersectBox(box, intersection) != null

        return (intersects && intersection.distanceToSquared(sourcePosition) < maxDistance.sqr)
    }
}
