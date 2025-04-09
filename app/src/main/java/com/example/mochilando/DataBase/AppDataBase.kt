package com.example.mochilando.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mochilando.Dao.TripDAO
import com.example.mochilando.Dao.UserDAO
import com.example.mochilando.Entity.Trip
import com.example.mochilando.Entity.User

@Database(
    entities = [User::class, Trip::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDAO
    abstract fun tripDao() : TripDAO

    companion object {
        @Volatile

        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "trip_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}