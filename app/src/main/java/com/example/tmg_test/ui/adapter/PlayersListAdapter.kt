package com.example.tmg_test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmg_test.R
import com.example.tmg_test.model.PlayerModel
import kotlinx.android.synthetic.main.item_players_list.view.*

class PlayersListAdapter(
    var items: List<PlayerModel>,
) : RecyclerView.Adapter<PlayersListAdapter.PlayersListViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersListViewHolder {
        return PlayersListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_players_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlayersListViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            vPlayersListItemName.text = item.name
        }
    }

    override fun getItemCount(): Int = items.size

    class PlayersListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}