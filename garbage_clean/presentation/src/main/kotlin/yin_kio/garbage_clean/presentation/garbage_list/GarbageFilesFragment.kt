package yin_kio.garbage_clean.presentation.garbage_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.LifecycleAware
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.data.FilesImpl
import yin_kio.garbage_clean.data.PermissionsImpl
import yin_kio.garbage_clean.data.StorageInfoImpl
import yin_kio.garbage_clean.domain.GarbageFilesFactory
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.garbage_list.adapter.GarbageAdapter
import yin_kio.garbage_clean.presentation.databinding.FragmentGarbageFilesBinding


class GarbageFilesFragment : Fragment(R.layout.fragment_garbage_files) {


    private val binding: FragmentGarbageFilesBinding by viewBinding()
    private val viewModel: ViewModel by lifecycleAware { createViewModel() }

    private val adapter = GarbageAdapter(
        onItemUpdate = {_,_,_ ->},
        onGroupUpdate = {_,_ ->}
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        binding.button.setOnClickListener { viewModel.scan() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect{
                binding.size.text = it.size
                binding.button.text = it.buttonText

                adapter.garbage = it.garbage


                adapter.notifyDataSetChanged()

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commands.collect{
                when(it){
                    Command.ShowDialog -> {
                        Log.d("!!!", "ShowDialog")
                    }
                }
            }
        }

    }


    override fun onStart() {
        super.onStart()
        viewModel.start()
    }



    private fun LifecycleAware.createViewModel(): ViewModel {
        val context = requireContext().applicationContext

        val uiOuter = UIOuterImpl(
            presenter = Presenter(context)
        )

        val useCases = GarbageFilesFactory.createGarbageFilesUseCases(
            uiOuter = uiOuter,
            permissions = PermissionsImpl(context),
            files = FilesImpl(),
            storageInfo = StorageInfoImpl(context),
            coroutineScope = viewModelScope
        )

        val viewModel = ViewModel(
            useCases = useCases,
            coroutineScope = viewModelScope
        )

        uiOuter.viewModel = viewModel

        return viewModel
    }
}