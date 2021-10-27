package com.nureddinelmas.myPasswordReminder.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.nureddinelmas.myPasswordReminder.model.Accounts
import com.nureddinelmas.myPasswordReminder.roomdb.AccountDao
import com.nureddinelmas.myPasswordReminder.roomdb.AccountDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myPasswordReminder.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var db : AccountDatabase
    private lateinit var accountDao: AccountDao

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Room.databaseBuilder(applicationContext, AccountDatabase::class.java, "Accounts")
            //.allowMainThreadQueries()
            .build()
        accountDao = db.accountsDao()
        }


    fun clickedButton(view : View){
        val account = Accounts(binding.textAccount.text.toString(), binding.textUserName.text.toString(), binding.textPassword.text.toString() )


        compositeDisposable.add(
            accountDao.insert(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

    }

    private fun handleResponse(){

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }
    }