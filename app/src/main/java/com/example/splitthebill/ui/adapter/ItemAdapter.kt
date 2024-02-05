package com.example.splitthebill.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.interfaces.ItemsObserver
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener
import com.example.splitthebill.ui.holder.ItemHolder

class ItemAdapter(private val context: Context, private var items: List<Item>, private val listener: OnItemClickListener): RecyclerView.Adapter<ItemHolder>() {

    private val observers = mutableListOf<ItemsObserver>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false), listener)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.description.text = items[position].description
        holder.value.text = context.getString(R.string.formatted_value, items[position].value)
        holder.image.setImageResource(R.drawable.basket)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun registerObserver(observer: ItemsObserver) {
        observers.add(observer)
    }

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onItemsChanged(items)
        }
    }

    fun setItems(newItems: List<Item>) {
        if (items != newItems) {
            items = newItems
            notifyDataSetChanged()
            notifyObservers()
        }
    }
}
