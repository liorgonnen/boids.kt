@file:JsQualifier("THREE")
@file:Suppress("ABSTRACT_MEMBER_NOT_IMPLEMENTED", "VAR_TYPE_MISMATCH_ON_OVERRIDE", "INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION", "PackageDirectoryMismatch")
package three.js

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

open external class DirectionalLightHelper : Object3D {
    constructor(light: DirectionalLight, size: Number, color: Color)
    constructor(light: DirectionalLight, size: Number, color: String)
    constructor(light: DirectionalLight, size: Number, color: Number)
    open var light: DirectionalLight
    open var lightPlane: Line
    open var targetLine: Line
    open var color: dynamic /* Color? | String? | Number? */
    override var matrix: Matrix4
    override var matrixAutoUpdate: Boolean
    open fun dispose()
    open fun update()
}