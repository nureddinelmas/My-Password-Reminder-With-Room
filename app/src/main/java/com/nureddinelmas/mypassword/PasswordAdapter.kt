package com.nureddinelmas.mypassword

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nureddinelmas.mypassword.databinding.RecyclerRowBinding

class PasswordAdapter(private val passwordList: ArrayList<Passwords>): RecyclerView.Adapter<PasswordAdapter.PasswordsHolder>() {



    inner class PasswordsHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

        init {

            binding.accountTextView.visibility = View.GONE
            binding.userNameTextView.visibility = View.GONE
            binding.PasswordTextView.visibility = View.GONE
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsHolder {
       val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordsHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordsHolder, position: Int) {
        holder.binding.recyclerView.text = passwordList[position].name

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, PasswordActivity::class.java)
            intent.putExtra("id",passwordList[position].id)
            intent.putExtra("info", "old")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return passwordList.size
    }
}