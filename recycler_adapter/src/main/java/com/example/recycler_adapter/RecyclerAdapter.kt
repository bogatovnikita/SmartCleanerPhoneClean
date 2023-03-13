package com.example.recycler_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


inline fun <T, reified VB: ViewBinding> recyclerAdapter(
    noinline onBind: VB.(T, SingleTypeViewHolder<T, VB>) -> Unit,
    noinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new},
    noinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new},
    noinline onViewHolderCreated: VB.(SingleTypeViewHolder<T, VB>) -> Unit = {}
) = BindingAdapter(
    _class = VB::class.java,
    onBind, areItemsTheSame, areContentsTheSame, onViewHolderCreated
)

class BindingAdapter<T, VB : ViewBinding>(
    private val _class: Class<VB>,
    private val onBind: VB.(T, SingleTypeViewHolder<T, VB>) -> Unit,
    areItemsTheSame: (T, T) -> Boolean = { item1, item2 -> item1 == item2},
    areContentsTheSame: (T, T) -> Boolean = { item1, item2 -> item1 == item2},
    private val onViewHolderCreated: VB.(SingleTypeViewHolder<T, VB>) -> Unit
) : ListAdapter<T, SingleTypeViewHolder<T, VB>>(itemCallback(areItemsTheSame, areContentsTheSame))
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleTypeViewHolder<T, VB> {
        return SingleTypeViewHolder.from<T, VB>(_class, parent).also {
            onViewHolderCreated(it.viewBinding, it)
        }
    }

    override fun onBindViewHolder(holder: SingleTypeViewHolder<T, VB>, position: Int) {
        holder.bind(getItem(position), onBind)
    }

}

inline fun <T> itemCallback(
    crossinline areItemsTheSame: (T, T) -> Boolean,
    crossinline areContentsTheSame: (T, T) -> Boolean
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T) =
        areContentsTheSame(oldItem, newItem)
}

class SingleTypeViewHolder<T, VB : ViewBinding> constructor(
    val viewBinding: VB
)  : RecyclerView.ViewHolder(viewBinding.root){

    private var _currentItem: T? = null
    val currentItem get() = _currentItem

    fun bind(item: T, bind: VB.(T, SingleTypeViewHolder<T, VB>) -> Unit){
        _currentItem = item
        viewBinding.bind(item, this)
    }

    @Suppress("unchecked_cast")
    companion object {
        fun <T, VB : ViewBinding>from(
            vbClass: Class<VB>,
            parent: ViewGroup
        ) : SingleTypeViewHolder<T, VB> {

            val method = vbClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            val inflater = LayoutInflater.from(parent.context)
            val binding = method.invoke(vbClass, inflater, parent, false) as VB
            return SingleTypeViewHolder(binding)
        }
    }
}