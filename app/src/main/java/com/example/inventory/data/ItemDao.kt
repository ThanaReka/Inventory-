package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    /*
     * There is no convenience annotation for the remaining functionality,
     * so you have to use the @Query annotation and supply SQLite queries
     */

    //Use the SQLite query as a string parameter to the @Query annotation.
    @Query("SELECT * from items WHERE id = :id" )
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    /*
     * With Flow as the return type, you receive notification whenever the data in the database changes.
     * The Room keeps this Flow updated for you, which means you only need to explicitly get the data once.
     * This setup is helpful to update the inventory list.
     * Because of the Flow return type, Room also runs the query on the background thread.
     * You don't need to explicitly make it a suspend function and call it inside a coroutine scope.
     */
}

/*
 * Mark the functions with the suspend keyword to let it run on a separate thread.
 * The database operations can take a long time to execute, so they need to run on a separate thread.
 * Room doesn't allow database access on the main thread.
 */