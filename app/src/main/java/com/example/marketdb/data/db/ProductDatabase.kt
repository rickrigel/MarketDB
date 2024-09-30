package com.example.marketdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.marketdb.data.db.model.ProductEntity
import com.example.marketdb.util.Constants

@Database(
    entities = [ProductEntity::class],
    version = 1, //versión de la bd para migraciones
    exportSchema = true //por defecto es true.
)

abstract class ProductDatabase: RoomDatabase() {
    //Aquí va el DAO
    abstract fun productDao(): ProductDao

    //Sin inyección de dependencias, instanciamos la base de datos
    //aquí con un patrón singleton

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase {
            //Si la instancia no es nula, entonces vamos
            //a regresar la que ya tenemos
            //Si es nula, creamos una instancia y la regresamos
            //(patrón singleton)

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }

        }

    }
}