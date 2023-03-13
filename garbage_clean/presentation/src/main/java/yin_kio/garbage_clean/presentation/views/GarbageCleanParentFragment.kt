package yin_kio.garbage_clean.presentation.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ads.showInter
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.models.ScreenState
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.ScreenViewModelFactory

class GarbageCleanParentFragment : Fragment(R.layout.fragment_garbage_clean_parent) {

    internal val viewModel: ObservableScreenViewModel by lifecycleAware { screenViewModel() }
    private lateinit var childNavController: NavController

    private var completeId: Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        completeId = requireArguments().getInt(ARG_COMPLETE_ID)
        childNavController = findChildNavController()
        setupObserver()

    }

    private fun findChildNavController() =
        (childFragmentManager.findFragmentById(R.id.garbage_clean_fragment_container) as NavHostFragment).navController

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                goBackIfClosed(it)
                showPermissionScreen(it.hasPermission)
                showDeleteProgress(it)
                showInter(it)
            }
        }
    }



    private fun goBackIfClosed(it: ScreenState) {
        if (it.isClosed) {
            parentNavController()?.navigateUp()
        }
    }

    private fun showPermissionScreen(
        hasPermission: Boolean
    ) {
        if (!hasPermission) {
            childNavController.navigate(R.id.permissionFragment)
        } else if (destinationIs(R.id.permissionFragment)) {
            childNavController.navigateUp()
        }
    }

    private fun showDeleteProgress(it: ScreenState) {
        if (it.deleteProgressState == DeleteProgressState.Progress
            && destinationIs(R.id.garbageCleanFragment)
        ) {
            childNavController.navigate(R.id.deleteProgressDialog)
        }
    }

    private fun showInter(screenState: ScreenState){
        if (screenState.deleteProgressState == DeleteProgressState.Complete
            && destinationIs(R.id.deleteProgressDialog)
        ){
            showInter {
                parentNavController()?.navigate(completeId, advicesArgs(screenState))
            }
        }
    }

    private fun advicesArgs(screenState: ScreenState) : Bundle {
        return Bundle().apply {
            putString(ADVICES_DIALOG_TITLE, getString(R.string.your_phone_is_cleaned))
            putString(ADVICES_DIALOG_DESCRIPTION, screenState.freed)
        }
    }

    private fun destinationIs(id: Int) : Boolean{
        return childNavController.currentDestination?.id == id
    }

    private fun parentNavController() = try {
        findNavController()
    } catch (ex: Exception) {
        null
    }






    private fun ViewModel.screenViewModel() = ScreenViewModelFactory().create(
        applicationContext = requireActivity().applicationContext,
        androidViewModel = this
    )

    companion object{
        private const val ADVICES_DIALOG_TITLE = "dialog_title"
        private const val ADVICES_DIALOG_DESCRIPTION = "dialog_description"
        private const val ARG_COMPLETE_ID = "completeId"
    }
}