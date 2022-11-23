package com.keepcoding.dragonball.model

data class DbCharacter(
    val id: String,
    val name: String,
    val description: String,
    val photo: String,
    val favorite: Boolean,
    var maxLife: Int = 100,
    var currentLife: Int = 100,
    var isSelected:Boolean = false
) {

    init {
        maxLife = 100
        currentLife = 100
    }
}


