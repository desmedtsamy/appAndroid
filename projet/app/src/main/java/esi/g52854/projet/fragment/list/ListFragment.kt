package esi.g52854.projet.fragment.list

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.Communicator
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import esi.g52854.projet.databinding.FragmentListBinding

class ListFragment: Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var adapter : ListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
            ): View? {


         db = Firebase.firestore
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list, container, false
        )

        val model= ViewModelProviders.of(requireActivity()).get(Communicator::class.java)

         adapter = ListAdapter()
        adapter.setModel(model)
        adapter.setNavController(findNavController())
        adapter.setDays(resources.getStringArray(R.array.day_array))
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        initRecettesArray()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.fragment_add)
        }
        this.binding.swiperefresh.setOnRefreshListener {
             db.collection("week")
                .get()
                .addOnSuccessListener { documents ->

                    Log.i("db", documents.size().toString())
                    for (document in documents) {
                       document.reference.delete()
                    }
                    initRecettesArray()
                }
              this.binding.swiperefresh.isRefreshing = false

        }
        return binding.root
    }
    private fun updateView(recettesArray : MutableList<Recette>){
        adapter.setData(recettesArray)

    }
    private fun addDB(recettesArray : MutableList<Recette>){
        Log.i("db","ajout dans la db "+recettesArray.size)
        recettesArray.take(7).forEach {
            db.collection("week")
                .add(it)

        }
    }
private fun initRecettesArray(){
    val recettesArray = mutableListOf<Recette>()
    db.collection("week").get().addOnSuccessListener { result ->
        Log.i("db", result.size().toString())
        if (result.isEmpty) {
            Log.i("db","recette")

            db.collection("recette").get()
                .addOnSuccessListener { result ->
                    for (document in result.take(7)) {
                        val titre = document.data["titre"] as String
                        val time = document.data["prepaduration"] as String
                        val timeTotal = document.data["time"] as String
                        val nbPerson = document.data["people"] as String
                        val difficulty = document.data["difficulty"] as String

                        val steps = document.data["steps"] as List<String>
                        val ingredients = document.data["ingredients"] as List<String>
Log.i("db",titre)
                        recettesArray.add(
                            Recette(
                                document.id,
                                titre,
                                difficulty,
                                nbPerson,
                                time,
                                timeTotal,
                                steps,
                                ingredients
                            )
                        )
                    }
                    recettesArray.shuffle()
                    addDB(recettesArray)
                    updateView(recettesArray)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }


        } else {
            db.collection("week").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val titre = document.data["titre"] as String
                        val time = document.data["prepaduration"] as String
                        val timeTotal = document.data["time"] as String
                        val nbPerson = document.data["people"] as String
                        val difficulty = document.data["difficulty"] as String

                        val steps = document.data["steps"] as List<String>
                        val ingredients = document.data["ingredients"] as List<String>
                        Log.i("db",titre)

                        recettesArray.add(
                            Recette(
                                document.id,
                                titre,
                                difficulty,
                                nbPerson,
                                time,
                                timeTotal,
                                steps,
                                ingredients
                            )
                        )
                    }

                    updateView(recettesArray)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }

    }
}
}