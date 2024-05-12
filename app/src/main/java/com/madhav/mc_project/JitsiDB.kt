package com.madhav.mc_project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Jitsi_users")
data class JitsiDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val email: String,
    val meeting_id: String
)