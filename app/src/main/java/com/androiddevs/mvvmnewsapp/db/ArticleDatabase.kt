package com.androiddevs.mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.models.Article

/*
* Database classes for room need to be abstract
* */

/*
* From chat GPT :)

* LOCK in this context is a private variable that is used as a synchronization lock to ensure that
* only one thread can execute the code inside the synchronized block at a time.

* synchronized is a keyword in Java and Kotlin that is used to ensure that a block of code is
* executed by only one thread at a time. When a thread enters a synchronized block, it acquires a
* lock on the specified object. Once the thread exits the block, it releases the lock. If another
* thread tries to enter the same synchronized block while a lock is held, it will be blocked until
* the lock is released.

* In this case, LOCK is an instance of Any, which is a base class for all non-nullable types in
* Kotlin. It serves as a placeholder and doesn't have any specific functionality. It is used as a
* lock object because it is guaranteed to be unique and cannot be null.

* By using synchronized block with LOCK, it ensures that only one thread at a time can access the
* instance variable, and that the createDatabase function is only called once, even if multiple
* threads are trying to access the ArticleDatabase instance at the same time.
* */

@Database(
    entities = [Article::class], // we only have one table in our database
    version = 2 // version is used to update db, so if we make changes, we need to update version
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao // Room implements this function behind the scenes

    // create a single instance of a database that is used to access DAO
    companion object {

        @Volatile // other threads can see when a thread changes this instance
        private var instance: ArticleDatabase? = null

        private val LOCK = Any()

        // returns instance of Database or if it is null createDatabase() and sets instance to it,
        // using synchronized(LOCK) to ensure that only one instance of the database is created at
        // a time, even if multiple threads try to access it.
        operator fun invoke(context: Context): ArticleDatabase? {
            instance ?: synchronized(LOCK) { // first null check
                instance ?: createDatabase(context).also { instance = it } // second null check
            }
            return instance
        }

        private fun createDatabase(context: Context) = // returns ArticleDatabase
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}

