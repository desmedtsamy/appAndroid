package esi.g52854.projet.fragment.detail

import androidx.lifecycle.ViewModel
import esi.g52854.projet.MainActivity
import esi.g52854.projet.Recette


class DetailViewModel(mainActivity: MainActivity) : ViewModel() {
 val recette:Recette = mainActivity.recette!!

}
