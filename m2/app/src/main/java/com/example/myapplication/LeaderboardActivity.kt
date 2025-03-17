// In LeaderboardActivity.kt
package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var backLeaderboardBtn: Button
    private lateinit var leaderboardEntries: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backLeaderboardBtn = findViewById(R.id.backLeaderboardBtn)
        leaderboardEntries = listOf(
            findViewById(R.id.leaderboardEntry1),
            findViewById(R.id.leaderboardEntry2),
            findViewById(R.id.leaderboardEntry3),
            findViewById(R.id.leaderboardEntry4),
            findViewById(R.id.leaderboardEntry5)
        )

        val playerName = intent.getStringExtra("PLAYER_NAME") ?: ""
        val playerTurns = intent.getIntExtra("PLAYER_TURNS", -1)

        if (playerName.isNotEmpty() && playerTurns > 0) {
            savePlayerScore(playerName, playerTurns)
        }

        displayLeaderboard()

        backLeaderboardBtn.setOnClickListener {
            finish()
        }
    }

    private fun savePlayerScore(name: String, turns: Int) {
        val sharedPref = getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE)

        val currentScores = mutableListOf<Pair<String, Int>>()

        for (i in 0 until 20) {
            val savedName = sharedPref.getString("player_$i", null)
            val savedTurns = sharedPref.getInt("turns_$i", -1)

            if (savedName != null && savedTurns > 0) {
                currentScores.add(Pair(savedName, savedTurns))
            } else {
                break
            }
        }

        // Add new score
        currentScores.add(Pair(name, turns))

        // Sort by turns (ascending)
        currentScores.sortBy { it.second }

        // Save back (max 20 entries)
        val editor = sharedPref.edit()
        editor.clear() // Clear existing entries

        // Save up to 20 entries
        for (i in currentScores.indices.take(20)) {
            editor.putString("player_$i", currentScores[i].first)
            editor.putInt("turns_$i", currentScores[i].second)
        }

        editor.apply()
    }

    private fun displayLeaderboard() {
        val sharedPref = getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE)

        // Get top 5 scores
        val scores = mutableListOf<Pair<String, Int>>()

        for (i in 0 until 20) {
            val name = sharedPref.getString("player_$i", null)
            val turns = sharedPref.getInt("turns_$i", -1)

            if (name != null && turns > 0) {
                scores.add(Pair(name, turns))
            } else {
                break
            }
        }

        // Display scores (top 5)
        for (i in 0 until minOf(5, scores.size)) {
            leaderboardEntries[i].text = "${i+1}. ${scores[i].first} - ${scores[i].second} turns"
        }
    }
}