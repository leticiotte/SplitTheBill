package com.example.splitthebill.domain.model

class Item(index: Int?, description: String, value: Double) {
    var index: Int?
    var description: String
    var value: Double = 0.0

    init {
        this.index = index
        this.description = description
        this.value = value
    }
}