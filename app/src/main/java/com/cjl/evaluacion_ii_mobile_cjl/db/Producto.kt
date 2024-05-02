package com.cjl.evaluacion_ii_mobile_cjl.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Producto(
    @PrimaryKey val idProducto:Int,
    var producto: String, // nombre producto
    var comprado: Boolean // indica si el producto esta comprado o no

)
