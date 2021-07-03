package esi.g52854.projet.fragment.list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import esi.g52854.projet.Communicator
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentListBinding

class ListFragment: Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter : ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
            ): View? {


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list, container, false
        )

        val model= ViewModelProviders.of(requireActivity()).get(Communicator::class.java)

        adapter = ListAdapter()
        adapter.init(model,findNavController(),resources.getStringArray(R.array.day_array))

        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.floatingActionButton.setOnClickListener {

            findNavController().navigate(R.id.fragment_add)
        }
        this.binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
              this.binding.swiperefresh.isRefreshing = false

        }
        return binding.root
    }
}
