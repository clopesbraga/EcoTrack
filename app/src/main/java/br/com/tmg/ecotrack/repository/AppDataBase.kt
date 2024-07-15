package br.com.tmg.ecotrack.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.tmg.ecotrack.DAO.ImageItemDAO
import br.com.tmg.ecotrack.model.database.ImageItemModel

@Database(entities = arrayOf(ImageItemModel::class), version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun imageItemDao(): ImageItemDAO

    companion object {

        private lateinit var DBINSTANCE: AppDataBase
        fun getDatabase(context: Context): AppDataBase {
            if (!Companion::DBINSTANCE.isInitialized) {
                synchronized(AppDataBase::class) {
                    DBINSTANCE =
                        Room.databaseBuilder(context, AppDataBase::class.java, "AppDataBase")
                            .addMigrations(MIGRATION)
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return DBINSTANCE
        }
    }

}

private val MIGRATION: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM Image")
    }

}