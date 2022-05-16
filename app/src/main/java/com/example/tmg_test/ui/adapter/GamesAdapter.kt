package com.example.tmg_test.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmg_test.R
import com.example.tmg_test.databinding.ItemGameRecordBinding
import com.example.tmg_test.model.GameModel

class GamesAdapter(
    val items: List<GameModel>,
    val gameClickListener: (GameModel) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GamesViewHolder>() {

    lateinit var bind: ItemGameRecordBinding

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        bind = ItemGameRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GamesViewHolder(bind.root)
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        val item = items[position]

        bind.gameRecordItemFirstPlayerName.text = item.firstPlayer.name
        bind.gameRecordItemFirstPlayerScore.text = item.firstPlayerScore.toString()
        bind.gameRecordItemSecondPlayerName.text = item.secondPlayer.name
        bind.gameRecordItemSecondPlayerScore.text = item.secondPlayerScore.toString()

        holder.itemView.setOnClickListener { gameClickListener(item) }
    }

    override fun getItemCount(): Int = items.size

    class GamesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
