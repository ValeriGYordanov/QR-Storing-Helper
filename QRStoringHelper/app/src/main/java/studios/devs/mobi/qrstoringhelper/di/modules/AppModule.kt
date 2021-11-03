package studios.devs.mobi.qrstoringhelper.di.modules


import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import studios.devs.mobi.qrstoringhelper.MainApplication
import javax.inject.Singleton

@Module
class AppModule(val context: Context, val application: Application) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

    @Provides
    fun providesWifiManager(context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager {
        return context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun providesSharedPreferences(context: Context): SharedPreferences{
        return context.getSharedPreferences(MainApplication.QR_STORING_HELPER_LOCAL_STORE, AppCompatActivity.MODE_PRIVATE)
    }

    @Provides
    fun providesContentResolver(context: Context): ContentResolver{
        return context.contentResolver
    }
}