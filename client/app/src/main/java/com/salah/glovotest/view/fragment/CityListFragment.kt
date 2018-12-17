package com.salah.glovotest.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import com.salah.glovotest.R
import com.salah.glovotest.model.CountryModel
import com.salah.glovotest.view.adapter.CountryAdapter
import com.salah.glovotest.viewmodel.CityListViewModel
import com.salah.glovotest.viewmodel.HomeViewModel


/**
 * Created by salah on 12/15/18.
 */
class CityListFragment : BaseFragment() {

    private lateinit var countryListView: ExpandableListView

    private lateinit var cityListViewModel: CityListViewModel

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var adapter: CountryAdapter

    override fun getContentLayout(): Int {
        return R.layout.fragment_city_list
    }

    override fun initializeComponents(view: View, savedInstanceState: Bundle?) {
        countryListView = view.findViewById(R.id.expandable_list)
    }

    override fun initializeData() {
        cityListViewModel = ViewModelProviders.of(activity!!).get(CityListViewModel::class.java)
        homeViewModel = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
        getCountries()
    }

    override fun initializeActions() {
        countryListView.setOnChildClickListener({ expandableListView, view, groupPosition, childPosition, l ->

            val country = adapter.getGroup(groupPosition) as CountryModel
            val city = country.cities!![childPosition]
            homeViewModel.setCity(city)

            activity!!.supportFragmentManager.popBackStack()

            false
        })
    }

    private fun getCountries() {
        cityListViewModel.getCountries(homeViewModel.cities.value!!).observe(this, Observer<List<CountryModel>> { countries ->
            adapter = CountryAdapter(context!!, countries!!)
            countryListView.setAdapter(adapter)
        })
    }
}