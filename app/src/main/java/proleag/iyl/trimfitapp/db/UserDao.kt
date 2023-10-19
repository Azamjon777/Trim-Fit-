package proleag.iyl.trimfitapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import proleag.iyl.trimfitapp.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}
