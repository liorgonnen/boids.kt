package boids.scene

import boids.BOID_SEE_AHEAD_DISTANCE
import boids.Boid
import boids.HALF_SCENE_SIZE
import boids.SCENE_SIZE
import three.js.Color
import three.js.MeshPhongMaterial
import three.js.Vector3

object SpatialBinLattice {

    private const val BIN_SIZE = BOID_SEE_AHEAD_DISTANCE * 0.75

    private const val NUM_BINS_PER_ROW = (SCENE_SIZE / BIN_SIZE).toInt()

    private val bins = Array(NUM_BINS_PER_ROW * NUM_BINS_PER_ROW) { BinItems() }

    // Items should only be added once for initialization and then updated using [update]
    fun add(boid: Boid) {
        val index = boid.position.toBinIndex()
        bins[index] += boid.binNode.apply { binIndex = index }
    }

    fun update(boid: Boid) {
        val node = boid.binNode
        val newIndex = boid.position.toBinIndex()
        val currentIndex = node.binIndex

        if (newIndex != currentIndex) {
            bins[currentIndex] -= node
            bins[newIndex] += node.apply { binIndex = newIndex }
        }
    }

    fun getSpatialNeighbors(boid: Boid) = NeighborsIterator.forIndex(boid.binNode.binIndex)

    private fun Vector3.toBinIndex(): Int {
        inline fun Number.toBinResolution() = ((toDouble() + HALF_SCENE_SIZE).coerceIn(0.0, SCENE_SIZE - 1) / BIN_SIZE).toInt()
        return y.toBinResolution() * NUM_BINS_PER_ROW + x.toBinResolution()
    }

    object NeighborsIterator : Iterator<Boid> {

        // Current bin to get the neighbors for
        private var index = 0

        // row and col range from -1 to 1 to get the surrounding bins
        private var col = 0
        private var row = 0

        // Indicates that we have more bins available to look for neighbors in
        private var moreBinsAvailable = true

        private var next: Boid? = null

        private var currentBinPointer: BinNode? = null

        private fun currentBinIndex() = index + row * NUM_BINS_PER_ROW + col

        fun forIndex(newIndex: Int) = apply {
            index = newIndex
        }

        fun rewind() = apply {
            currentBinPointer = null
            val indexRow = index / NUM_BINS_PER_ROW
            val indexCol = index % NUM_BINS_PER_ROW
            col = if (indexCol == 0) 0 else -1
            row = if (indexRow == 0) 0 else -1
            moreBinsAvailable = true
            next = null
        }

        private fun findNext() {
            next = null
            // If there's no current bin, find one
            while (moreBinsAvailable && currentBinPointer == null) {
                val index = currentBinIndex()
                // If we're out of bins, no reason to continue
                if (index >= bins.size) {
                    moreBinsAvailable = false
                    continue
                }

                // Check if index
                if (index >= 0 && moreBinsAvailable) currentBinPointer = bins[index].tail

                col++
                val indexCol = index % NUM_BINS_PER_ROW
                if (col > 1 || indexCol + col >= NUM_BINS_PER_ROW) {
                    col = if (indexCol == 0) 0 else -1
                    row++
                    if (row >= 2) moreBinsAvailable = false
                }
            }

            currentBinPointer?.let { node ->
                next = node.boid
                currentBinPointer = node.prev
            }

        }

        override fun hasNext(): Boolean {
            if (next == null && moreBinsAvailable) findNext()
            return next != null
        }

        override fun next(): Boid {
            if (next == null && moreBinsAvailable) findNext()

            val result = next ?: error("next called with no more items")

            next = null

            return result
        }

    }
}

class BinNode(
    val boid: Boid,
    internal var next: BinNode? = null,
    internal var prev: BinNode? = null,
    internal var binIndex: Int = -1
) {
    fun set(next: BinNode?, prev: BinNode?) = apply {
        this.next = next
        this.prev = prev
    }

    fun clearSiblings() = apply {
        prev = null
        next = null
    }
}

/**
 * Essentially a doubly linked list with O(1) add/remove operations that works with nodes directly
 * to eliminate any kind of traversal
 */
class BinItems {

    var tail: BinNode? = null
        private set

    operator fun plusAssign(node: BinNode) {
        if (tail == null) {
            tail = node.clearSiblings()
        } else {
            tail!!.next = node.set(prev = tail, next = null) // Using the not-null assertion operator to prevent bugs
            tail = tail!!.next
        }
    }

    /**
     * We need to get a reference to a specific node to remove. We don't want to do any traversal here
     * @param node The node to remove from the list
     */
    operator fun minusAssign(node: BinNode) {
        node.prev?.next = node.next
        node.next?.prev = node.prev
        if (node === tail) tail = node.prev
        node.clearSiblings()
    }
}