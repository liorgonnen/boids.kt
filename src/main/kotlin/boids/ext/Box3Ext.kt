package boids.ext

import three.js.Box3
import three.js.Vector3

private val auxVector = Vector3()
private val result = Vector3()

fun Box3.deflect(sourcePosition: Vector3, hitPoint: Vector3): Vector3 {
    result.zero()
    auxVector.subVectors(hitPoint, sourcePosition)

    if (auxVector.z > 0.0 && hitPoint.z == this.max.z) result.crossVectors(auxVector, Y_AXIS).also { console.log("Front impact") } // Approaching th wall from the front
    else if (auxVector.z < 0.0 && hitPoint.z == this.min.z) result.crossVectors(auxVector, NEGATIVE_Y_AXIS).also { console.log("Back impact") } // Approaching th wall from the front
    else if (auxVector.x > 0.0 && hitPoint.x == this.min.x) result.crossVectors(auxVector, Y_AXIS).also { console.log("Left side impact") } // Approaching wall from the left
    else if (auxVector.x < 0.0 && hitPoint.x == this.max.x) result.crossVectors(auxVector, NEGATIVE_Y_AXIS).also { console.log("Right side impact") } // Approaching wall from the left
    else console.log("NO impact")
    return result
}