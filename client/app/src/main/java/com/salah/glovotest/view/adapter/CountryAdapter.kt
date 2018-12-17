package com.salah.glovotest.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

import com.salah.glovotest.R
import com.salah.glovotest.model.CityModel
import com.salah.glovotest.model.CountryModel

import java.util.ArrayList

/**
 * Created by salah on 12/15/18.
 */

class CountryAdapter(private val context: Context, private val countries: List<CountryModel>) : BaseExpandableListAdapter() {

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val country = getGroup(groupPosition) as CountryModel

        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }

        val tvCountryName = convertView!!.findViewById<TextView>(R.id.tv_group_name)
        tvCountryName.text = country.name

        if (isExpanded) {
            convertView.findViewById<View>(R.id.arrow).setBackgroundResource(R.drawable.down)
        } else {
            convertView.findViewById<View>(R.id.arrow).setBackgroundResource(R.drawable.next)
        }

        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val city = getChild(groupPosition, childPosition) as CityModel
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }

        val tvCityName = convertView!!.findViewById<TextView>(R.id.tv_name)
        tvCityName.text = city.name

        return convertView
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val cities = countries[groupPosition].cities
        return cities!![childPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return this.countries.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.countries[groupPosition].cities!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.countries[groupPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
