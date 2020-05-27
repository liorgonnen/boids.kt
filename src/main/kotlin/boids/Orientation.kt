package boids

class Orientation {
    private var accumulatedFraction = 0.0

    private var currentValue = 0.0
        set(value) {
            accumulatedFraction = 0.0
            field = value
        }

    private var targetValue = 0.0

    private var changeSpeed = 1.0

    fun update(fraction: Double) {
        if (accumulatedFraction < 1.0) accumulatedFraction += fraction
        if (accumulatedFraction > 1.0) accumulatedFraction = 1.0

        //currentValue +=
    }
}