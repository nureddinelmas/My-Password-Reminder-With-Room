package com.nureddinelmas.mypassword

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.nureddinelmas.mypassword.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var passwordsList = ArrayList<Passwords>()
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        val adapter = PasswordAdapter(passwordsList)
        binding.recyclerView.adapter = adapter

        try {
            val database = this.openOrCreateDatabase("Passwords",Context.MODE_PRIVATE, null)

            val cursor = database.rawQuery("SELECT * FROM passwords", null)
            val passnameIx= cursor.getColumnIndex("passname")
            val passIdIx = cursor.getColumnIndex("id")

            while(cursor.moveToNext()){
                val name = cursor.getString(passnameIx)
                val id = cursor.getInt(passIdIx)
                val pass = Passwords(id, name)
                passwordsList.add(pass)
            }
            adapter.notifyDataSetChanged()
            cursor.close()
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding.NewButton.setOnClickListener{
            val intent = Intent(this@MainActivity, PasswordActivity::class.java)
            startActivity(intent)

        }

    }
}