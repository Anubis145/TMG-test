package com.example.tmg_test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmg_test.databinding.ItemPlayersListBinding
import com.example.tmg_test.model.PlayerModel

class PlayersListAdapter(
    var items: List<PlayerModel>,
) : RecyclerView.Adapter<PlayersListAdapter.PlayersListViewHolder>() {

    lateinit var bind: ItemPlayersListBinding

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersListViewHolder {
        val bind = ItemPlayersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayersListViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PlayersListViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class PlayersListViewHolder(val bind: ItemPlayersListBinding) : RecyclerView.ViewHolder(bind.root){
        fun bind(item: PlayerModel) {
            bind.playersListItemName.text = item.name
        }
    }
}
