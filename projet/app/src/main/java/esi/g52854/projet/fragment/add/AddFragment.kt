@file:Suppress("PrivatePropertyName")

package esi.g52854.projet.fragment.add

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentAddBinding
import java.io.IOException


class AddFragment : Fragment() {

    private lateinit var viewModel: AddViewModel

    private lateinit var listSteps: MutableList<EditText>
    private lateinit var listIngredients: MutableList<EditText>
    private lateinit var listIngredientsTypes: MutableList<Spinner>
    private lateinit var listIngredientsQuantity: MutableList<EditText>
    private lateinit var binding: FragmentAddBinding
    private var nbEtapes: Int = 1
    private var nbIngredients: Int = 1
    private lateinit var thiscontext:Context

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)

        thiscontext = container!!.context
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add, container, false)

        if(viewModel.imageId != "default.jpg"){
            try {
                val bitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, viewModel.filePath)
                binding.image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        createDifficultySpinner()
        createTypeSpinner()
        returnAction()

        listSteps = mutableListOf()
        listIngredientsQuantity = mutableListOf()
        listIngredientsTypes = mutableListOf()
        listIngredients = mutableListOf()

        listSteps
        listIngredientsQuantity.add(binding.ingredientQuantity)
        listIngredientsTypes.add(binding.ingredientSpinner)
        listIngredients.add(binding.ingredientET)

        binding.addStepsButton.setOnClickListener{
            addSteps(listSteps)
        }
        binding.addIngredientButton.setOnClickListener{
            addIngredient()
        }

       binding.registerButton.setOnClickListener {
           valid()
       }
        binding.imageButton.setOnClickListener{
            viewModel.chooseImage(this)
        }
        return binding.root
    }

    private fun valid(){
        val difficultys: Array<out String> = resources.getStringArray(R.array.difficulty_array)
        val difficulty = difficultys[binding.difficultySpinner.selectedItemPosition]
        val typeStrings: Array<out String> = resources.getStringArray(R.array.type_array)
        val titre:String = binding.titreET.text.toString()
        val prepaTime = viewModel.time(binding.prepatimeET.text.toString())
        val time = viewModel.time(binding.timeET.text.toString())
        val people = binding.nbpersoET.text.toString()

        if(viewModel.saveRecipe(difficulty,typeStrings,titre,prepaTime,time,people,
                        listSteps,nbIngredients,listIngredients,listIngredientsTypes,
                        listIngredientsQuantity)){
            Toast.makeText(activity, getString(R.string.added_recette), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.listFragment)
        }else{
            wrongValue()
        }

    }
    private fun wrongValue() {
            AlertDialog.Builder(context)
                    .setTitle("impossible d'ajouter la recette")
                    .setMessage("il reste des champs vide ")
                    .setPositiveButton(R.string.oui, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
    }
    private fun returnAction() {
        requireActivity().onBackPressedDispatcher.addCallback(this){
            AlertDialog.Builder(context)
                    .setTitle("Supprimer la recette")
                    .setMessage("voulez vous vraiment retourner en arriére ?")
                    .setPositiveButton(R.string.oui, { dialog, which ->
                        findNavController().navigate(R.id.listFragment)
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }
    }

    private fun createTypeSpinner() {
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
    }

    private fun addSteps(listSteps: MutableList<EditText>) {
        nbEtapes ++
        val editText = EditText(thiscontext)
        val textView = TextView(thiscontext)
        listSteps.add(editText)
        textView.text = getString(R.string.etapeNum, nbEtapes)
        binding.layout.addView(editText, 9 + (nbEtapes * 2))
        binding.layout.addView(textView, 9 + (nbEtapes * 2))
    }

    private fun createDifficultySpinner() {
        // populate difficulty
        val spinner: Spinner = binding.difficultySpinner

        ArrayAdapter.createFromResource(
                thiscontext,
                R.array.difficulty_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }


    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == viewModel.imageRequest && resultCode == RESULT_OK
                && data != null && data.data != null
        ) {
            viewModel.filePath = data.data!!
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context?.contentResolver, viewModel.filePath)
                binding.image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun addIngredient(){
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
        binding.layout.addView(layout, 10 + (nbEtapes * 2) + (nbIngredients * 2))
        binding.layout.addView(textView, 10 + (nbEtapes * 2) + (nbIngredients * 2))

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

}