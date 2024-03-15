package com.example.ahyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ahyak.Calendar.CalenderFragment
import com.example.ahyak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fun replaceFragment(fragment: Fragment) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.containers, fragment)
            fragmentTransaction.commit()
        }

        replaceFragment(TodayRecordFragment())

        binding.bottomNavigationview.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_today -> {
                    replaceFragment(TodayRecordFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_statistics -> {
                    replaceFragment(StatisticsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_calender -> {
                    replaceFragment(CalenderFragment())
                    return@setOnNavigationItemSelectedListener true
                }else -> return@setOnNavigationItemSelectedListener false
            }
        }

    }
}