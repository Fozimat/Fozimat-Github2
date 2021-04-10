package com.fozimat.fozimat_github.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fozimat.fozimat_github.DetailActivity
import com.fozimat.fozimat_github.R
import com.fozimat.fozimat_github.databinding.ItemRowUserBinding
import com.fozimat.fozimat_github.helper.CustomOnItemClickListener
import com.fozimat.fozimat_github.model.User

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {
    var listUser = ArrayList<User>()
    set(listUser) {
        if(listUser.size > 0) {
            this.listUser.clear()
        }
        this.listUser.addAll(listUser)

        notifyDataSetChanged()
    }

    fun addItem(user: User) {
        this.listUser.add(user)
        notifyItemInserted(this.listUser.size - 1)
    }

    fun updateItem(position: Int, note: User) {
        this.listUser[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUser.size)
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
        val binding = ItemRowUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions())
                    .into(binding.imgAvatar)

            binding.tvName.text = user.login
            itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                override fun onItemClicked(view: View, position: Int) {
//                    val move = Intent(activity, DetailActivity::class.java)
//                    move.putExtra(DetailActivity.EXTRA_POSITION, position)
//                    move.putExtra(DetailActivity.EXTRA_USERNAME, username)
//                    activity.startActivity(move)
                }
            }))
        }

    }
}