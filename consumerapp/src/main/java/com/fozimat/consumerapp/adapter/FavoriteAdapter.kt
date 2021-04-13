package com.fozimat.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fozimat.consumerapp.DetailFavActivity
import com.fozimat.consumerapp.DetailFavActivity.Companion.EXTRA_POSITION
import com.fozimat.consumerapp.DetailFavActivity.Companion.EXTRA_USERNAME_FAV
import com.fozimat.consumerapp.R
import com.fozimat.consumerapp.databinding.ItemRowUserBinding
import com.fozimat.consumerapp.helper.CustomOnItemClickListener
import com.fozimat.consumerapp.model.User

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {
    var listUser = ArrayList<User>()
    set(listUser) {
        if(listUser.size > 0) {
            this.listUser.clear()
        }
        this.listUser.addAll(listUser)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class UserViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions())
                    .into(binding.imgAvatar)

            binding.tvName.text = user.login
            itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                override fun onItemClicked(view: View, position: Int) {
                    val move = Intent(activity, DetailFavActivity::class.java)
                    move.putExtra(EXTRA_POSITION, position)
                    move.putExtra(EXTRA_USERNAME_FAV, user)
                    activity.startActivity(move)
                }
            }))
        }

    }
}