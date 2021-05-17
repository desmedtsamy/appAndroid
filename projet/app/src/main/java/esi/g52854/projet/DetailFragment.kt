package esi.g52854.projet

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import esi.g52854.projet.database.recipe.RecipeViewModel
import esi.g52854.projet.databinding.FragmentDetailBindingImpl
import esi.g52854.projet.databinding.FragmentListBinding
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : Fragment() {


    private lateinit var thiscontext: Context
    private lateinit var binding: FragmentDetailBindingImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thiscontext  = container!!.getContext()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        val model= ViewModelProviders.of(requireActivity()).get(Communicator::class.java)
        model.message.observe(this.viewLifecycleOwner, object : Observer<Any> {
            override fun onChanged(o: Any?) {

                getData(o!! as Recette)
            }})



        return binding.root

    }
    fun getData(recette:Recette){
        var nb = 0

                    binding.root.titre.text = recette.titre
                    binding.root.nbperso.text = recette.people+" personnes"
                    binding.root.difficulty.text = recette.difficulty
                    var stepsList =recette.steps
                    stepsList.forEach{
                        nb++
                        val etape = TextView(thiscontext)
                        val titreEtape = TextView(thiscontext)
                        etape.text = it
                        titreEtape.text = " étape "+nb
                        titreEtape.textSize = 21f
                        titreEtape.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.root.steps.addView(titreEtape)
                        binding.root.steps.addView(etape)
                    }


    }




}