package proleag.iyl.trimfitapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import proleag.iyl.trimfitapp.models.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
