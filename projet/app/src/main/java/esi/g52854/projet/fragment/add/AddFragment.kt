@file:Suppress("PrivatePropertyName")

package esi.g52854.projet.fragment.add

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentAddBinding
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class AddFragment : Fragment() {
    private var imageId = "default.jpg"
    private lateinit var binding: FragmentAddBinding
    private var nbEtapes: Int = 1
    private var nbIngredients: Int = 1
    //image
    private lateinit var filePath: Uri
    private val ImageRequest = 71
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         db = Firebase.firestore

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
           if(imageId != "default.jpg"){
               uploadImage()
           }
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
           val prepaTime = time(binding.prepatimeET.text.toString().toInt())
           val time = time(binding.timeET.text.toString().toInt())
           val recette = hashMapOf(
               "titre" to binding.titreET.text.toString(),
               "prepaduration" to prepaTime,
               "time" to time,
               "difficulty" to difficulty[binding.difficultySpinner.selectedItemPosition],
               "people" to binding.nbpersoET.text.toString(),
               "steps" to liststepsString,
               "ingredients" to listIngredientsString,
               "imageId" to imageId
           )
           db.collection("recette")
               .add(recette)
               .addOnSuccessListener {
                   Toast.makeText(activity, getString(R.string.added_recette), Toast.LENGTH_SHORT).show()
                   findNavController().navigate(R.id.listFragment)
               }
               .addOnFailureListener { e ->
                   Log.w(TAG, "Error adding document", e)
               }

            }

        //////////////image//////////////
        binding.imageButton.setOnClickListener{
            chooseImage()
        }

        return binding.root
    }
    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ImageRequest)

        imageId = UUID.randomUUID().toString()
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageRequest && resultCode == RESULT_OK && data != null && data.data != null
        ) {
            filePath = data.data!!
            try {
                binding.image.visibility = View.VISIBLE
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                binding.image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage() {
        val storageReference: StorageReference
        val storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        if (filePath.path.isNullOrBlank()) {

            val ref: StorageReference =
                storageReference.child("images/$imageId")
            ref.putFile(filePath)
        }
    }
    fun time(time:Int): String {
        var tmp = time
        var hour = 0
        while(time >= 60){
            hour++
            tmp -= 60
        }
        if(hour == 0){
            return "$time minutes"
        }
        return "$hour h$tmp"

    }

}