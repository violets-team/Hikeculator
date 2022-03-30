package com.example.hikeculator.data.data_store.current_selected_product

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.hikeculator.ProductPreferences


private const val DATA_STORE_FILE_NAME_PRODUCT = "product"

val Context.productPreferenceStore: DataStore<ProductPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME_PRODUCT,
    serializer = ProductPreferenceSerializer
)
