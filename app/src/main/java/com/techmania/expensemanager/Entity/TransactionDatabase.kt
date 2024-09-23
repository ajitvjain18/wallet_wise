package com.techmania.expensemanager.Entity

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

@Database(entities = [TransactionEntity::class], version = 1)
abstract class TransactionDatabase : RoomDatabase(){

    abstract fun getTransactionDao() : TransactionDAO


    companion object {
        @Volatile
        private var INSTANCE : TransactionDatabase? = null
        private val DATABASE_NAME = "transaction_database.db"

        fun getdatabase(context:Context) : TransactionDatabase{
            return  INSTANCE?: synchronized(this){
            val instance = Room.databaseBuilder(context.applicationContext,TransactionDatabase::class.java,DATABASE_NAME).build()
            INSTANCE = instance
            instance
        }
        }
    }

    fun backupDatabase(context: Context) {
        val currentDBPath = context.getDatabasePath(DATABASE_NAME).absolutePath
        val backupDBPath =
            context.getExternalFilesDir(null)?.absolutePath + File.separator + "backup_$DATABASE_NAME"

        try {
            val src = FileInputStream(currentDBPath).channel
            val dst = FileOutputStream(backupDBPath).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
            Log.d("ajit", "backupDatabase: success")
        } catch (e: IOException) {
            Log.d("ajit", "backupDatabase: fail")
            e.printStackTrace()
        }
    }

    fun restoreDatabase(context: Context) {
        val currentDBPath = context.getDatabasePath(DATABASE_NAME).absolutePath
        val backupDBPath =
            context.getExternalFilesDir(null)?.absolutePath + File.separator + "backup_$DATABASE_NAME"

        try {
            val src = FileInputStream(backupDBPath).channel
            val dst = FileOutputStream(currentDBPath).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
            Log.d("ajit", "restoreDatabase: success")
        } catch (e: IOException) {
            Log.d("ajit", "restoreDatabase: fail")
            e.printStackTrace()
            // Restore failed
        }
    }
}