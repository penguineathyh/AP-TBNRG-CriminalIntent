package com.example.criminalintent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.CrimeListViewModel
import com.example.criminalintent.DateUtil
import com.example.criminalintent.R
import com.example.criminalintent.model.Crime
import kotlinx.android.synthetic.main.fragment_crime_list.view.rv_crime_list
import java.util.UUID
import kotlin.properties.Delegates

class CrimeListFragment : Fragment() {

    /**
     * Let hosting activity implement this.
     * By this, hosting activity can have its own inner fragment managing business.
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    private val viewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    private lateinit var crimeListRecyclerView: RecyclerView
    private lateinit var crimeListAdapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeListRecyclerView = view.rv_crime_list
        crimeListAdapter = Adapter().also {
            crimeListRecyclerView.adapter = it
            crimeListRecyclerView.layoutManager = LinearLayoutManager(context)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes -> crimes?.let { crimeListAdapter.crimes = it } }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callbacks = activity as? Callbacks
    }

    override fun onDestroy() {
        super.onDestroy()
        callbacks = null
    }

    private inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var crimes: List<Crime> by Delegates.observable(listOf()) { _, oldValue, newValue ->
            DiffUtil.calculateDiff(DiffCallback(oldValue, newValue)).dispatchUpdatesTo(this)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_crime_list, parent, false),
                callbacks
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ViewHolder).bind(crimes[position])
        }

        override fun getItemCount(): Int {
            return crimes.size
        }
    }

    private class ViewHolder(view: View, callbacks: Callbacks?) :
        RecyclerView.ViewHolder(view) {
        private val crimeTitle: TextView = view.findViewById(R.id.text_view_item_crime_title)
        private val crimeDate: TextView = view.findViewById(R.id.text_view_item_crime_date)
        private val crimeSolved: ImageView =
            view.findViewById(R.id.image_view_item_crime_solved)

        private lateinit var crime: Crime
        private val crimeId: UUID get() = crime.id

        init {
            view.setOnClickListener {
                callbacks?.onCrimeSelected(crimeId)
            }
        }

        fun bind(crime: Crime) {
            this.crime = crime
            crimeTitle.text = crime.title
            crimeDate.text = DateUtil.format(crime.date)
            crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
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

        const val TAG_CRIME_LIST_FRAGMENT = "crime.list.fragment"

        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}