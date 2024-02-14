package com.example.splitthebill.domain.model

import java.io.Serializable

class Item : Serializable {
    var index: Int? = null
    var description: String = ""
    var value: Double = 0.0

    constructor()

    constructor(index: Int?, description: String, value: Double) {
        this.index = index
        this.description = description
        this.value = value
    }
}