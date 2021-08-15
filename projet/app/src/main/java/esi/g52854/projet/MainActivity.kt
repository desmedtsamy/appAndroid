package esi.g52854.projet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import esi.g52854.projet.databinding.ActivityMainBinding
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var _user: String = "0"
    var recette : Recette? = null
    var user: String
        get() = _user

        set(newVal){

            _user = newVal
            val preferences: SharedPreferences =
                    applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("user", user)
            editor.apply()
        }
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        val preferences: SharedPreferences = applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        user = preferences.getString("user", 0.toString()).toString()

        if(!isOnline()){
            this.findNavController(R.id.myNavHostFragment).navigate(R.id.fragment_offline)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)

        return true
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