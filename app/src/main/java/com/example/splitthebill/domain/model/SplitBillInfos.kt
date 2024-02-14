package com.example.splitthebill.domain.model

import java.io.Serializable

class SplitBillInfos : Serializable {
    var index: Int? = null
    var name: String = ""
    var hasToPay: Boolean = false
    var value: Double = 0.0

    constructor(index: Int?, name: String, hasToPay: Boolean, value: Double) {
        this.index = index
        this.name = name
        this.value = value
        this.hasToPay = hasToPay
    }
}