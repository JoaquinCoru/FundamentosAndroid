package com.keepcoding.dragonball.model

data class DbCharacterDto(
    val id:String,
    val name:String,
    val description:String,
    val photo:String,
    val favorite:Boolean,
)
