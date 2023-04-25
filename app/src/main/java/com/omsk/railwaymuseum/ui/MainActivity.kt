package com.omsk.railwaymuseum.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.data.NightModeType

class MainActivity : AppCompatActivity() {

    private val preferenceKey = "theme"
    private lateinit var sharedPreferences: SharedPreferences
    private val nightModesList = arrayOf(
        NightModeType.MODE_NIGHT_NO,
        NightModeType.MODE_NIGHT_YES,
        NightModeType.getDefaultMode()
    )
    //Если в настройки выбора темной темы ничего не загружено,
    // то используется значение темной темы по умолчанию: 1 (светлая)
    private val defNightModeType = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        AppCompatDelegate.setDefaultNightMode(loadNightModeType())
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            for (nightMode in nightModesList) {
                it.add(Menu.NONE, nightMode.value, Menu.NONE, getString(nightMode.title))
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        AppCompatDelegate.setDefaultNightMode(item.itemId)
        saveNightModeType(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun saveNightModeType(nightModeType: Int) {
        val edit = sharedPreferences.edit()
        edit.putInt(preferenceKey, nightModeType)
        edit.apply()
    }

    private fun loadNightModeType(): Int {
        return sharedPreferences.getInt(preferenceKey, defNightModeType)
    }

}