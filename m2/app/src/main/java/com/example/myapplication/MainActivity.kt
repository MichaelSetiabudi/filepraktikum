package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var startGameBtn: Button
    private lateinit var showLeaderBoardBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startGameBtn = findViewById(R.id.startGameBtn)
        showLeaderBoardBtn = findViewById(R.id.showLeaderBoardBtn)

        startGameBtn.setOnClickListener {
            val intent = Intent(this, gameActivity::class.java)
            startActivity(intent)
        }

        showLeaderBoardBtn.setOnClickListener {
            // Handle klik tombol Show LeaderBoard
        }
    }

}