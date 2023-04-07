package kaz.dev.recepiesbook.ui.fragments.recipes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kaz.dev.recepiesbook.Constance.Companion.API_KEY
import kaz.dev.recepiesbook.viewmodels.MainViewModel
import kaz.dev.recepiesbook.R
import kaz.dev.recepiesbook.adapter.RecipesAdapter
import kaz.dev.recepiesbook.util.NetworkResult
import kaz.dev.recepiesbook.viewmodels.RecipesViewModel
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        return mView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setUpRecyclerView()

        requestApiData()
    }

    private fun setUpRecyclerView() {
        mView.rvRecipes.adapter = mAdapter
        mView.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
    private fun showShimmerEffect() {
        mView.shimmerFrameLayout.startShimmer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, {response->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireActivity(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                   showShimmerEffect()
                }
            }
        })
    }

    private fun hideShimmerEffect() {
        mView.shimmerFrameLayout.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}