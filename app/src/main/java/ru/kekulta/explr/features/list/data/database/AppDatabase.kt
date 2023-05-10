package ru.kekulta.explr.features.list.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kekulta.explr.features.difflist.data.database.dao.HashedDao
import ru.kekulta.explr.features.difflist.data.database.dto.HashedDto
import ru.kekulta.explr.features.list.data.database.dao.FileDao
import ru.kekulta.explr.features.list.data.database.dto.FileDto

@Database(entities = [FileDto::class, HashedDto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFileDao(): FileDao
    abstract fun getHashedDao(): HashedDao


    companion object {
        private var instance: AppDatabase? = null

        fun initDatabase(context: Context) {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "files.db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        fun getDatabase(): AppDatabase {
            return requireNotNull(instance) {
                "Database wasn't initialized!"
            }
        }
    }

}
