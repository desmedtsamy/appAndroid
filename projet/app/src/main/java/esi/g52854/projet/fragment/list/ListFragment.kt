package esi.g52854.projet.fragment.list

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.Communicator
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import esi.g52854.projet.database.recipe.RecipeViewModel
import esi.g52854.projet.databinding.FragmentListBinding

const val KEY_RECETTES = "recettes_key"


class ListFragment: Fragment() {

    private lateinit var mRecipeViewModel: RecipeViewModel
    private lateinit var binding: FragmentListBinding
    private lateinit var recettesArray :MutableList<Recette>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
Log.i("prout","onCreateView")
recettesArray = mutableListOf()


        val db = Firebase.firestore
        val recettes = db.collection("recette")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list, container, false
        )

        val model= ViewModelProviders.of(requireActivity()).get(Communicator::class.java)

        val adapter = ListAdapter()
        adapter.setModel(model)
        adapter.setNavController(findNavController())
        adapter.setDays(resources.getStringArray(R.array.day_array))
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recettes.get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val titre = document.data["titre"] as String
                        val time = Integer.parseInt(document.data["prepaduration"] as String)
                        val timeTotal = Integer.parseInt(document.data["time"] as String)
                        val nbPerson = Integer.parseInt(document.data["people"] as String)
                        val difficulty = document.data["difficulty"] as String

                        val steps = document.data["steps"] as  List<String>
                        val ingredients = document.data["ingredients"] as  List<String>

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
                    adapter.setData(recettesArray)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.fragment_connexion)
        }
        this.binding.swiperefresh.setOnRefreshListener {
            recettesArray.shuffle()
            adapter.setData(recettesArray)
            this.binding.swiperefresh.isRefreshing = false
        }
        return binding.root
    }
    override fun onPause() {
        super.onPause()
        Log.i("prout","onPause Called")
    }
    override fun onResume() {
        super.onResume()
        Log.i("prout","resume Called")

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val idArray = mutableListOf<String>()
        recettesArray.forEach{
            idArray.add(it.id)
        }
        outState.putStringArrayList(KEY_RECETTES, ArrayList(idArray))
        Log.i("prout","saved data")
    }
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Log.i("prout","onCreateView")
    if (savedInstanceState != null) {

        val idArray = savedInstanceState.getStringArray(KEY_RECETTES)
        idArray?.forEach {
            Log.i("prout","test" +it)
        }
    }
}
}