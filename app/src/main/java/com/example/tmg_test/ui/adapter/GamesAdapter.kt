package com.example.tmg_test.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmg_test.databinding.ItemGameRecordBinding
import com.example.tmg_test.model.GameModel

class GamesAdapter(
    private val items: List<GameModel>,
    private val gameClickListener: (GameModel) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GamesViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        val bind = ItemGameRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GamesViewHolder(bind)
    }

    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, gameClickListener)
    }

    override fun getItemCount(): Int = items.size

    class GamesViewHolder(val bind: ItemGameRecordBinding) : RecyclerView.ViewHolder(bind.root){
        fun bind(item: GameModel, gameClickListener: (GameModel) -> Unit){
            bind.gameRecordItemFirstPlayerName.text = item.firstPlayer.name
            bind.gameRecordItemFirstPlayerScore.text = item.firstPlayerScore.toString()
            bind.gameRecordItemSecondPlayerName.text = item.secondPlayer.name
            bind.gameRecordItemSecondPlayerScore.text = item.secondPlayerScore.toString()

            bind.root.setOnClickListener { gameClickListener(item) }
        }
    }
}
