package ru.hse.meditation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.hse.meditation.databinding.ActivityAboutBinding

class AboutActivity : ActionWithBackButton() {
    private val viewModel: AboutViewModel by viewModels()
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.complexTask()
            binding.progressBar.visibility = View.INVISIBLE
            binding.textView11.visibility = View.VISIBLE
        }
    }
}
