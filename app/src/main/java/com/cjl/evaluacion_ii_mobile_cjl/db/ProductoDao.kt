package com.cjl.evaluacion_ii_mobile_cjl.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductoDao {

    // Para obtener todos los productos
    @Query("SELECT * FROM producto")
    fun getProductos():List<Producto>

    // Para obtener la cantidad total de productos
    @Query("SELECT COUNT(*) FROM producto")
    fun getCantProductos():Int

    // Para obtener todos los productos comprados
    @Query("SELECT * FROM producto WHERE comprado=1")
    fun getProductosComprados():List<Producto>

    // Para obtener todos los productos faltantes de compra
    @Query("SELECT * FROM producto WHERE comprado=0")
    fun getProductosFaltantes():List<Producto>

    // Para insertar Productos
    @Insert
    fun insertProducto(producto: Producto):Long

    // Para actualizaar productos
    @Update
    fun updateProducto(producto: Producto)

    // Para eliminar Producto
    @Delete
    fun deleteProducto(producto: Producto)
}