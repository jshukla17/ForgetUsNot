package com.jshukla.forgetusnot

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jshukla.forgetusnot.databinding.ActivityCatchupBinding
import com.jshukla.forgetusnot.databinding.ActivityLovedOneBinding
import com.jshukla.forgetusnot.ui.theme.ForgetUsNotTheme

class LovedOneActivity : ComponentActivity() {
    private lateinit var binding: ActivityLovedOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLovedOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgUri = intent.getStringExtra("uri")
        imgUri?.let {
            val photoUri = Uri.parse(imgUri)
            binding.imageView.setImageURI(photoUri)
        } ?: run {
            binding.imageView.setImageResource(R.mipmap.ic_launcher_foreground)
        }
        val name = intent.getStringExtra("name")
        binding.PersonName.text = name
        binding.SubmitButton.setOnClickListener{
            
        }
    }
}