package com.primogemstudio.advancedui.test

import glm_.vec2.Vec2
import glm_.vec4.Vec4
import imgui.Cond
import imgui.ImGui
import imgui.api.slider

class ImguiRender {
    companion object {
        @JvmField
        var SmoothEdge = 0f

        @JvmField
        var Radius = 20

        @JvmField
        var Thickness = 0f

        @JvmField
        var FrostGrass = false

        @JvmField
        var Pos = floatArrayOf(136f, 13f)

        @JvmField
        var Size = floatArrayOf(200f, 200f)

        @JvmField
        var Color = floatArrayOf(1f, 1f, 1f, 0.4f)

        @JvmStatic
        fun render(imgui: ImGui) {
            imgui.begin("Minecraft")
            imgui.setWindowSize(Vec2(320, 240), Cond.Once)
            imgui.text("Hello Minecraft!")
            imgui.slider("Radius", ::Radius, 1, 100)
            imgui.slider("Thickness", ::Thickness, 0.0f, 1.0f)
            imgui.slider("SmoothEdge", ::SmoothEdge, 0.0f, 1.0f)
            imgui.slider2("Pos", Pos, 0.0f, 500f)
            imgui.slider2("Size", Size, 0.0f, 400f)
            imgui.slider4("Color", Color, 0.0f, 1.0f)
            imgui.checkbox("FrostGrass", ::FrostGrass)
        }
    }
}