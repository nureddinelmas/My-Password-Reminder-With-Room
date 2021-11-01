package com.nureddinelmas.myPasswordReminder.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.nureddinelmas.myPasswordReminder.adapter.AccountAdapter
import com.nureddinelmas.myPasswordReminder.model.Accounts
import com.nureddinelmas.myPasswordReminder.roomdb.AccountDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myPasswordReminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val compositeDisposable = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        val db = Room.databaseBuilder(applicationContext, AccountDatabase::class.java, "Accounts")
            .fallbackToDestructiveMigration()
            .build()
        val accountDao = db.accountsDao()

        compositeDisposable?.add(
            accountDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

        binding.addCard.setOnClickListener{
            val intent = Intent(this@MainActivity, PasswordActivity::class.java)
            startActivity(intent)

        }

    }

    private fun handleResponse(accountList : List<Accounts>){
        if (accountList.isNotEmpty()){
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = AccountAdapter(accountList)
            binding.recyclerView.adapter = adapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }
}