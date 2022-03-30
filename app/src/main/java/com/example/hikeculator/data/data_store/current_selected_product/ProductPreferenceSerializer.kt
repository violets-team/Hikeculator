package com.example.hikeculator.data.data_store.current_selected_product

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.hikeculator.ProductPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object ProductPreferenceSerializer : Serializer<ProductPreferences> {

    override val defaultValue: ProductPreferences = ProductPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProductPreferences {
        try {
            return ProductPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: ProductPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}