package com.example.superphoto.domain.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.superphoto.databinding.ActivityMainBinding
import com.example.superphoto.domain.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

    }
}