package uz.ideal.dictionary.database

import android.annotation.SuppressLint
import android.content.Context
import uz.ideal.dictionary.libs.DataBaseHelper
import uz.ideal.dictionary.models.WordData

class Database private constructor(context: Context) : DataBaseHelper(context, "words.db") {

    companion object {
        var myBase: Database? = null

        fun init(context: Context) {
            if (myBase == null)
                myBase = Database(context)
        }

        fun getBase(): Database = myBase!!
    }

    @SuppressLint("Recycle")
    fun getAllWords(): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor = mDataBase.rawQuery("select * from data", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            data.add(
                WordData(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6)
                )
            )
            cursor.moveToNext()
        }
        cursor.close()
        return data
    }

}