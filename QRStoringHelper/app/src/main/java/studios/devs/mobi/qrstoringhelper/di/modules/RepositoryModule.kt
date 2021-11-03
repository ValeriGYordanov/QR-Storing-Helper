package studios.devs.mobi.qrstoringhelper.di.modules

import android.content.ContentResolver
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import studios.devs.mobi.qrstoringhelper.repositories.ILaunchRepository
import studios.devs.mobi.qrstoringhelper.repositories.IMainRepository
import studios.devs.mobi.qrstoringhelper.repositories.LaunchRepository
import studios.devs.mobi.qrstoringhelper.repositories.MainRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(): IMainRepository {
        return MainRepository()
    }

    @Provides
    @Singleton
    fun provideLaunchRepository(sharedPreferences: SharedPreferences, contentResolver: ContentResolver): ILaunchRepository {
        return LaunchRepository(sharedPreferences, contentResolver)
    }

}
