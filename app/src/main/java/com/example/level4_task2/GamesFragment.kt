package com.example.level4_task2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level4_task2.databinding.ActivityMainBinding
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

        // Set click listeners for each image and send string parameter of chosen option to startGame function
        rockImage.setOnClickListener() {
            startGame("rock")
        }

        paperImage.setOnClickListener() {
            startGame("paper")
        }

        scissorsImage.setOnClickListener() {
            startGame("scissors")
        }
    }

    // Function to determine the plays of the player and computer based on the players click
        private fun startGame(selectedOption: String) {
            when (selectedOption){
                "rock" -> imagePlayer.setImageResource(R.drawable.rock)
                "paper" -> imagePlayer.setImageResource(R.drawable.paper)
                "scissors" -> imagePlayer.setImageResource(R.drawable.scissors)
            }

            // Make list of all possible selections and then randomize the computer's played hand
            var options = arrayOf("rock", "paper", "scissors")
            val computerOption = options.random()

            when (computerOption){
                "rock" -> imageComputer.setImageResource(R.drawable.rock)
                "paper" -> imageComputer.setImageResource(R.drawable.paper)
                "scissors" -> imageComputer.setImageResource(R.drawable.scissors)
            }

            checkResult(computerOption, selectedOption)

        }

        private fun checkResult(computerPlay: String, userPlay: String){
            // Save the textview widget that displays result in result variable. Then change the text based on the hands played
            var result = ""
            if (computerPlay == userPlay){
                result = "Draw!"
            } else if (computerPlay == "rock" && userPlay == "scissors" || computerPlay == "paper" && userPlay == "rock" ||
                computerPlay == "scissors" && userPlay == "paper"){
                result = "You lose!"
            } else {
                result = "You win!"
            }

            mainScope.launch {
                val game = Game(
                    date = Date(),
                    moveComputer = computerPlay,
                    movePlayer = userPlay,
                    result = result
                )

                withContext(Dispatchers.IO) {
                    gameRepository.insertGame(game)
                }

                getGamesFromDatabase()
            }
        }

        private fun initRv() {

            // Initialize the recycler view with a linear layout manager, adapter
            rvShoppingList.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvShoppingList.adapter = gamesAdapter
            rvShoppingList.setHasFixedSize(true)
            rvShoppingList.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        private fun getGamesFromDatabase() {
            mainScope.launch {
                val shoppingList = withContext(Dispatchers.IO) {
                    gameRepository.getAllGames()
                }
                this@GamesFragment.games.clear()
                this@GamesFragment.games.addAll(shoppingList)
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