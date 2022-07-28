package ru.wearemad.mad_koin_compose.vm

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.StateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.ViewModelStoreOwnerProducer
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
    noinline saveState: BundleDefinition = { Bundle() },
    savedStateOwner: SavedStateRegistryOwner? = LocalContext.current as? SavedStateRegistryOwner,
    owner: ViewModelStoreOwner = LocalComposeScreenViewModelStoreHolder.current.getOrCreateScreenVmOwner(viewModelId),
    noinline parameters: ParametersDefinition? = null,
): T = remember(viewModelId, qualifier, parameters) {
    getViewModel(
        viewModelId,
        { owner },
        qualifier,
        parameters,
        saveState,
        savedStateOwner = savedStateOwner,
    )
}

@Composable
fun <T : ViewModel> Scope.getScopedViewModelByClass(
    viewModelId: String,
    vmClass: KClass<T>,
    qualifier: Qualifier? = null,
    saveState: BundleDefinition = { Bundle() },
    savedStateOwner: SavedStateRegistryOwner? = LocalContext.current as? SavedStateRegistryOwner,
    owner: ViewModelStoreOwner = LocalComposeScreenViewModelStoreHolder.current.getOrCreateScreenVmOwner(viewModelId),
    parameters: ParametersDefinition? = null,
): T = remember(viewModelId, qualifier, parameters) {
    getViewModel(
        viewModelId,
        { owner },
        vmClass,
        qualifier,
        parameters = parameters,
        saveState = saveState,
        savedStateOwner = savedStateOwner,
    )
}

inline fun <reified T : ViewModel> Scope.getViewModel(
    viewModelId: String,
    noinline owner: ViewModelStoreOwnerProducer,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
    noinline saveState: BundleDefinition = { Bundle() },
    savedStateOwner: SavedStateRegistryOwner? = null
): T = getViewModel(
    viewModelId,
    owner,
    T::class,
    qualifier = qualifier,
    parameters = parameters,
    saveState = saveState,
    savedStateOwner = savedStateOwner,
)

fun <T : ViewModel> Scope.getViewModel(
    viewModelId: String,
    owner: ViewModelStoreOwnerProducer,
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null,
    saveState: BundleDefinition? = { Bundle() },
    savedStateOwner: SavedStateRegistryOwner? = null
): T {
    val ownerDef = owner()
    return getViewModel(
        ViewModelParameter(
            clazz,
            qualifier,
            saveState,
            parameters,
            ownerDef,
            registryOwner = savedStateOwner
        ),
        viewModelId
    )
}

internal fun <T : ViewModel> Scope.getViewModel(
    viewModelParameters: ViewModelParameter<T>,
    viewModelId: String,
): T {
    val viewModelProvider = ViewModelProvider(
        viewModelParameters.viewModelStoreOwner,
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