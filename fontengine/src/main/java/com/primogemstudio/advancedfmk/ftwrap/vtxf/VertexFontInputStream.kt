package com.primogemstudio.advancedfmk.ftwrap.vtxf

import com.primogemstudio.advancedfmk.client.LOGGER
import com.primogemstudio.advancedfmk.ftwrap.FreeTypeGlyph
import org.joml.Vector2f
import java.io.DataInputStream
import java.io.InputStream

class VertexFontInputStream(`in`: InputStream) : DataInputStream(`in`) {
    fun parse(): List<Pair<Char, FreeTypeGlyph>> {
        if (readUTF() != "VTXF") throw IllegalStateException("Font header wrong")
        if (readShort() != 0x0307.toShort()) throw IllegalStateException("Magic number wrong")
        val version = readByte()
        val glyph = readInt()
        println("Glyphs: $glyph (ver $version)")

        val map = mutableListOf<Pair<Char, FreeTypeGlyph>>()
        var le = 0
        for (i in 0..<glyph) {
            val code = readInt()
            val whscale = readFloat()

            val vertices = mutableListOf<Vector2f>()

            for (j in 0..<readInt()) vertices.add(Vector2f(readFloat(), readFloat()))

            val indices = mutableListOf<Int>()
            for (k in 0..<readInt().apply { if (this % 3 != 0) throw IllegalStateException("Wrong indices size") }) {
                indices.add(readInt())
            }

            map.add(Pair(code.toChar(), FreeTypeGlyph(whscale, vertices, indices)))
            le++
            if (le % 500 == 0) LOGGER.info("${code.toChar()} 0x${Integer.toHexString(code)} ${le.toFloat() / glyph.toFloat() * 100} % complete")
        }
        if (readLong() / glyph.toLong() != 0x20230426L) throw IllegalStateException("Wrong file end")

        return map
    }
}