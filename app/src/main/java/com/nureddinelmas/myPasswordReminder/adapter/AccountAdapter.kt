package com.nureddinelmas.myPasswordReminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nureddinelmas.myPasswordReminder.model.Accounts
import myPasswordReminder.databinding.RecyclerRowBinding

class AccountAdapter (private val accountList : List<Accounts>) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
    var directionOfSecond = false
    class ViewHolder(val recyclerRowBinding: RecyclerRowBinding) : RecyclerView.ViewHolder(recyclerRowBinding.root){


        init {
            recyclerRowBinding.secondLinearLayout.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val recyclerRowBinding: RecyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      holder.recyclerRowBinding.recyclerView.setText(accountList.get(position).accountName)

        holder.itemView.setOnClickListener {
            if (!directionOfSecond){
                holder.recyclerRowBinding.secondLinearLayout.visibility = View.VISIBLE
                holder.recyclerRowBinding.PasswordTextView.text = "Password : " + accountList[position].password
                holder.recyclerRowBinding.userNameTextView.text = "User Name : " + accountList.get(position).userName
                holder.recyclerRowBinding.comments.text= "Comments : " + accountList.get(position).comment
                directionOfSecond = true
            }else{
                holder.recyclerRowBinding.secondLinearLayout.visibility = View.GONE
                directionOfSecond = false
            }
        }


    }

    override fun getItemCount(): Int {
       return accountList.size
    }
}