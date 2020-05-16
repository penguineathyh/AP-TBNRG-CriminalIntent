package com.example.criminalintent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.CrimeListViewModel
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import kotlinx.android.synthetic.main.fragment_crime_list.view.rv_crime_list
import kotlin.properties.Delegates

class CrimeListFragment private constructor() : Fragment() {

    private val viewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    private lateinit var crimeListRecyclerView: RecyclerView
    private lateinit var crimeListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeListRecyclerView = view.rv_crime_list
        crimeListAdapter = Adapter().also {
            it.crimes = viewModel.crimes
            crimeListRecyclerView.adapter = it
            crimeListRecyclerView.layoutManager = LinearLayoutManager(context)
        }

        return view
    }

    private class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var crimes: List<Crime> by Delegates.observable(listOf()) { _, oldValue, newValue ->
            DiffUtil.calculateDiff(DiffCallback(oldValue, newValue)).dispatchUpdatesTo(this)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_TYPE_REQUIRE_POLICE -> RequirePoliceViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_crime_list_require_police, parent, false)
                )
                else -> DefaultViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_crime_list_default, parent, false)
                )
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ViewHolder).bind(crimes[position])
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].requirePolice) VIEW_TYPE_REQUIRE_POLICE
            else VIEW_TYPE_DEFAULT
        }

        private abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            abstract fun bind(crime: Crime)
        }

        private class DefaultViewHolder(view: View) : ViewHolder(view) {
            private val crimeTitle: TextView = view.findViewById(R.id.text_view_item_crime_title)
            private val crimeDate: TextView = view.findViewById(R.id.text_view_item_crime_date)

            override fun bind(crime: Crime) {
                crimeTitle.text = crime.title
                crimeDate.text = crime.date.toString()
            }
        }

        private class RequirePoliceViewHolder(view: View) : ViewHolder(view) {
            private val crimeTitle: TextView = view.findViewById(R.id.text_view_item_crime_title)
//            private val requirePolice: Button = view.findViewById(R.id.button_item_require_police)

            override fun bind(crime: Crime) {
                crimeTitle.text = crime.title
            }
        }


        private class DiffCallback(
            val oldList: List<Crime>,
            val newList: List<Crime>
        ) : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        }

        companion object {
            private const val VIEW_TYPE_DEFAULT = 0
            private const val VIEW_TYPE_REQUIRE_POLICE = 1
        }
    }

    companion object {

        const val TAG_CRIME_LIST_FRAGMENT = "crime.list.fragment"

        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}