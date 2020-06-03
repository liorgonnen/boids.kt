package boids

import boids.ext.plusAssign
import boids.ext.toMeshPhongMaterial
import three.js.*

class TextObjects {

    companion object {
        const val FONT = "fonts/font_montserrat_black.json"
    }

    private val textGroup = Group()

    init {
        FontLoader().apply { load(FONT, onLoad = ::onFontLoaded) }
    }

    private fun onFontLoaded(font: Font) {
        textGroup += Mesh(createTextGeometry("Boids", font, HALF_SCENE_SIZE / 8, 10), 0xff00ff.toMeshPhongMaterial())
    }

    fun addToScene(scene: Scene) = scene.add(textGroup)

    private fun createTextGeometry(
            text: String,
            font: Font,
            size: Number? = null,
            thickness: Number? = null,
    ) = TextGeometry(text, object : TextGeometryParameters {
        override var font = font
        override var size = size
        override var height = thickness

    }).apply {
        computeFaceNormals()
        computeFlatVertexNormals()
    }
}