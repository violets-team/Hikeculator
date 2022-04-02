package com.example.hikeculator.presentation.provision_bag

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hikeculator.R
import com.example.hikeculator.databinding.ItemProvisionBagProductBinding
import com.example.hikeculator.domain.entities.ProvisionBagProduct

class ProvisionBagAdapter(
    private val onItemClick: (updatedProduct: ProvisionBagProduct) -> Unit
) : ListAdapter<ProvisionBagProduct, ProvisionBagAdapter.ProductItemHolder>(
    ProvisionBagProductItemDiffCallback()
) {

    private var rootRecyclerView: RecyclerView? = null
    private var wasNotAnimated = true

    private companion object {
        const val MAX_ALPHA = 1F
        const val MIN_ALPHA = 0f
        const val TRANSACTION_START_SHIFT_VALUE = 400f
        const val TRANSLATION_DESTINATION = 0f
        const val DURATION = 500L
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rootRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductItemHolder(
            binding = ItemProvisionBagProductBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        holder.bind(product = getItem(holder.absoluteAdapterPosition))

    }

    override fun submitList(list: List<ProvisionBagProduct>?) {
        super.submitList(list)

        if (wasNotAnimated) {
            animateRecyclerView()
            wasNotAnimated = false
        }
    }

    private fun animateRecyclerView() {
        rootRecyclerView?.apply {
            val layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.recycler_view_provision_bag_layout_animation
            )

            layoutAnimation = layoutAnimationController
        }
    }

    inner class ProductItemHolder(
        val binding: ItemProvisionBagProductBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProvisionBagProduct) {
            with(binding) {
                checkBoxProvisionBagProduct.isChecked = product.isBought
                textViewProductName.text = product.name
                textViewProductWeight.text = root.context.getString(
                    R.string.text_provision_bag_product_weight,
                    product.weight
                )

                root.apply {
                    val updatedProduct = product.copy(isBought = !product.isBought)

                    setOnClickListener {
                        onItemClick(updatedProduct)
                        animateItem()
                    }
                }
            }
        }

        private fun animateItem() {
            binding.root.let { view ->
                view.alpha = MIN_ALPHA
                val randomDirection = if ((0..1).random() % 2 == 0) 1 else -1
                view.translationX = TRANSACTION_START_SHIFT_VALUE * randomDirection

                view.animate().apply {
                    duration = DURATION
                    interpolator = AnticipateOvershootInterpolator()
                    alpha(MAX_ALPHA)
                    translationX(TRANSLATION_DESTINATION)
                }.start()
            }
        }
    }
}