
package esi.g52854.projet.fragment.connexion

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.R
import esi.g52854.projet.database.recipe.RecipeViewModel
import esi.g52854.projet.databinding.FragmentConnexionBinding


class ConnexionFragment : Fragment() {

    private lateinit var binding: FragmentConnexionBinding
    private lateinit var viewModel: ConnexionViewModel
    private lateinit var _RecipeViewModel : RecipeViewModel
    private  var nbEtapes: Int = 1
    private  var nbIngredients: Int = 1

    lateinit var adapter:ArrayAdapter<String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Firebase.firestore

        val thiscontext = container!!.getContext()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_connexion, container, false
        )

        _RecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ConnexionViewModel::class.java)


        // populate difficulty
        val spinner: Spinner = binding.difficultySpinner
        val difficulty: Array<out String> = resources.getStringArray(R.array.difficulty_array)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            thiscontext,
            R.array.difficulty_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        val listSteps = mutableListOf<EditText>()
        listSteps.add(binding.stepsET)
        binding.moreStepsButton.setOnClickListener{
            nbEtapes ++
            val editText = EditText(thiscontext)
            val textView = TextView(thiscontext)
            listSteps.add(editText)
            textView.text = "etape "+nbEtapes
            binding.layout.addView(editText,9+(nbEtapes * 2))
            binding.layout.addView(textView,9+(nbEtapes * 2))
        }

        val listIngredientsQuantity = mutableListOf<EditText>()
        val listIngredientsTypes = mutableListOf<Spinner>()
        val listIngredients = mutableListOf<EditText>()

        listIngredientsQuantity.add(binding.ingredientQuantity)
        listIngredientsTypes.add(binding.ingredientSpinner)
        listIngredients.add(binding.ingredientET)

        // populate difficulty
        val typeStrings: Array<out String> = resources.getStringArray(R.array.type_array)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            thiscontext,
            R.array.type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.ingredientSpinner.adapter = adapter
        }
        binding.addIngredientButton.setOnClickListener{
            nbIngredients ++

            val layout = LinearLayout(thiscontext)
            val ingredient = EditText(thiscontext)
            val quantity = EditText(thiscontext)
            val type = Spinner(thiscontext)
            val textView = TextView(thiscontext)

            ArrayAdapter.createFromResource(
                thiscontext,
                R.array.type_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                type.adapter = adapter
            }


            layout.orientation =LinearLayout.HORIZONTAL
            layout.addView(quantity)
            layout.addView(type)
            layout.addView(ingredient)
            textView.text = "Ingrédient "+nbIngredients
            binding.layout.addView(layout,10+(nbEtapes * 2)+(nbIngredients * 2))
            binding.layout.addView(textView,10+(nbEtapes * 2)+(nbIngredients * 2))

            listIngredientsQuantity.add(quantity)
            listIngredientsTypes.add(type)
            listIngredients.add(ingredient)

           layout.layoutParams =  LinearLayout.LayoutParams(
                   LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f)

            quantity.layoutParams =  LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f)
            quantity.hint = "quantité"
            type.layoutParams =  LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f)
            ingredient.layoutParams =  LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f)
            ingredient.hint = "ingrédient"
        }

       binding.registerButton.setOnClickListener{
           val liststepsString = mutableListOf<String>()
           listSteps.forEach{
               liststepsString.add(it.text.toString())
           }
           val listIngredientsString = mutableListOf<String>()
           for (i in 0..nbIngredients-1) {
               var quantity = listIngredientsQuantity.get(i).text.toString()
               var type = typeStrings.get(listIngredientsTypes.get(i).selectedItemPosition)
               var ingredient = listIngredients.get(i).text.toString()
               listIngredientsString.add(quantity+" "+type+" "+ingredient)
           }
           val recette = hashMapOf(
               "titre" to binding.titreET.text.toString(),
               "prepaduration" to binding.prepatimeET.text.toString(),
               "time" to binding.timeET.text.toString(),
               "difficulty" to difficulty.get(binding.difficultySpinner.selectedItemPosition),
               "people" to binding.nbpersoET.text.toString(),
               "steps" to liststepsString,
               "ingredients" to listIngredientsString
           )
           db.collection("recette")
               .add(recette)
               .addOnSuccessListener { documentReference ->
                   Toast.makeText(activity, getString(R.string.added_email), Toast.LENGTH_SHORT).show()
               }
               .addOnFailureListener { e ->
                   Log.w(TAG, "Error adding document", e)
               }

       }

        return binding.root
    }

}