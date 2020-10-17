package com.example.level4_task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GamesFragment : Fragment() {

    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val games = arrayListOf<Game>()
    private val gamesAdapter = GamesAdapter(games)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())

        getGamesFromDatabase()

        initRv()

        // On click delete floating action button delete all games from history
        fabDeleteAll.setOnClickListener() {
            removeAllGames()
        }

    }

        private fun initRv() {
            // Initialize the recycler view with a linear layout manager, adapter
            rvHistoryGames.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvHistoryGames.adapter = gamesAdapter
//            rvHistoryGames.setHasFixedSize(true)
            rvHistoryGames.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        private fun getGamesFromDatabase() {
            mainScope.launch {
                val gameHistory = withContext(Dispatchers.IO) {
                    gameRepository.getAllGames()
                }
                this@GamesFragment.games.clear()
                this@GamesFragment.games.addAll(gameHistory)
                this@GamesFragment.gamesAdapter.notifyDataSetChanged()
            }
        }

    // TODO: add this function to trash icon in menu of history fragment
        private fun removeAllGames() {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.deleteAllGames()
                }
                getGamesFromDatabase()
            }
        }

    }