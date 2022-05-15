package com.example.tmg_test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmg_test.R
import com.example.tmg_test.model.GameModel
import kotlinx.android.synthetic.main.item_game_record.view.*

class GamesAdapter(
    val items: List<GameModel>,
    val gameClickListener: (GameModel) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GamesViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        return GamesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            vGameRecordItemFirstPlayerName.text = item.firstPlayer.name
            vGameRecordItemFirstPlayerScore.text = item.firstPlayerScore.toString()
            vGameRecordItemSecondPlayerName.text = item.secondPlayer.name
            vGameRecordItemSecondPlayerScore.text = item.secondPlayerScore.toString()

            setOnClickListener { gameClickListener(item) }
        }
    }

    override fun getItemCount(): Int = items.size

    class GamesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}