package com.madhav.mc_project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JitsiDB::class], version = 1)
abstract class JitsiDBClass : RoomDatabase() {
    abstract fun JitsiDAO() : JitsiDAObject

    object DatabaseBuilder {
        @Volatile
        private var INSTANCE: JitsiDBClass? = null

        fun getInstance(context: Context): JitsiDBClass {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JitsiDBClass::class.java,
                    "Jitsi_users"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}