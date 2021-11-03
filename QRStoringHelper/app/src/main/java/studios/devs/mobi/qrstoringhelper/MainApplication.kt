package studios.devs.mobi.qrstoringhelper

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import studios.devs.mobi.qrstoringhelper.di.AppComponent
import studios.devs.mobi.qrstoringhelper.di.DaggerAppComponent
import studios.devs.mobi.qrstoringhelper.di.modules.AppModule
import javax.inject.Inject

class MainApplication: Application(), HasAndroidInjector {

    companion object{
        lateinit var appComponent: AppComponent
        const val QR_STORING_HELPER_LOCAL_STORE = "QRStoringHelperLocalStore"
    }

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this, this)).build()
        appComponent.inject(this)
    }

}