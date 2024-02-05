package com.example.splitthebill.domain.model

class Item(description: String, value: Double) {
    var description: String
    var value: Double = 0.0

    init {
        this.description = description
        this.value = value
    }
}