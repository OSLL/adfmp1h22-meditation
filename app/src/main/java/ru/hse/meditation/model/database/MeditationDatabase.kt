package ru.hse.meditation.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.hse.meditation.model.converter.DateConverter
import ru.hse.meditation.model.dao.CourseDao
import ru.hse.meditation.model.dao.PracticeDao
import ru.hse.meditation.model.dao.PracticeRecordDao
import ru.hse.meditation.model.dao.TheoryDao
import ru.hse.meditation.model.entity.Course
import ru.hse.meditation.model.entity.Practice
import ru.hse.meditation.model.entity.PracticeRecord
import ru.hse.meditation.model.entity.Theory

@Database(
    entities = [Course::class, Theory::class, Practice::class, PracticeRecord::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class MeditationDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun theoryDao(): TheoryDao
    abstract fun practiceDao(): PracticeDao
    abstract fun practiceRecordDao(): PracticeRecordDao

    companion object {
        @Volatile private var instance: MeditationDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MeditationDatabase::class.java, "meditation_dev.db")
                .createFromAsset("test.db")
                .build()
    }
}
