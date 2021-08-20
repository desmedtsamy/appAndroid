package esi.g52854.projet.fragment.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import esi.g52854.projet.MainActivity
import esi.g52854.projet.R
import esi.g52854.projet.Recette
import esi.g52854.projet.databinding.FragmentDetailBindingImpl
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBindingImpl
    private lateinit var viewModel: DetailViewModel
    private lateinit var viewModelFactory: DetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = DetailViewModelFactory((requireActivity() as MainActivity))
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(DetailViewModel::class.java)

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false)
        getData(viewModel.recette)
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

        val timePrepa = TextView(requireContext())
        timePrepa.text = getString(R.string.prepaDur, recette.prepaduration)
        timePrepa.textSize = 21f
        timePrepa.setTextColor(Color.parseColor("#FFFFFF"))
        binding.root.steps.addView(timePrepa)

        val ingredients = TextView(requireContext())
        ingredients.text =  getString(R.string.ingrédients)
        ingredients.setTextColor(Color.parseColor("#FFFFFF"))
        ingredients.textSize = 21f
        binding.root.steps.addView(ingredients)

        ingredientsList.forEach{
            val ingredient = TextView(requireContext())

            ingredient.text = it
            binding.root.steps.addView(ingredient)
        }
        val stepsList = recette.steps

        stepsList.forEach{
            nb++
            val etape = TextView(requireContext())
            val titreEtape = TextView(requireContext())
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