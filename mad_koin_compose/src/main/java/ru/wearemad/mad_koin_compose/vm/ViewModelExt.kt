package ru.wearemad.mad_koin_compose.vm

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.viewmodel.resolveViewModel
import ru.wearemad.mad_core_compose.vm.vm_store_holder.LocalComposeScreenViewModelStoreHolder
import kotlin.reflect.KClass

@OptIn(KoinInternalApi::class)
@Composable
inline fun <reified T : ViewModel> Scope.getScopedViewModel(
    viewModelId: String,
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner = LocalComposeScreenViewModelStoreHolder.current.getOrCreateScreenVmOwner(viewModelId),
    noinline parameters: ParametersDefinition? = null,
): T = getScopedViewModelByClass(viewModelId, T::class, qualifier, owner, parameters)

@OptIn(KoinInternalApi::class)
@Composable
fun <T : ViewModel> Scope.getScopedViewModelByClass(
    viewModelId: String,
    vmClass: KClass<T>,
    qualifier: Qualifier? = null,
    owner: ViewModelStoreOwner = LocalComposeScreenViewModelStoreHolder.current.getOrCreateScreenVmOwner(viewModelId),
    parameters: ParametersDefinition? = null,
): T {
    val registryOwner = LocalContext.current as? SavedStateRegistryOwner
    val extras = buildExtras(owner, registryOwner)
    return remember(viewModelId) {
        resolveViewModel(
            vmClass = vmClass,
            viewModelStore = owner.viewModelStore,
            key = viewModelId,
            extras = extras,
            qualifier = qualifier,
            scope = this,
            parameters = parameters,
        )
    }
}

private fun buildExtras(
    owner: ViewModelStoreOwner,
    registryOwner: SavedStateRegistryOwner?,
): CreationExtras = MutableCreationExtras().apply {
    set(VIEW_MODEL_STORE_OWNER_KEY, owner)
    if (registryOwner != null) {
        set(SAVED_STATE_REGISTRY_OWNER_KEY, registryOwner)
        set(DEFAULT_ARGS_KEY, Bundle())
    }
}