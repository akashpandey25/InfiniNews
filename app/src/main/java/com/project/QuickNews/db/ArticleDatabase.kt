package com.project.QuickNews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.QuickNews.model.Article

@Database(entities = [Article::class], version = 2)
@TypeConverters(Converter::class)
abstract class ArticleDatabase:RoomDatabase() {
    abstract fun getArticleDao(): ArticleDAO
    companion object{
        @Volatile //change made in one thread will let to other immediately
        private  var instance: ArticleDatabase?=null //hold the singleton article of the db
        private val LOCK=Any() //it is used to synchronize the db creation
        //it follows singleton pattern mean only one thread at a time will be executed
        operator  fun  invoke(context: Context)= instance?: synchronized(LOCK){ //it follows singleton patten only one instance is created
            instance?: createDataBase(context).also {
                instance=it
            }
        }

        private fun createDataBase(context: Context)=
                Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_db.db"
                )
                    .addMigrations(MIGRATION)
                    .build()

        val MIGRATION=object :Migration(1, 2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE articles ADD COLUMN category TEXT")
            }
        }

    }

}