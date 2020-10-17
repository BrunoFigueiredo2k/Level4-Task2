package com.example.level4_task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.game.view.*

class GamesAdapter(private val games: List<Game>) : RecyclerView.Adapter<GamesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(game: Game) {
            itemView.dateGame.text = game.date.toString()
            itemView.imagePlayer.setImageResource(game.movePlayer)
            itemView.imageComputer.setImageResource(game.moveComputer)
            itemView.resultHistory.text = game.result
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.game, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: GamesAdapter.ViewHolder, position: Int) {
        holder.databind(games[position])
    }

}