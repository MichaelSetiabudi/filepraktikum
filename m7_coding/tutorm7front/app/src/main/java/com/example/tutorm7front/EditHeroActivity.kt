package com.example.tutorm7front

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tutorm7front.databinding.ActivityEditHeroBinding
import com.example.tutorm7front.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditHeroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditHeroBinding
    private lateinit var hero: HeroEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hero = intent.getSerializableExtra("hero") as HeroEntity

        setupForm()
        setupSaveButton()
    }

    private fun setupForm() {
        binding.editTextName.setText(hero.name)
        binding.editTextDescription.setText(hero.description)
        binding.editTextDifficulty.setText(hero.difficulty)
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val updatedHero = hero.copy(
                name = binding.editTextName.text.toString(),
                description = binding.editTextDescription.text.toString(),
                difficulty = binding.editTextDifficulty.text.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    RetrofitInstance.instance.updateHero(hero.id, updatedHero)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditHeroActivity, "Hero updated", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditHeroActivity, "Failed to update hero", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
