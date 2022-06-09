package com.example.chatapp.ui.otp

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.core.BaseFragment
import com.example.chatapp.databinding.FragmentCountriesCodeBinding
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.country_code.CountryCode
import com.example.chatapp.utils.Constants.COUNTRY_CODE_REQUEST
import com.example.chatapp.utils.Constants.COUNTRY_CODE_SELECTED
import com.example.chatapp.utils.VerticalMarginItemDecoration
import com.example.chatapp.utils.autoCleared
import com.example.chatapp.utils.dpToPx
import com.example.chatapp.utils.extension.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CountriesCodeFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentCountriesCodeBinding by autoCleared()

    private val viewModel: CountriesCodeViewModel by viewModels()

    private lateinit var adapter: CountriesCodeAdapter

    private var navController: NavController? = null

    private var countryCodeSelected: CountryCode? = null

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryCodeSelected = it.getParcelable<CountryCode>(COUNTRY_CODE_SELECTED)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        binding = FragmentCountriesCodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)

        binding.toolBar.setNavigationOnClickListener(this)

        navController = findNavController()

        setupCountriesCodeAdapter()

        lifecycleScope.launchWhenStarted {
            viewModel.countriesCodesSF.collect { dataState->
                when(dataState.status){
                    DataState.Status.SUCCESS -> {
                        displayLoading(false)
                        adapter.submitList(dataState.data)
                    }
                    DataState.Status.ERROR -> {
                        displayLoading(false)
                        dataState.error?.throwableMessage?.let { toastMy(it) }
                    }
                    DataState.Status.LOADING -> {
                        displayLoading(true)
                    }
                }

            }
        }

        viewModel.countriesCodesFilteredSF.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupCountriesCodeAdapter(){
        adapter = CountriesCodeAdapter(requireContext(), countryCodeSelected){
            setFragmentResult(COUNTRY_CODE_REQUEST, bundleOf(COUNTRY_CODE_SELECTED to it))
            navController?.navigateUp()
        }
        binding.rvCountriesCode.adapter = adapter
        binding.rvCountriesCode.addItemDecoration(
                VerticalMarginItemDecoration(
                    dpToPx(10f, requireContext())
                )
            )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty() && pendingQuery != "") {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            viewModel.searchCountry(it)
        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true
            }
        })

        searchView.onQueryTextChanged { country ->
            viewModel.setSearchQueryValue(country)
        }
    }

    override fun onClick(p0: View?) {
        navController?.popBackStack()
    }

}