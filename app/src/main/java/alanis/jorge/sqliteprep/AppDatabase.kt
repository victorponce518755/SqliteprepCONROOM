package alanis.jorge.sqliteprep

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    //Tenemos que generar un User y un UserDao
    //El User es una data class con los atributos que queremos que tenga nuestra tabla
    //El UserDao es una interface con las funciones que queremos que tenga nuestra tabla
    //Primero el UserDao como interface


}