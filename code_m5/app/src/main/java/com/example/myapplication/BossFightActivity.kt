package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BossFightActivity : AppCompatActivity() {

    private lateinit var tvBossHP: TextView
    private lateinit var tvTroops: TextView
    private lateinit var tvResult: TextView
    private lateinit var btnRestart: Button
    private lateinit var btnContinue: Button

    private var troopsCount = 0
    private var bossHP = 500
    private var currentRound = 1
    private var countdownBossHP = 0
    private var countdownTroops = 0
    private var remainingTroops = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.boss_fight)

        tvBossHP = findViewById(R.id.tvBossHP)
        tvTroops = findViewById(R.id.tvTroops)
        tvResult = findViewById(R.id.tvResult)
        btnRestart = findViewById(R.id.btnRestart)
        btnContinue = findViewById(R.id.btnContinue)

        troopsCount = intent.getIntExtra("TROOPS_COUNT", 0)
        bossHP = intent.getIntExtra("BOSS_HP", 500)
        currentRound = intent.getIntExtra("CURRENT_ROUND", 1)

        countdownBossHP = bossHP
        countdownTroops = troopsCount
        tvBossHP.text = "Boss HP: $bossHP"
        tvTroops.text = "Troops: $troopsCount"

        title = "Boss Fight - Round $currentRound"

        btnRestart.visibility = View.GONE
        btnContinue.visibility = View.GONE
        tvResult.visibility = View.GONE

        btnRestart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnContinue.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("CONTINUE_GAME", true)
            intent.putExtra("REMAINING_TROOPS", remainingTroops)
            intent.putExtra("CURRENT_ROUND", currentRound)
            startActivity(intent)
            finish()
        }

        startBattle()
    }

    private fun startBattle() {
        object : CountDownTimer(5000, 50) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = 1 - (millisUntilFinished / 5000.0)

                if (troopsCount > bossHP) {
                    countdownBossHP = (bossHP - progress * bossHP).toInt().coerceAtLeast(0)
                    countdownTroops = (troopsCount - progress * bossHP).toInt().coerceAtLeast(1)
                } else {
                    countdownTroops = (troopsCount - progress * troopsCount).toInt().coerceAtLeast(0)
                    countdownBossHP = (bossHP - progress * troopsCount).toInt().coerceAtLeast(0)
                }

                tvBossHP.text = "Boss HP: $countdownBossHP"
                tvTroops.text = "Troops: $countdownTroops"

                if (countdownBossHP == 0 || countdownTroops == 0) {
                    this.cancel()
                    onFinish()
                }
            }

            override fun onFinish() {
                if (troopsCount > bossHP) {
                    countdownBossHP = 0
                    remainingTroops = troopsCount - bossHP
                    countdownTroops = remainingTroops
                } else {
                    countdownTroops = 0
                    countdownBossHP = bossHP - troopsCount
                    remainingTroops = 0
                }

                tvBossHP.text = "Boss HP: $countdownBossHP"
                tvTroops.text = "Troops: $countdownTroops"

                showBattleResult()
            }
        }.start()
    }

    private fun showBattleResult() {
        tvResult.visibility = View.VISIBLE

        if (troopsCount > bossHP) {
            tvResult.text = "YOU WIN!"
            tvResult.setTextColor(getColor(android.R.color.holo_green_dark))

            if (currentRound < 3) {
                btnContinue.visibility = View.VISIBLE
            } else {
                btnRestart.visibility = View.VISIBLE
            }
        } else {
            // Player loses
            tvResult.text = "YOU LOSE!"
            tvResult.setTextColor(getColor(android.R.color.holo_red_dark))
            btnRestart.visibility = View.VISIBLE
        }
    }
}