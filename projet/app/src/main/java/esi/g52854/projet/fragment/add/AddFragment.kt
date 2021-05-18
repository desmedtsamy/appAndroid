
package esi.g52854.projet.fragment.add

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private  var nbEtapes: Int = 1
    private  var nbIngredients: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Firebase.firestore

        val thiscontext = container!!.context
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add, container, false
        )


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
            textView.text = getString(R.string.etapeNum, nbEtapes)
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
            textView.text = getString(R.string.ingredientNum, nbIngredients)
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
           for (i in 0 until nbIngredients) {
               val quantity = listIngredientsQuantity[i].text.toString()
               val type = typeStrings[listIngredientsTypes[i].selectedItemPosition]
               val ingredient = listIngredients[i].text.toString()
               listIngredientsString.add("$quantity $type $ingredient")
           }
           val prepaTime = (binding.prepatimeET.text.toString().toInt()/60).toString() +" h "+ (binding.prepatimeET.text.toString().toInt()%60).toString()
           val time = (binding.timeET.text.toString().toInt()/60).toString() +" h "+ (binding.timeET.text.toString().toInt()%60).toString()

           val recette = hashMapOf(
               "titre" to binding.titreET.text.toString(),
               "prepaduration" to prepaTime,
               "time" to time,
               "difficulty" to difficulty[binding.difficultySpinner.selectedItemPosition],
               "people" to binding.nbpersoET.text.toString(),
               "steps" to liststepsString,
               "ingredients" to listIngredientsString
           )
           db.collection("recette")
               .add(recette)
               .addOnSuccessListener { documentReference ->
                   Toast.makeText(activity, getString(R.string.added_recette), Toast.LENGTH_SHORT).show()
               }
               .addOnFailureListener { e ->
                   Log.w(TAG, "Error adding document", e)
               }

            }

        return binding.root
    }

}