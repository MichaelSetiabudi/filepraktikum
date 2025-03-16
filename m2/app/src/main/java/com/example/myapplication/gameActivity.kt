package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class gameActivity : AppCompatActivity() {
    private lateinit var backBtn: Button
    private lateinit var turnText: TextView
    private lateinit var colorGrid: GridLayout
    private lateinit var powerUpBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var cheatBtn: Button

    private var turns = 0
    private val gridSize = 10
    private val buttons = Array(gridSize) { Array<Button?>(gridSize) { null } }

    private val colors = listOf(
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.YELLOW,
        Color.MAGENTA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backBtn = findViewById(R.id.backBtn)
        turnText = findViewById(R.id.turnText)
        colorGrid = findViewById(R.id.colorGrid)
        powerUpBtn = findViewById(R.id.powerUpBtn)
        resetBtn = findViewById(R.id.resetBtn)
        cheatBtn = findViewById(R.id.cheatBtn)

        // Set teks awal
        turnText.text = "Turns: $turns"

        // Setup tombol back
        backBtn.setOnClickListener {
            finish()
        }

        // Setup reset button
        resetBtn.setOnClickListener {
            resetGame()
        }

        // Setup power-up button
        powerUpBtn.setOnClickListener {
            // Implementasi power-up disini
        }

        // Setup cheat button
        cheatBtn.setOnClickListener {
            // Implementasi cheat disini
        }

        // Buat grid 10x10
        createColorGrid()
    }
    private fun createColorGrid() {
        // Hitung ukuran layar untuk menentukan ukuran button
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        // Set ukuran button (80% dari lebar layar dibagi 10)
        val buttonSize = (screenWidth * 0.8 / gridSize).toInt()

        // Buat 10x10 buttons
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val button = Button(this)

                // Set parameter layout
                val params = GridLayout.LayoutParams()
                params.width = buttonSize
                params.height = buttonSize
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                params.setMargins(2, 2, 2, 2)

                // Set warna acak
                val randomColor = colors.random()
                button.setBackgroundColor(randomColor)

                // Hapus teks dan padding
                button.text = ""
                button.setPadding(0, 0, 0, 0)
                button.minimumWidth = 0
                button.minimumHeight = 0

                // Set listener
                button.setOnClickListener {
                    // Handle click
                    changeColor(row, col)
                }

                // Tambahkan button ke grid dan array
                colorGrid.addView(button, params)
                buttons[row][col] = button
            }
        }
    }

    private fun changeColor(row: Int, col: Int) {
        // Implementasi logika perubahan warna
        val button = buttons[row][col]
        button?.let {
            // Ubah ke warna acak berikutnya
            val randomColor = colors.random()
            it.setBackgroundColor(randomColor)

            // Tambah hitungan giliran
            turns++
            turnText.text = "Turns: $turns"
        }
    }

    private fun resetGame() {
        // Reset giliran
        turns = 0
        turnText.text = "Turns: $turns"

        // Reset warna semua button
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                buttons[row][col]?.setBackgroundColor(colors.random())
            }
        }
    }
}