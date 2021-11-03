package studios.devs.mobi.qrstoringhelper.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import studios.devs.mobi.qrstoringhelper.MainApplication
import studios.devs.mobi.qrstoringhelper.di.modules.*
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    ActivityModule::class,
    ViewModelModule::class,
    RepositoryModule::class])
interface AppComponent: AndroidInjector<MainApplication> {}