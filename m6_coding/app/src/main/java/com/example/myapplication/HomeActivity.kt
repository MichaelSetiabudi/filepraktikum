package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    lateinit var tvUserInfo: TextView
    lateinit var btnLogout: Button
    lateinit var btnAddFriend: ImageView
    lateinit var rvFriends: RecyclerView
    lateinit var adapter: FriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvUserInfo = findViewById(R.id.tvUserInfo)
        btnLogout = findViewById(R.id.btnLogout)
        btnAddFriend = findViewById(R.id.btnAddFriend)
        rvFriends = findViewById(R.id.rvFriends)

        tvUserInfo.text = "${UserData.currentUserName}\n(${UserData.currentUserPhone})"

        rvFriends.layoutManager = LinearLayoutManager(this)
        adapter = FriendAdapter(this)
        rvFriends.adapter = adapter

        btnLogout.setOnClickListener {
            UserData.currentUserPhone = ""
            UserData.currentUserName = ""
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnAddFriend.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}