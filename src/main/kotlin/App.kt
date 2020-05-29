import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boids
import boids.DebugPlayground
import boids.Math
import boids.ext.*
import three.js.Vector
import three.js.Vector3
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Boids().animate()
    //DebugPlayground().animate()
}


