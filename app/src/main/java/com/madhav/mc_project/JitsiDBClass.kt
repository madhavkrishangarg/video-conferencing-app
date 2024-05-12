package com.madhav.mc_project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JitsiDB::class], version = 1)
abstract class JitsiDBClass : RoomDatabase() {
    abstract fun JitsiDAO() : JitsiDAObject
}