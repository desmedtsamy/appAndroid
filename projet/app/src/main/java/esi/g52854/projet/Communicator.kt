package esi.g52854.projet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Communicator : ViewModel(){

    val message = MutableLiveData<Any>()

    fun setMsgCommunicator(msg:Recette){
        message.setValue(msg)
    }
}