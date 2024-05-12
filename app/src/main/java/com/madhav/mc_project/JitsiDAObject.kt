package com.madhav.mc_project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JitsiDAObject {
    @Insert
    fun upsert(userData: JitsiDB)

    @Query("SELECT * FROM Jitsi_users WHERE email = :user_email")
    fun getUserData(user_email: String): JitsiDB?
}