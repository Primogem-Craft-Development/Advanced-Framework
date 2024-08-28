package com.primogemstudio.advancedfmk.kui.test.snakedual

import org.joml.Vector2i
import kotlin.random.Random

class Main {
    var worm: Move
    var food: Vector2i

    init {
        worm = Move()
        food = createFood()
    }

    private fun createFood(): Vector2i {
        var x: Int
        var y: Int
        do {
            x = Random.nextInt(COLS)
            y = Random.nextInt(ROWS)
        }
        while (worm.contains(x, y))
        return Vector2i(x, y)
    }

    fun step() {
        if (worm.hit()) {
            worm = Move()
            food = createFood()
        }
        else {
            if (worm.creep(food)) food = createFood()
        }
    }

    companion object {
        const val ROWS: Int = 16
        const val COLS: Int = 16
    }
}