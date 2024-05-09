package com.example.preatec

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.preatec.databinding.ActivityWmenuBinding
import com.google.android.material.navigation.NavigationView

class Wmenu : AppCompatActivity() {
    private val binding: ActivityWmenuBinding by lazy {
        ActivityWmenuBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.mtToolbar)

        val toggle = ActionBarDrawerToggle(this,
            binding.drawerLayout,
            binding.mtToolbar,
            R.string.close_drawer,
            R.string.open_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.nvMenuLateral.setNavigationItemSelectedListener {
            onNavigationItemSelected(it)
            true
        }
    }

    override fun onBackPressed(){
        binding.drawerLayout.run {
            if (isDrawerOpen(GravityCompat.START))
                closeDrawer(GravityCompat.START)
            else
                super.onBackPressed()
        }
    }

    private fun onNavigationItemSelected(item: MenuItem){
        simpleAction(item)
    }

    private fun simpleAction(item: MenuItem) {
        var message = ""
        when(item.itemId){
            R.id.menu_call-> {
                binding.mtToolbar.setTitle(R.string.menu_call)
                message = "Menu Call"
                loadFragment(CallFragment.newInstance(android.R.color.holo_blue_light))
            }
            R.id.menu_camera-> {
                binding.mtToolbar.setTitle(R.string.menu_camera)
                message = "Menu camera"
                loadFragment(CallFragment.newInstance(android.R.color.holo_green_light))
            }
            R.id.menu_gallery-> {
                binding.mtToolbar.setTitle(R.string.menu_gallery)
                message = "Menu Gallery"
                loadFragment(CallFragment.newInstance(android.R.color.holo_orange_light))
            }
            R.id.menu_send-> {
                binding.mtToolbar.setTitle(R.string.menu_send)
                message = "Menu Send"
                loadFragment(CallFragment.newInstance(android.R.color.holo_red_light))
            }
            R.id.menu_share-> {
                binding.mtToolbar.setTitle(R.string.menu_share)
                message = "Menu Share"
                loadFragment(CallFragment.newInstance(android.R.color.darker_gray))
            }
            R.id.menu_agenda-> {
                binding.mtToolbar.setTitle(R.string.menu_agenda)
                message = "Menu Agenda"
                loadFragment(CallFragment.newInstance(android.R.color.holo_red_dark))
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun loadFragment(fragment: Fragment?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment!!)
            //.addToBackStack("name")
            .commit()
    }
}