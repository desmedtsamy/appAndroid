package esi.g52854.projet.fragment.connexion

import android.util.Patterns
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executor


class ConnexionViewModel (): ViewModel() {

    private var executor: Executor? = null
    // The list of email
    private val _list = MutableLiveData< MutableList<String?> >()
    val list: LiveData< MutableList<String?> >
        get() = _list

    // The list of id
    private val _listOfId = MutableLiveData< MutableList<Long?> >()
    val listOfId: LiveData< MutableList<Long?> >
        get() = _listOfId

    // The list of email
    private var _adapter = MutableLiveData< ArrayAdapter<String?> >()
    val adapter: LiveData<ArrayAdapter<String?>>
        get() = _adapter

    init{
        _list.value =  mutableListOf()
        _listOfId.value =  mutableListOf()
    }



}