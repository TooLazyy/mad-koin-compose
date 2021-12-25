package ru.wearemad.mad_koin_compose.vm

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.StateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ViewModelOwnerDefinition
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.factory.DefaultViewModelFactory
import org.koin.androidx.viewmodel.scope.BundleDefinition
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import ru.wearemad.mad_core_compose.vm.vm_store_holder.LocalComposeScreenViewModelStoreHolder
import kotlin.reflect.KClass

@Composable
inline fun <reified T : ViewModel> Scope.getScopedViewModel(
    viewModelId: String,
    qualifier: Qualifier? = null,
    owner: ViewModelOwner = ViewModelOwner.from(
        LocalComposeScreenViewModelStoreHolder.current.getOrCreateScreenVmOwner(viewModelId),
        LocalContext.current as ComponentActivity,
    ),
    noinline parameters: ParametersDefinition? = null,
): T {
    return remember(viewModelId, qualifier, parameters) {
        getViewModel(qualifier, viewModelId, { owner }, parameters)
    }
}

@Composable
fun <T : ViewModel> Scope.getScopedViewModelByClass(
    viewModelId: String,
    vmClass: KClass<T>,
    qualifier: Qualifier? = null,
    owner: ViewModelOwner = ViewModelOwner.from(
        LocalComposeScreenViewModelStoreHolder.current.getOrCreateScreenVmOwner(viewModelId),
        LocalContext.current as ComponentActivity,
    ),
    parameters: ParametersDefinition? = null,
): T {
    return remember(viewModelId, qualifier, parameters) {
        getViewModel(
            qualifier,
            viewModelId,
            { owner },
            vmClass,
            parameters = parameters
        )
    }
}

inline fun <reified T : ViewModel> Scope.getViewModel(
    qualifier: Qualifier? = null,
    viewModelId: String,
    noinline owner: ViewModelOwnerDefinition,
    noinline parameters: ParametersDefinition? = null,
): T {
    return getViewModel(qualifier, viewModelId, owner, T::class, parameters = parameters)
}

fun <T : ViewModel> Scope.getViewModel(
    qualifier: Qualifier? = null,
    viewModelId: String,
    owner: ViewModelOwnerDefinition,
    clazz: KClass<T>,
    state: BundleDefinition? = null,
    parameters: ParametersDefinition? = null,
): T {
    val ownerDef = owner()
    return getViewModel(
        ViewModelParameter(
            clazz,
            qualifier,
            state,
            parameters,
            ownerDef.store,
            ownerDef.stateRegistry
        ),
        viewModelId
    )
}

internal fun <T : ViewModel> Scope.getViewModel(
    viewModelParameters: ViewModelParameter<T>,
    viewModelId: String,
): T {
    val viewModelProvider = ViewModelProvider(
        viewModelParameters.viewModelStore,
        pickFactory(viewModelParameters)
    )
    return viewModelProvider.resolveInstance(
        viewModelParameters,
        viewModelId
    )
}

internal fun <T : ViewModel> ViewModelProvider.resolveInstance(
    viewModelParameters: ViewModelParameter<T>,
    viewModelId: String,
): T {
    val javaClass = viewModelParameters.clazz.java
    val customKey = viewModelParameters.qualifier?.toString() ?: viewModelId
    return get(customKey, javaClass)
}

private fun <T : ViewModel> Scope.pickFactory(
    viewModelParameters: ViewModelParameter<T>,
): ViewModelProvider.Factory = if (viewModelParameters.registryOwner != null) {
    StateViewModelFactory(this, viewModelParameters)
} else {
    DefaultViewModelFactory(this, viewModelParameters)
}