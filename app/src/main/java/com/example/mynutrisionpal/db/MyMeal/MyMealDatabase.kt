package com.example.mynutrisionpal.db.MyMeal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MyMeal::class), version=1, exportSchema = false)
abstract class MyMealDatabase: RoomDatabase() {
    abstract fun myMealDao(): MyMealDao

    companion object {
        @Volatile
        private  var instance: MyMealDatabase?=null

        fun getDatabase(context: Context) = instance ?: synchronized(this){
            Room.databaseBuilder(context.applicationContext, MyMealDatabase:: class.java, "mymeal_db").allowMainThreadQueries().build()
        }
    }
}