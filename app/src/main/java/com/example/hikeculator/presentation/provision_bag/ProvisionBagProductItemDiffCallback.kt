package com.example.hikeculator.presentation.provision_bag

import androidx.recyclerview.widget.DiffUtil
import com.example.hikeculator.domain.entities.ProvisionBagProduct

class ProvisionBagProductItemDiffCallback : DiffUtil.ItemCallback<ProvisionBagProduct>() {

    override fun areItemsTheSame(
        oldItem: ProvisionBagProduct,
        newItem: ProvisionBagProduct
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProvisionBagProduct,
        newItem: ProvisionBagProduct
    ): Boolean {
        return oldItem == newItem
    }
}