package com.expert.healthinfo.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.expert.healthinfo.core.data.source.local.entity.HeadlinesEntity

@Database(entities = [HeadlinesEntity::class], version = 1, exportSchema = false)
abstract class HeadlinesDatabase : RoomDatabase() {
    abstract fun headlinesDao():HeadlinesDao
}