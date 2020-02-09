package uz.ideal.dictionary.libs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataBaseHelper extends SQLiteOpenHelper {
    private static String TAG = "TTT";

    private static String DB_PATH = "";
    private static String DB_NAME;
    private final Context mContext;
    protected SQLiteDatabase mDataBase;

    @SuppressLint("SdCardPath")
    public DataBaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);//
        DB_NAME = databaseName;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;

        try {
            createDataBase();
            openDataBase();
            close();
            mDataBase = getReadableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDataBase() throws IOException {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.v(TAG, dbFile + "   " + dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        try {
            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close


                    ();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Open the database, so we can query it
    private void openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        Log.i(TAG, "openDataBase: ");
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
