package uz.ideal.dictionary.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import uz.ideal.dictionary.libs.DataBaseHelper
import uz.ideal.dictionary.models.WordData

class Database private constructor(context: Context) : DataBaseHelper(context, "words.db") {

    companion object {
        private var myBase: Database? = null

        fun init(context: Context) {
            if (myBase == null)
                myBase = Database(context)
        }

        fun getBase(): Database = myBase!!
    }

    @SuppressLint("Recycle")
    fun getAllWords(): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor =
            mDataBase.rawQuery("select * from data where isDel=? order by word", arrayOf("0"))
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

    fun getFavouriteWords(): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor =
            mDataBase.rawQuery(
                "select * from data where isFav=? and isDel=? order by word",
                arrayOf("1", "0")
            )
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

    fun getSeenWords(): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor =
            mDataBase.rawQuery(
                "select * from data where isSeen=? and isDel=? order by word",
                arrayOf("1", "0")
            )
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

    fun getDeletedWords(): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor =
            mDataBase.rawQuery("select * from data where isDel=? order by word", arrayOf("1"))
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


    fun newWord(word: String, translation: String, description: String): Long {
        val values = ContentValues()
        values.put("word", word)
        values.put("translation", translation)
        values.put("description", description)
        values.put("isFav", 0)
        values.put("isSeen", 0)
        values.put("isDel", 0)
        return mDataBase.insert("data", null, values)
    }

    fun moveToDeletingBox(id: Int) {
        val v = ContentValues()
        v.put("isDel", 1)
        mDataBase.update("data", v, "id=?", arrayOf(id.toString()))
    }

    fun delete(id: Int) {
        mDataBase.delete("data", "id=?", arrayOf(id.toString()))
    }

    fun restore(id: Int) {
        val v = ContentValues()
        v.put("isDel", 0)
        mDataBase.update("data", v, "id=?", arrayOf(id.toString()))
    }

    fun addToFavourites(id: Int) {
        val v = ContentValues()
        v.put("isFav", 1)
        mDataBase.update("data", v, "id=?", arrayOf(id.toString()))
    }

    fun removeFromFavourites(id: Int) {
        val v = ContentValues()
        v.put("isFav", 0)
        mDataBase.update("data", v, "id=?", arrayOf(id.toString()))
    }

    fun addToSeen(id: Int) {
        val v = ContentValues()
        v.put("isSeen", 1)
        mDataBase.update("data", v, "id=?", arrayOf(id.toString()))
    }

    fun removeFromSeen(id: Int) {
        val v = ContentValues()
        v.put("isSeen", 0)
        mDataBase.update("data", v, "id=?", arrayOf(id.toString()))
    }

    fun searchFromAllWords(word: String): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor = mDataBase.rawQuery(
            "select * from data where isDel=? and word like ? order by word",
            arrayOf("0", "$word%")
        )
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

    fun searchFromFavourites(word: String): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor = mDataBase.rawQuery(
            "select * from data where isFav=? and isDel=? and word like ? order by word",
            arrayOf("1", "0", "$word%")
        )
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

    fun searchFromSeen(word: String): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor = mDataBase.rawQuery(
            "select * from data where isSeen=? and isDel=? and word like ? order by word",
            arrayOf("1", "0", "$word%")
        )
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

    fun searchFromDeleting(word: String): ArrayList<WordData> {
        val data = arrayListOf<WordData>()
        val cursor = mDataBase.rawQuery(
            "select * from data where isDel=? and word like ? order by word",
            arrayOf("1", "$word%")
        )
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