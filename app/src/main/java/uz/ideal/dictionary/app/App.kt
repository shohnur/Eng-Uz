package uz.ideal.dictionary.app

import android.app.Application
import uz.ideal.dictionary.database.Database

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }

    override fun onTerminate() {
        Database.getBase().close()
        super.onTerminate()
    }

}