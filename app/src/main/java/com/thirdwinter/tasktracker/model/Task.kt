package com.thirdwinter.tasktracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kcoding.recyclerview_helper.SuperEntity
import java.util.*

@Entity
data class Task(
    val title: String,
    var completed: Boolean=false,
    val timeStamp: Long = System.currentTimeMillis(),
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
):SuperEntity()
