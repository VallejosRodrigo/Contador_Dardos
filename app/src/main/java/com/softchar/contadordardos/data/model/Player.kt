package com.softchar.contadordardos.data.model

data class Player(
    var name: String = "Player",
    var sum: Int = 0,
    var target: Int = 0,
    var numbersAdded: MutableList<Int> = mutableListOf(),
    var initialTarget: Int = 0,
    var currentScore: Int = 0
)
