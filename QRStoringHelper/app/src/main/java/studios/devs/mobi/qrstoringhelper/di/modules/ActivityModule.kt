package studios.devs.mobi.qrstoringhelper.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studios.devs.mobi.qrstoringhelper.ui.LaunchActivity
import studios.devs.mobi.qrstoringhelper.ui.MainActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeActivityInjector(): MainActivity
    @ContributesAndroidInjector
    abstract fun contributeLaunchActivityInjector(): LaunchActivity
}
