package com.example.splitthebill.domain.model.interfaces

import com.example.splitthebill.domain.model.Item

interface ItemsObserver {
    fun onItemsChanged(members: List<Item>)
}
