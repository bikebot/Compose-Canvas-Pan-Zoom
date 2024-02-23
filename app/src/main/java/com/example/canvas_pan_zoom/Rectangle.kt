package com.example.canvas_pan_zoom

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Auxiliary operator for subtracting a size from an offset
operator fun Offset.minus(size: Size): Offset {
    return Offset(
        this.x - size.width,
        this.y - size.height
    )
}

class Rectangle(
    var center: Offset,
    var size: Size,
    var orientation: Float  // degrees, clockwise from 12 o'clock - SVG standard
) {
    fun draw(scope: DrawScope, selected: Boolean) {
        // Make rectangle accessible within transform scope
        val r = this

        scope.withTransform({
            rotate(
                degrees = r.orientation,
                pivot = r.center
            )
        }) {
            drawRect(
                color = if (selected) Color.Green else Color.Gray,
                topLeft = r.center - r.size / 2f,
                size = r.size
            )
        }
    }

    fun contains(pos: Offset): Boolean {
        val posInRect = pos - center // Position relative to rectangle center
        val orientationRad = orientation * PI / 180
        val posUnrotated = Offset(
            (posInRect.x * cos(orientationRad) + posInRect.y * sin(orientationRad)).toFloat(),
            (-posInRect.x * sin(orientationRad) + posInRect.y * cos(orientationRad)).toFloat()
        )
        return  posUnrotated.x in -size.width / 2 .. size.width / 2 &&
                posUnrotated.y in -size.height / 2 .. size.height / 2
    }
}