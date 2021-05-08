package com.example.ambient_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter : ListAdapter<FavoritesEntry, FavoritesAdapter.FavoritesViewHolder>(FavoritesComparator) {
    // Set up on click listener for items in recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val newViewHolder = FavoritesViewHolder.create(parent)
        newViewHolder.itemView.setOnClickListener {
            val position = newViewHolder.adapterPosition
            if (position != NO_POSITION) {
                val item = getItem(position)
                val intent = Intent("ADAPTER")
                val content: ArrayList<String> = item.content as ArrayList<String>
                val volume: ArrayList<String> = item.volume as ArrayList<String>
                intent.putExtra("ACTION", "PLAY")
                intent.putStringArrayListExtra("CONTENT", content)
                intent.putStringArrayListExtra("VOLUME", volume)
                LocalBroadcastManager.getInstance(parent.context).sendBroadcast(intent)
            }
        }
        return newViewHolder
    }

    // Bind view holder
    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    // Set up view holder
    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameItemView: TextView = itemView.findViewById(R.id.textName)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        // Set up text field
        fun bind(current: FavoritesEntry) {
            nameItemView.text = current.name
            buttonDelete.setOnClickListener {
                val intent = Intent("ADAPTER")
                intent.putExtra("ACTION", "DELETE")
                intent.putExtra("ID", current.id)
                LocalBroadcastManager.getInstance(itemView.context).sendBroadcast(intent)
            }
        }

        companion object {
            fun create(parent: ViewGroup): FavoritesViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.inner, parent, false)
                return FavoritesViewHolder(view)
            }
        }
    }

    // Used to compare IDs of objects within room database
    companion object {
        private val FavoritesComparator = object : DiffUtil.ItemCallback<FavoritesEntry>() {
            override fun areItemsTheSame(oldItem: FavoritesEntry, newItem: FavoritesEntry): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FavoritesEntry, newItem: FavoritesEntry): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

