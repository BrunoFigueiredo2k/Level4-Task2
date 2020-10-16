package com.example.level4_task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PlayGameFragment : Fragment() {
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val games = arrayListOf<Game>()
    private val gamesAdapter = GamesAdapter(games)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        // Set image to chosen option
        when (selectedOption){
            "rock" -> imagePlayer.setImageResource(R.drawable.rock)
            "paper" -> imagePlayer.setImageResource(R.drawable.paper)
            "scissors" -> imagePlayer.setImageResource(R.drawable.scissors)
        }

        // Make list of all possible selections and then randomize the computer's played hand
        var options = arrayOf("rock", "paper", "scissors")
        val computerOption : String = options.random()

        // Set image to random option stored in computerOption
        when (computerOption){
            "rock" -> imageComputer.setImageResource(R.drawable.rock)
            "paper" -> imageComputer.setImageResource(R.drawable.paper)
            "scissors" -> imageComputer.setImageResource(R.drawable.scissors)
        }

        checkResult(computerOption, selectedOption)
    }

    private fun checkResult(computerPlay: String, userPlay: String){
        // Save the textview widget that displays result in result variable. Then change the text based on the hands played
        if (userPlay ==  computerPlay){
            result.setText(R.string.result_draw)
        } else if (computerPlay == "rock" && userPlay == "scissors" || computerPlay == "paper" && userPlay == "rock" ||
            computerPlay == "scissors" && userPlay == "paper"){
            result.setText(R.string.result_lose)
        } else {
            result.setText(R.string.result_win)
        }

        // Insert games into Game History db
        addGameToDatabase(computerPlay, userPlay, result)
    }

    // TODO: fix inserting game into db
    private fun addGameToDatabase(computerPlay: String, userPlay: String, result: TextView){
        mainScope.launch {
            val game = Game(
                date = Date(),
                moveComputer = computerPlay,
                movePlayer = userPlay,
                result = result.toString()             // convert result (TextView) to string so it can be saved in db
            )

            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
    }
}