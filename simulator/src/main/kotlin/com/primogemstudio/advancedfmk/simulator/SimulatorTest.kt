package com.primogemstudio.advancedfmk.simulator

import com.primogemstudio.advancedfmk.simulator.roundtrip.CharacterBase
import com.primogemstudio.advancedfmk.simulator.roundtrip.DefaultedObject
import com.primogemstudio.advancedfmk.simulator.roundtrip.SimulatedUniverse
import kotlin.random.Random

fun main() {
    val simu = SimulatedUniverse(funcRequestTarget = {
        var i = -1
        while (i == -1 || !it.targetObjects[i].alive()) {
            i = Random.nextInt(0, it.targetObjects.size)
        }
        println("${it.`object`} attack ${it.targetObjects[i]} (player)")
        intArrayOf(i)
    }, funcUncontrolledRequestTarget = {
        var i = -1
        while (i == -1 || !it.targetObjects[i].alive()) {
            i = Random.nextInt(0, it.targetObjects.size)
        }
        println("${it.`object`} attack ${it.targetObjects[i]} (enemy)")
        intArrayOf(i)
    })

    simu.characters.add(DefaultedObject(
        100f, 25f, CharacterBase.Type.Controllable
    ))
    simu.characters.add(DefaultedObject(
        200f, 15f, CharacterBase.Type.Controllable
    ))
    simu.characters.add(DefaultedObject(
        50f, 50f, CharacterBase.Type.Controllable
    ))

    simu.enemies.add(DefaultedObject(
        500f, 20f, CharacterBase.Type.UnControllable
    ))
    simu.enemies.add(DefaultedObject(
        750f, 5f, CharacterBase.Type.UnControllable
    ))

    simu.loopMain()

    println(simu)
}