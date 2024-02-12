package com.example.splitthebill.domain.model

import java.io.Serializable

class Member : Serializable {
    var index: Int? = null
    var name: String = ""
    var amountPaid: Double = 0.0
    var items: MutableList<Item> = mutableListOf()
    constructor()

    constructor(index: Int?, name: String, value: Double, list: MutableList<Item>) {
        this.index = index
        this.name = name
        this.amountPaid = value
        this.items = list
    }
}