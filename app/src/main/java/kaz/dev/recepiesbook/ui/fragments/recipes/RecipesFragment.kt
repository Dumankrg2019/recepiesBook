package kaz.dev.recepiesbook.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kaz.dev.recepiesbook.MainViewModel
import kaz.dev.recepiesbook.R
import kaz.dev.recepiesbook.adapter.RecipesAdapter
import kaz.dev.recepiesbook.databinding.FragmentRecipesBinding
import kotlinx.android.synthetic.main.fragment_recipes.view.*


class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        mView.rvRecipes.adapter = mAdapter
        mView.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        mView.shimmerFrameLayout.startShimmer()
    }

    private fun requestApiData() {
        mainViewModel.getRecipes()
    }

    private fun hideShimmerEffect() {
        mView.shimmerFrameLayout.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}