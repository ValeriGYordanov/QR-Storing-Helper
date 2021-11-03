package studios.devs.mobi.qrstoringhelper.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import studios.devs.mobi.qrstoringhelper.di.ViewModelKey
import studios.devs.mobi.qrstoringhelper.di.ViewModelProviderFactory
import studios.devs.mobi.qrstoringhelper.viewmodels.LaunchViewModel
import studios.devs.mobi.qrstoringhelper.viewmodels.MainViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    internal abstract fun launchViewModel(launchViewModel: LaunchViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory


}
