package esi.g52854.projet.fragment.list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import esi.g52854.projet.Communicator
import esi.g52854.projet.MainActivity
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentListBinding


class ListFragment: Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
            ): View? {

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list, container, false
        )

        val model= ViewModelProviders.of(requireActivity()).get(Communicator::class.java)
        viewModel.init((requireActivity() as MainActivity).user,resources.getStringArray(R.array.day_array),model,findNavController())
        val recyclerView = binding.recyclerview
        recyclerView.adapter = viewModel.adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.floatingActionButton.setOnClickListener {

            findNavController().navigate(R.id.fragment_add)
        }

        binding.disconnect.setOnClickListener {
            disconnect()
        }

        this.binding.swiperefresh.setOnRefreshListener {
            viewModel.refresh()
            this.binding.swiperefresh.isRefreshing = false
        }

        return binding.root
    }
    private fun disconnect(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.signOut()
        Toast.makeText(requireActivity(), "You are Logged Out", Toast.LENGTH_SHORT).show()
        (requireActivity() as MainActivity).user = "0"
        findNavController().navigate(R.id.connectionFragment)
    }
}
