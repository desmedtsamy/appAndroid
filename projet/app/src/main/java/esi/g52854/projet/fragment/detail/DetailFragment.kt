package esi.g52854.projet.fragment.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import esi.g52854.projet.Communicator
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import esi.g52854.projet.databinding.FragmentDetailBindingImpl
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.io.File

class DetailFragment : Fragment() {


    private lateinit var thiscontext: Context
    private lateinit var binding: FragmentDetailBindingImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thiscontext  = container!!.context
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        val model= ViewModelProviders.of(requireActivity()).get(Communicator::class.java)
        model.message.observe(this.viewLifecycleOwner, Observer<Any> { o -> getData(o!! as Recette) })



        return binding.root

    }
    private fun getData(recette: Recette){
        var nb = 0
        setImage(recette)
        binding.root.titre.text = recette.titre
        binding.root.nbperso.text = getString(R.string.persoNb,recette.people)
        binding.root.difficulty.text = recette.difficulty
        binding.root.time.text = recette.time
        val ingredientsList = recette.ingredients

        val timePrepa = TextView(thiscontext)
        timePrepa.text = getString(R.string.prepaDur, recette.prepaduration)
        timePrepa.textSize = 21f
        timePrepa.setTextColor(Color.parseColor("#FFFFFF"))
        binding.root.steps.addView(timePrepa)

        val ingredients = TextView(thiscontext)
        ingredients.text =  getString(R.string.ingrédients)
        ingredients.setTextColor(Color.parseColor("#FFFFFF"))
        ingredients.textSize = 21f
        binding.root.steps.addView(ingredients)

        ingredientsList.forEach{
            val ingredient = TextView(thiscontext)

            ingredient.text = it
            binding.root.steps.addView(ingredient)
        }
        val stepsList = recette.steps

        stepsList.forEach{
            nb++
            val etape = TextView(thiscontext)
            val titreEtape = TextView(thiscontext)
            etape.text = it
            titreEtape.text = getString(R.string.etapeNum, nb)
                titreEtape.textSize = 21f
                titreEtape.setTextColor(Color.parseColor("#FFFFFF"))
                binding.root.steps.addView(titreEtape)
                binding.root.steps.addView(etape)
        }

    }
    private fun setImage(recette:Recette){

            val bitmap : Bitmap = BitmapFactory.decodeFile(recette.imagePath)
            binding.root.image_recipe.setImageBitmap(bitmap)
    }
}