package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.database.dao.CharacterDao
import com.example.data.database.dao.MatchHistoryDao
import com.example.data.database.dao.UserProfileDao
import com.example.data.database.entities.CharacterEntity
import com.example.data.database.entities.MatchHistoryEntity
import com.example.data.database.entities.UserProfileEntity

@Database(
    entities = [
        CharacterEntity::class,
        UserProfileEntity::class,
        MatchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun matchHistoryDao(): MatchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anime_imposter_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
