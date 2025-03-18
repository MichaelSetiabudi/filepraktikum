package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    private lateinit var backBtn: Button

    private lateinit var turnText: TextView
    private lateinit var colorGrid: GridLayout
    private lateinit var powerUpBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var cheatBtn: Button

    private var turns = 0
    private val gridSize = 10
    private val buttons = Array(gridSize) { Array<Button?>(gridSize) { null } }
    private val buttonColors = Array(gridSize) { IntArray(gridSize) }
    private var playerName = ""
    private var isPowerUpActive = false
    private var isGameOver = false

    private val colors = listOf(
        Color.RED,
        Color.GREEN,
        Color.YELLOW,
        Color.MAGENTA
    )

    private val inactiveColor = Color.GRAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"

        backBtn = findViewById(R.id.backBtn)
        turnText = findViewById(R.id.turnText)
        colorGrid = findViewById(R.id.colorGrid)
        powerUpBtn = findViewById(R.id.powerUpBtn)
        resetBtn = findViewById(R.id.resetBtn)
        cheatBtn = findViewById(R.id.cheatBtn)
        turnText.text = "Turns: $turns"

        backBtn.setOnClickListener {
            finish()
        }

        resetBtn.setOnClickListener {
            resetGame()
        }
        powerUpBtn.isEnabled = false
        powerUpBtn.setBackgroundColor(Color.GRAY)
        powerUpBtn.setOnClickListener {
            if (powerUpBtn.isEnabled && !isPowerUpActive) {
                isPowerUpActive = true
                Toast.makeText(this, "Power-up activated! Click any block to clear its row and column.", Toast.LENGTH_SHORT).show()
            }
        }
        cheatBtn.setOnClickListener {
            activatePowerUp()
            Toast.makeText(this, "Cheat activated! Power-up is now available.", Toast.LENGTH_SHORT).show()
        }
        createColorGrid()
    }

    private fun createColorGrid() {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val buttonSize = (screenWidth * 0.8 / gridSize).toInt()
        colorGrid.removeAllViews()
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val button = Button(this)

                val params = GridLayout.LayoutParams()
                params.width = buttonSize
                params.height = buttonSize
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                params.setMargins(2, 2, 2, 2)

                val randomColor = colors.random()
                button.setBackgroundColor(randomColor)
                buttonColors[row][col] = randomColor

                button.text = ""
                button.setPadding(0, 0, 0, 0)
                button.minimumWidth = 0
                button.minimumHeight = 0

                button.setOnClickListener {
                    if (!isGameOver) {
                        handleButtonClick(row, col)
                    }
                }
                colorGrid.addView(button, params)
                buttons[row][col] = button
            }
        }
    }

    private fun handleButtonClick(row: Int, col: Int) {
        val clickedColor = buttonColors[row][col]
        if (clickedColor == inactiveColor) {
            return
        }

        if (isPowerUpActive) {
            clearRowAndColumn(row, col)

            isPowerUpActive = false
            powerUpBtn.setBackgroundColor(Color.GRAY)
            powerUpBtn.isEnabled = false

            turns++
            turnText.text = "Turns: $turns"

            checkGameOver()
        } else {
            val clearCount = clearConnectedBlocks(row, col, clickedColor, mutableSetOf())

            if (clearCount >= 4) {
                // Only make power-up available, don't activate it yet
                powerUpBtn.setBackgroundColor(Color.rgb(255, 165, 0)) // Orange color
                powerUpBtn.isEnabled = true
                Toast.makeText(this, "Power-up available!", Toast.LENGTH_SHORT).show()
            }

            turns++
            turnText.text = "Turns: $turns"

            checkGameOver()
        }
    }
    private fun clearRowAndColumn(row: Int, col: Int) {
        for (c in 0 until gridSize) {
            buttons[row][c]?.setBackgroundColor(inactiveColor)
            buttonColors[row][c] = inactiveColor
        }
        for (r in 0 until gridSize) {
            buttons[r][col]?.setBackgroundColor(inactiveColor)
            buttonColors[r][col] = inactiveColor
        }
        Toast.makeText(this, "Row and column cleared!", Toast.LENGTH_SHORT).show()
    }

    private fun clearConnectedBlocks(row: Int, col: Int, targetColor: Int, visited: MutableSet<Pair<Int, Int>>): Int {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            return 0
        }
        val position = Pair(row, col)

        if (position in visited || buttonColors[row][col] != targetColor) {
            return 0
        }
        visited.add(position)
        buttons[row][col]?.setBackgroundColor(inactiveColor)
        buttonColors[row][col] = inactiveColor
        val count = 1 +
                clearConnectedBlocks(row - 1, col, targetColor, visited) +
                clearConnectedBlocks(row + 1, col, targetColor, visited) +
                clearConnectedBlocks(row, col - 1, targetColor, visited) +
                clearConnectedBlocks(row, col + 1, targetColor, visited)

        return count
    }

    private fun activatePowerUp() {
        powerUpBtn.setBackgroundColor(Color.rgb(255, 165, 0))
        powerUpBtn.isEnabled = true
        Toast.makeText(this, "Power-up available!", Toast.LENGTH_SHORT).show()
    }


    private fun checkGameOver() {
        var allInactive = true
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                if (buttonColors[row][col] != inactiveColor) {
                    allInactive = false
                    break
                }
            }
            if (!allInactive) break
        }

        if (allInactive) {
            isGameOver = true
            Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show()

            // Navigate to LeaderboardActivity instead of MainActivity
            val intent = Intent(this, LeaderboardActivity::class.java)
            // Pass the player's name and turns to the leaderboard
            intent.putExtra("PLAYER_NAME", playerName)
            intent.putExtra("PLAYER_TURNS", turns)
            startActivity(intent)
            finish()
        }
    }

    private fun resetGame() {
        turns = 0
        turnText.text = "Turns: $turns"
        isGameOver = false
        isPowerUpActive = false
        powerUpBtn.setBackgroundColor(Color.GRAY)
        powerUpBtn.isEnabled = false

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val randomColor = colors.random()
                buttons[row][col]?.setBackgroundColor(randomColor)
                buttonColors[row][col] = randomColor
            }
        }
    }
}