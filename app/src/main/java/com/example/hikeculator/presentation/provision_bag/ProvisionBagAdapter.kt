package com.example.hikeculator.presentation.provision_bag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.databinding.ItemBagProductBinding
import com.example.hikeculator.domain.common.update
import com.example.hikeculator.domain.entities.Product

class ProvisionBagAdapter : RecyclerView.Adapter<ProvisionBagAdapter.ProductItemHolder>() {

    private val products = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductItemHolder(
            binding = ItemBagProductBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        holder.bind(product = products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateData(data: List<Product>) {
        products.update(newData = data)
        notifyDataSetChanged()
    }

    class ProductItemHolder(
        val binding: ItemBagProductBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.checkBoxProduct.text = product.name
            binding.textViewWeight.setText(product.weight.toString())
        }
    }
}