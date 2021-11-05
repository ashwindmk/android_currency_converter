package com.ashwin.android.currencyconverter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.ashwin.android.currencyconverter.databinding.ActivityMainBinding
import com.ashwin.android.currencyconverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertButton.setOnClickListener {
            viewModel.convert(
                binding.amountIntputEditText.text.toString(),
                binding.fromSpinner.selectedItem.toString(),
                binding.toSpinner.selectedItem.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is MainViewModel.CurrencyEvent.Initial -> {
                        binding.convertProgressBar.isVisible = false
                        binding.resultTextView.isVisible = false
                    }
                    is MainViewModel.CurrencyEvent.Loading -> {
                        binding.convertProgressBar.isVisible = true
                        binding.resultTextView.isVisible = false
                    }
                    is MainViewModel.CurrencyEvent.Success -> {
                        binding.apply {
                            convertProgressBar.isVisible = false
                            resultTextView.isVisible = true
                            resultTextView.setTextColor(Color.BLACK)
                            resultTextView.text = event.resultText
                        }
                    }
                    is MainViewModel.CurrencyEvent.Failure -> {
                        binding.apply {
                            convertProgressBar.isVisible = false
                            resultTextView.isVisible = true
                            resultTextView.setTextColor(Color.RED)
                            resultTextView.text = event.errorText
                        }
                    }
                }
            }
        }
    }
}
