package jamycake.lifecycle_aware

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController

inline fun <reified T : Any> Fragment.previousBackStackEntry(
    noinline onCleared: ViewModel.() -> Unit = {},
    noinline creator: LifecycleAware.() -> T = { created as T }
): Lazy<T> {
    return LifeCycleAwareContainer(
        key = T::class.java.name,
        onCleared = onCleared,
        creator = creator,
        viewModelStoreOwner = { findNavController().previousBackStackEntry!! }
    )
}

inline fun <reified T : Any> Fragment.currentBackStackEntry(
    noinline onCleared: ViewModel.() -> Unit = {},
    noinline creator: LifecycleAware.() -> T = { created as T }
): Lazy<T> {
    return LifeCycleAwareContainer(
        key = T::class.java.name,
        onCleared = onCleared,
        creator = creator,
        viewModelStoreOwner = { findNavController().currentBackStackEntry!! }
    )
}

inline fun <reified T : Any> Fragment.lifecycleAware(
    noinline viewModelStoreOwner: () -> ViewModelStoreOwner = { this },
    noinline onCleared: ViewModel.() -> Unit = {},
    noinline creator: LifecycleAware.() -> T = { created as T }
): Lazy<T> {
    return LifeCycleAwareContainer(
        key = T::class.java.name,
        onCleared = onCleared,
        creator = creator,
        viewModelStoreOwner =  viewModelStoreOwner
    )
}

inline fun <reified T : Any> AppCompatActivity.lifecycleAware(
    noinline viewModelStoreOwner: () -> ViewModelStoreOwner = { this },
    noinline onCleared: ViewModel.() -> Unit = {},
    noinline creator: ViewModel.() -> T
) : Lazy<T>{
    return LifeCycleAwareContainer(
        key = T::class.java.name,
        onCleared = onCleared,
        creator = creator,
        viewModelStoreOwner = viewModelStoreOwner
    )
}

class LifeCycleAwareContainer <T : Any>(
    private val key: String,
    private val onCleared: ViewModel.() -> Unit,
    private val creator: LifecycleAware.() -> T,
    private val viewModelStoreOwner: () -> ViewModelStoreOwner
) : Lazy<T>{

    private var cached: T? = null
    @Suppress("unchecked_cast")
    override val value: T
        get() {

            if (cached == null){
                cached = ViewModelProvider(viewModelStoreOwner(),
                    LifecycleAware.Factory(onCleared, creator)
                )[key, LifecycleAware::class.java]
                .created as T
            }

            return cached!!
        }

    override fun isInitialized(): Boolean = cached != null
}

@Suppress("unchecked_cast")
class LifecycleAware(
    private val onCleared: ViewModel.() -> Unit,
    creator: LifecycleAware.() -> Any
) : ViewModel() {

    val created by lazy { creator.invoke(this) }

    override fun onCleared() {
        this.onCleared.invoke(this)
    }

    class Factory<R : Any>(
        private val onCleared: ViewModel.() -> Unit,
        private val creator: LifecycleAware.() -> R
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LifecycleAware(onCleared, creator) as T
        }
    }

}