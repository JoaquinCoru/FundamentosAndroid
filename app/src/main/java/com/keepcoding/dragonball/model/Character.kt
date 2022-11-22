package com.keepcoding.dragonball.model

data class Character(
    val id: String,
    val name: String,
    val description: String,
    val photo: String,
    val favorite: Boolean,
    var maxLife: Int = 100,
    var currentLife: Int = 100
) {


    init {
        maxLife = 100
        currentLife = 100
    }
}


