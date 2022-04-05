package com.example.hikeculator.data.repository_implementations

import android.content.Context
import com.example.hikeculator.NutritionalValuePreferences
import com.example.hikeculator.data.common.mapToProduct
import com.example.hikeculator.data.data_store.current_selected_product.productPreferenceStore
import com.example.hikeculator.domain.entities.Product
import com.example.hikeculator.domain.repositories.SelectedProductRepository
import kotlinx.coroutines.flow.first


class SelectedProductRepositoryImpl(private val appContext: Context) : SelectedProductRepository {

    override suspend fun saveProduct(product: Product) {
        appContext.productPreferenceStore.updateData { currentPreferences ->
            currentPreferences.toBuilder().apply {
                id = product.id
                name = product.name
                nutritionalValue = NutritionalValuePreferences.newBuilder().apply {
                    calories = product.nutritionalValue.calories
                    proteins = product.nutritionalValue.proteins
                    fats = product.nutritionalValue.fats
                    carbs = product.nutritionalValue.carbs
                }.build()
            }.build()
        }
    }

    override suspend fun fetchProduct(): Product {
        return appContext.productPreferenceStore.data.first().mapToProduct()
    }
}
