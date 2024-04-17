package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Whenever you change the schema of the database table, you have to increase the version number
// Set exportSchema to false so as not to keep schema version history backups
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase: RoomDatabase() {
// declare an abstract function that returns the ItemDao so that the database knows about the DAO.
    abstract fun itemDao(): ItemDao
// companion object allows access to the methods to create or get the database and uses the class name as the qualifier
    companion object {
/**
 * Instance variable keeps a reference to the database, when one has been created help maintaining a single
 * instance of the database opened at a given time, which is an expensive resource to create and maintain.
 */
/**
 * value of a volatile variable is never cached, and all reads and writes are to and from the main memory.
 * These features help ensure the value of Instance is always up to date and is the same for all execution
 * threads. It means that changes made by one thread to Instance are immediately visible to all other threads.
 */

    @Volatile
    private var Instance: InventoryDatabase? = null

    // Context is a parameter that the database builder needs
    fun getDatabase(context: Context): InventoryDatabase {
/**
 * Wrapping the code to get the database inside a synchronized block means that only one thread of execution
 * at a time can enter this block of code, which makes sure the database only gets initialized once
 * (instead of risking multiple threads potentially asking for a database instance at the same time
 * resulting in two databases (known as a race condition))
 */
        // if the Instance is not null, return it, otherwise create a new database instance.
                                            //this = companion object
        return Instance ?: synchronized(this) {
            Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database").fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            // "it" keeps a reference to the recently created db instance
        }
    }

    }
}

/**
 * Note: Normally, you would provide a migration object with a migration strategy for when the schema changes.
 * A migration object is an object that defines how you take all rows with the old schema and convert them to
 * rows in the new schema, so that no data is lost. Migration is beyond the scope of this codelab, but the term
 * refers to when the schema is changed and you need to move your date without losing the data. Since this is a
 * sample app, a simple alternative is to destroy and rebuild the database, which means that the inventory data
 * is lost. For example, if you change something in the entity class, like adding a new parameter, you can allow
 * the app to delete and re-initialize the database.
 */