package com.example.hikeculator.presentation.product_search

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes


class AutoCompleteAdapter(context: Context, @LayoutRes layoutId: Int) :
    ArrayAdapter<String>(context, layoutId) {

    private val suggestions: MutableList<String> = mutableListOf()

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(text: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = suggestions
                count = suggestions.size
            }
        }

        override fun publishResults(text: CharSequence?, results: FilterResults?) {
            clear()
            addAll(suggestions)
        }
    }

    fun setSuggestions(suggestions: List<String>) {
        this.suggestions.apply {
            clear()
            addAll(suggestions)
        }
    }

    fun getSuggestion(position: Int): String = suggestions[position]
}