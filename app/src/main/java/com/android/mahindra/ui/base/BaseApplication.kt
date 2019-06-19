package com.android.mahindra.ui.base

/**
 * @author Kaiser Perwez
 */
 
 
import android.app.Application

/*import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric*/

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //  Fabric.with(this, Crashlytics()) //init fabric

        /*  //init media picker
          Album.initialize(AlbumConfig.newBuilder(this)
                  .setAlbumLoader(MyMediaLoader())
                  .build())*/
    }
}
