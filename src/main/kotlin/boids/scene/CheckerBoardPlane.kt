package boids.scene

import boids.PLANE_COLOR1
import boids.PLANE_COLOR2
import boids.SCENE_SIZE
import boids.ext.HALF_PI
import boids.ext.toMeshPhongMaterial
import three.js.Color
import three.js.Mesh
import three.js.PlaneGeometry

object CheckerBoardPlane {

    private const val SEGMENTS = 16

    private val COLORS = arrayOf(Color(PLANE_COLOR1), Color(PLANE_COLOR2))

    fun create() = Mesh(
        geometry = PlaneGeometry(SCENE_SIZE, SCENE_SIZE, SEGMENTS, SEGMENTS).apply {
            rotateX(-HALF_PI)
            for (y in 0 until SEGMENTS)
                for (x in 0 until SEGMENTS)
                    for (f in 0 until 2)
                        faces[2 * (y * SEGMENTS + x) + f].color.set(COLORS[(x + y % 2) % 2])
        },
        material = 0xffffff.toMeshPhongMaterial().apply { flatShading = true; vertexColors = true }
    )
}