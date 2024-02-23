package com.example.canvas_pan_zoom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PanZoomCanvas(rect: Rectangle) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableFloatStateOf(1f) }
    var selected: Rectangle? by remember { mutableStateOf(null) }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
        .pointerInput(Unit) {
            detectTapGestures(onTap = { tap ->
                // Transform tap pos to local coordinates
                val tap1 = (tap - offset) / scale

                // Did we hit the rectangle?
                selected = if (rect.contains(tap1)) rect else null
            })
        }
        .pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, _ ->
                scale *= zoom
                offset += (centroid - offset) * (1f - zoom) + pan
            }
        }
    ) {
        // Canvas does not clip by itself!
        clipRect {
            withTransform({
                translate(offset.x, offset.y)
                scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
            }) {
                rect.draw(scope = this, selected = selected == rect)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 500)
@Composable
fun CanvasPreview() {
    PanZoomCanvas(
        rect = Rectangle(
            center = Offset(300f, 100f),
            size = Size(200f, 100f),
            orientation = 0f
        )
    )
}