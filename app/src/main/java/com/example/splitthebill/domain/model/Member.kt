package com.example.splitthebill.domain.model

import java.io.Serializable

class Member(name: String, value: Double, list: MutableList<Item>) : Serializable {
    var name: String
    var amountPaid: Double = 0.0
    var items: MutableList<Item>

    init {
        this.name = name
        this.amountPaid = value
        this.items = list
    }
}