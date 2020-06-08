package boids

import boids.ext.boundingBox
import boids.ext.plusAssign
import boids.ext.toMeshPhongMaterial
import three.js.*

class TextObjects(onChange: ChangeCallback? = null) : Object3DHolder(onChange) {

    companion object {
        const val FONT = "fonts/font_montserrat_black.json"
    }

    override val sceneObject = Group()

    init {
        FontLoader().apply { load(FONT, onLoad = ::onFontLoaded) }
    }

    private fun onFontLoaded(font: Font) {
        sceneObject += Mesh(createTextGeometry("Boids", font, HALF_SCENE_SIZE / 8, 10), 0xff00ff.toMeshPhongMaterial()).apply {
            position.set(-boundingBox.max.x.toDouble() / 2, -boundingBox.min.y.toDouble(), 0)
        }

        notifySceneObjectUpdated()
    }

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
        computeBoundingBox()
    }
}