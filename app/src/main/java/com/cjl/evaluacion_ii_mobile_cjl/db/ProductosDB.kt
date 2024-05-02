package com.cjl.evaluacion_ii_mobile_cjl.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities=[Producto::class], version=1)
abstract class ProductosDB:RoomDatabase() {

    abstract fun productDao():ProductoDao

    //singleton

    companion object{
        // esto asegura que sea actualizada la propiedad de forma atomica
        @Volatile
        private var BASE_DATOS:ProductosDB?=null

        fun getInstance(contexto: Context):ProductoDao{
            databaseBuilder(
                contexto.applicationContext,
                ProductosDB::class.java,
                "Productos.bd"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { BASE_DATOS= it }
            return TODO("Provide the return value")
        }

    }


}