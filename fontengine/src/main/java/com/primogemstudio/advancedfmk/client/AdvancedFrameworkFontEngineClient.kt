package com.primogemstudio.advancedfmk.client

import com.primogemstudio.advancedfmk.fontengine.gen.FreeTypeFont
import net.fabricmc.api.ClientModInitializer
import org.lwjgl.util.harfbuzz.HarfBuzz.*

class AdvancedFrameworkFontEngineClient : ClientModInitializer {
    override fun onInitializeClient() {
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    // noto-sans-abraic.ttf
    val pth = "/usr/share/fonts/StarRailFont.ttf"
    val ftfont = FreeTypeFont(pth)

    val blob = hb_blob_create_from_file_or_fail(pth)
    val blength = hb_blob_get_length(blob)
    println("Font length: $blength")

    val face = hb_face_create(blob, 0)
    hb_blob_destroy(blob)

    val upem = hb_face_get_upem(face)
    val font = hb_font_create(face)
    hb_font_set_scale(font, upem, upem)

    val buffer = hb_buffer_create()
    // "إنه اختبار"
    hb_buffer_add_utf8(buffer, "Test! 测试！", 0, -1)
    hb_buffer_guess_segment_properties(buffer)

    hb_shape(font, buffer, null)
    val count = hb_buffer_get_length(buffer)
    val infos = hb_buffer_get_glyph_infos(buffer)

    for (i in 0 ..< count) {
        val info = infos?.get(i)
        println("cluster ${info?.cluster()}\t\t\tglyph ${info?.codepoint()}->${ftfont.toGlyphIndex(info?.codepoint()?.toLong()?: 0L, true)}")
    }
    hb_buffer_destroy(buffer)
    hb_font_destroy(font)
    hb_face_destroy(face)
}