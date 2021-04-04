
package esi.g52854.projet.fragment.connexion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import esi.g52854.projet.R
import esi.g52854.projet.database.User
import esi.g52854.projet.database.UserViewModel
import esi.g52854.projet.databinding.FragmentConnexionBinding


class ConnexionFragment : Fragment() {

    private lateinit var binding: FragmentConnexionBinding
    private lateinit var viewModel: ConnexionViewModel
    private lateinit var _UserViewModel : UserViewModel

    lateinit var adapter:ArrayAdapter<String?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
                 R.layout.fragment_connexion,container,false)

        _UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ConnexionViewModel::class.java)

        binding.connexionButton.setOnClickListener{
            checkEmail()
        }
        binding.autoCompleteTextView.setOnClickListener{
            binding.autoCompleteTextView.showDropDown()
        }
        setAdapter()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.listFragment)
        }

        return binding.root
    }

    private fun setAdapter() {
            adapter = ArrayAdapter<String?>(requireActivity(),
                    android.R.layout.simple_list_item_1, viewModel.list.value!!)
            _UserViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
                user.forEach {
                    viewModel.test(it.email,it.userId)
                }
            })

        binding.autoCompleteTextView.setAdapter(adapter)
    }

    private fun insertEmailToDatabase(email : String) {
        val user = User(0,System.currentTimeMillis(),email)
        if(!viewModel.contains(email)){

            _UserViewModel.addUser(user)

            Toast.makeText(activity,getString(R.string.added_email) , Toast.LENGTH_SHORT).show()
        }else{
            user.userId = viewModel.listOfId.value?.get(viewModel.list.value?.indexOf(email)!!)!!
            _UserViewModel.updateUser(user)

            Toast.makeText(activity,getString(R.string.updatedEmail) , Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkEmail() {
        val email = binding.autoCompleteTextView.text.toString()

        if(viewModel.checkEmail(email))  {

            insertEmailToDatabase(email)
        } else {
            Toast.makeText(activity,getString(R.string.unvalid_email) , Toast.LENGTH_SHORT).show()
        }

    }

}