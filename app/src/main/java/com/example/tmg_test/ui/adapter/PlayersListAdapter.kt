package com.example.tmg_test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmg_test.R
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
        bind = ItemPlayersListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayersListViewHolder(bind.root)
    }

    override fun onBindViewHolder(holder: PlayersListViewHolder, position: Int) {
        val item = items[position]

        bind.playersListItemName.text = item.name
    }

    override fun getItemCount(): Int = items.size

    class PlayersListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
