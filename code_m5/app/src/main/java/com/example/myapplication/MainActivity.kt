package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: GameViewModel
    private lateinit var gameGrid: GridLayout
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    private lateinit var btnCheat: Button
    private lateinit var tvTroopsCount: TextView
    private lateinit var tvRoundInfo: TextView
    private lateinit var gridCells: Array<Array<FrameLayout?>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        gameGrid = findViewById(R.id.gameGrid)
        btnLeft = findViewById(R.id.btnLeft)
        btnRight = findViewById(R.id.btnRight)
        btnCheat = findViewById(R.id.btnCheat)
        tvTroopsCount = findViewById(R.id.tvTroopsCount)
        tvRoundInfo = findViewById(R.id.tvRoundInfo)

        initGridCells()
        setupButtons()
        observeViewModel()
        handleIncomingIntent(intent)
    }

    private fun initGridCells() {
        gridCells = Array(7) { row ->
            Array(2) { col ->
                val index = row * 2 + col
                if (index < gameGrid.childCount) {
                    gameGrid.getChildAt(index) as? FrameLayout
                } else {
                    null
                }
            }
        }
    }

    private fun setupButtons() {
        btnLeft.setOnClickListener {
            viewModel.moveLeft()
        }

        btnRight.setOnClickListener {
            viewModel.moveRight()
        }

        btnCheat.setOnClickListener {
            Toast.makeText(this, "Cheat button clicked!", Toast.LENGTH_SHORT).show()
            viewModel.toggleCheat()
        }
    }

    private fun observeViewModel() {
        viewModel.playerPos.observe(this) { position ->
            updatePlayerPosition(position)
        }

        viewModel.troops.observe(this) { troops ->
            tvTroopsCount.text = "Troops: $troops"
            updatePlayerTroops(troops)
        }

        viewModel.gates.observe(this) { grid ->
            updateGateDisplay(grid)
        }

        viewModel.gameState.observe(this) { state ->
            if (state == GameState.BOSS_FIGHT) {
                navigateToBossFight()
            }
        }

        viewModel.wave.observe(this) { wave ->
            updateRoundInfo()
        }

        viewModel.round.observe(this) { round ->
            updateRoundInfo()
        }
    }

    private fun updateRoundInfo() {
        val round = viewModel.round.value ?: 1
        val wave = viewModel.wave.value ?: 0
        val maxWaves = viewModel.getMaxWavesForRound()
        tvRoundInfo.text = "Round $round - Wave $wave/$maxWaves"
    }

    private fun updatePlayerPosition(position: Int) {
        clearCell(6, 0)
        clearCell(6, 1)

        updatePlayerTroops(viewModel.troops.value ?: 10, position)
    }

    private fun updatePlayerTroops(troops: Int, position: Int = viewModel.playerPos.value ?: 0) {
        val cell = gridCells[6][position]
        cell?.let {
            it.setBackgroundColor(getColor(android.R.color.darker_gray))

            val textView = findOrCreateTextView(it, "playerText")
            textView.text = troops.toString()
            textView.textSize = 24f
            textView.setTextColor(getColor(android.R.color.black))
        }
    }

    private fun updateGateDisplay(grid: Array<Array<Gate?>>) {
        for (row in 0 until 6) {
            for (col in 0 until 2) {
                clearCell(row, col)
            }
        }

        for (row in 0 until 6) {
            for (col in 0 until 2) {
                val gate = grid[row][col]
                if (gate != null) {
                    displayGate(row, col, gate)
                }
            }
        }
    }

    private fun displayGate(row: Int, col: Int, gate: Gate) {
        val cell = gridCells[row][col]
        cell?.let {
            val textView = findOrCreateTextView(it, "gateText")

            val text = when (gate.operator) {
                "+" -> "+${gate.value}"
                "-" -> "-${gate.value}"
                "×" -> "×${gate.value}"
                "÷" -> "÷${gate.value}"
                else -> gate.value.toString()
            }

            textView.text = text
            textView.textSize = 24f

            val color = when (gate.operator) {
                "+" -> getColor(android.R.color.holo_green_dark)
                "-" -> getColor(android.R.color.holo_red_dark)
                "×" -> getColor(android.R.color.holo_orange_dark)
                "÷" -> getColor(android.R.color.holo_blue_dark)
                else -> getColor(android.R.color.black)
            }
            textView.setTextColor(color)
        }
    }

    private fun findOrCreateTextView(parent: FrameLayout, tag: String): TextView {
        return parent.findViewWithTag(tag) as? TextView ?: TextView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER
            this.tag = tag
            parent.addView(this)
        }
    }

    private fun clearCell(row: Int, col: Int) {
        gridCells[row][col]?.removeAllViews()

        if (row != 6) {
            gridCells[row][col]?.setBackgroundColor(getColor(android.R.color.white))
        }
    }

    private fun navigateToBossFight() {
        val intent = Intent(this, BossFightActivity::class.java).apply {
            putExtra("TROOPS_COUNT", viewModel.troops.value ?: 0)
            putExtra("BOSS_HP", viewModel.bossHealth.value ?: 500)
            putExtra("CURRENT_ROUND", viewModel.round.value ?: 1)
        }
        startActivity(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent == null) return

        val continueGame = intent.getBooleanExtra("CONTINUE_GAME", false)
        if (continueGame) {
            val remainingTroops = intent.getIntExtra("REMAINING_TROOPS", 10)
            val currentRound = intent.getIntExtra("CURRENT_ROUND", 1)

            viewModel.updateTroopsAfterBossFight(remainingTroops)

            if (currentRound < 3) {
                val nextRound = currentRound + 1
                viewModel.continueToNextRound(nextRound)
            } else {
                viewModel.resetGame()
            }

            intent.removeExtra("CONTINUE_GAME")
            intent.removeExtra("REMAINING_TROOPS")
        } else {
            viewModel.startGame()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }
}