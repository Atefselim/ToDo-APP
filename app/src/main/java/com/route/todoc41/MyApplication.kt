package com.route.todoc41

import android.app.Application
import com.route.todoc41.database.MyDatabase

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.init(this)
    }
}