
package esi.g52854.projet.connexion

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
import androidx.recyclerview.widget.LinearLayoutManager
import esi.g52854.projet.R
import esi.g52854.projet.database.User
import esi.g52854.projet.database.UserViewModel
import esi.g52854.projet.databinding.FragmentConnexionBinding


class ConnexionFragment : Fragment() {

    private lateinit var binding: FragmentConnexionBinding
    private lateinit var viewModel: ConnexionViewModel
    private lateinit var _UserViewModel : UserViewModel

    var list: MutableList<String?> =  mutableListOf()
    var listOfId: MutableList<Long?> =  mutableListOf()
    lateinit var adapter:ArrayAdapter<String?>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                 R.layout.fragment_connexion,container,false)
        _UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.connexionButton.setOnClickListener{
            checkEmail()
        }
        binding.autoCompleteTextView.setOnClickListener{
            binding.autoCompleteTextView.showDropDown()
        }
        setAdapter()
        viewModel = ViewModelProvider(this).get(ConnexionViewModel::class.java)
        return binding.root
    }

    private fun setAdapter() {

        adapter = ArrayAdapter<String?>(activity!!,
                android.R.layout.simple_list_item_1,list)
        binding.autoCompleteTextView.setAdapter(adapter)
        _UserViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            user.forEach{
                list.add(it.email.toString())
                listOfId.add(it.userId)
            }
        })
    }

    private fun insertEmailToDatabase(email : String) {
        val user = User(0,System.currentTimeMillis(),email)
        if(!list.contains(email)){

            _UserViewModel.addUser(user)

            Toast.makeText(activity,getString(R.string.added_email) , Toast.LENGTH_SHORT).show()
        }else{
            user.userId = listOfId.get(list.indexOf(email))!!
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