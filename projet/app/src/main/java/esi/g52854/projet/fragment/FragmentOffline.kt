package esi.g52854.projet.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentOfflineBinding
import java.io.IOException

class FragmentOffline : Fragment() {


    private lateinit var binding: FragmentOfflineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("test42","onCreate")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_offline, container, false
        )

        this.binding.swiperefresh.setOnRefreshListener {
            this.binding.swiperefresh.isRefreshing = false
           if(isOnline()){

               findNavController().navigate(R.id.connexionFragment)
           }else{
               Toast.makeText(requireActivity(), "You are offline ", Toast.LENGTH_SHORT).show()
           }

        }

        return this.binding.root
    }

    private fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }
}