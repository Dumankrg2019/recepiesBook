package kaz.dev.recepiesbook.ui.fragments.recipes

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kaz.dev.recepiesbook.Constance.Companion.API_KEY
import kaz.dev.recepiesbook.viewmodels.MainViewModel
import kaz.dev.recepiesbook.R
import kaz.dev.recepiesbook.adapter.RecipesAdapter
import kaz.dev.recepiesbook.databinding.FragmentRecipesBinding
import kaz.dev.recepiesbook.util.NetworkResult
import kaz.dev.recepiesbook.util.observeOnce
import kaz.dev.recepiesbook.viewmodels.RecipesViewModel
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val  binding get() = _binding!!


    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }



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

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setUpRecyclerView()

        readDatabase()
    }

    private fun setUpRecyclerView() {
        binding.rvRecipes.adapter = mAdapter
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun readDatabase() {
       lifecycleScope.launch {
           mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, {database ->
               if(database.isNotEmpty()) {
                   Log.e("RecipesFragment", "readDatabase called!")
                   mAdapter.setData(database[0].foodRecipes)
                   hideShimmerEffect()
               } else {
                   requestApiData()
               }
           })
       }
    }


    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestApiData() {
        Log.e("RecipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, {response->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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

    private fun loadDataFromCache() {
       lifecycleScope.launch {
           mainViewModel.readRecipes.observe(viewLifecycleOwner, {database->
               if(database.isNotEmpty()) {
                   mAdapter.setData(database[0].foodRecipes)
               }
           })
       }
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.hideShimmer()
        binding.shimmerFrameLayout.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}