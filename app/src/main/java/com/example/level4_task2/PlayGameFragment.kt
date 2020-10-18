package com.example.level4_task2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        gameRepository = GameRepository(requireContext())

        // Display all time stats
        displayStats()

        // Set click listeners for each image and send string parameter of chosen option to startGame function
        rockImage.setOnClickListener() {
            startGame(0)
        }

        paperImage.setOnClickListener() {
            startGame(1)
        }

        scissorsImage.setOnClickListener() {
            startGame(2)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayStats(){
        // Launch inside coroutine
        mainScope.launch {
            val draws = withContext(Dispatchers.IO) {
                gameRepository.countDraws()
            }
            val wins = withContext(Dispatchers.IO) {
                gameRepository.countWins()
            }
            val losses = withContext(Dispatchers.IO) {
                gameRepository.countLosses()
            }

            // Setting stats text in TextView for stats
            stats_query.text = "Win: ${wins} Draw: ${draws} Lose:${losses}"
        }
    }

    // Function to determine the plays of the player and computer based on the players click
    private fun startGame(selectedOption: Int) {
        // Set image to chosen option
        when (selectedOption){
            0 -> imagePlayer.setImageResource(R.drawable.rock)
            1 -> imagePlayer.setImageResource(R.drawable.paper)
            2 -> imagePlayer.setImageResource(R.drawable.scissors)
        }

        // Make list of all possible selections and then randomize the computer's played hand
        var options = arrayOf(0, 1, 2)
        val computerOption : Int = options.random()

        // Set image to random option stored in computerOption
        when (computerOption){
            0 -> imageComputer.setImageResource(R.drawable.rock)
            1 -> imageComputer.setImageResource(R.drawable.paper)
            2 -> imageComputer.setImageResource(R.drawable.scissors)
        }

        checkResult(computerOption, selectedOption)
    }

    private fun checkResult(computerPlay: Int, userPlay: Int){
        // Save the textview widget that displays result in result variable. Then change the text based on the hands played
        if (userPlay ==  computerPlay){
            result.setText(R.string.result_draw)
        } else if (computerPlay == 0 && userPlay == 2 || computerPlay == 1 && userPlay == 0 ||
            computerPlay == 2 && userPlay == 1){
            result.setText(R.string.result_lose)
        } else {
            result.setText(R.string.result_win)
        }

        // Insert games into Game History db
        addGameToDatabase(computerPlay, userPlay, result)
    }

    // TODO: fix inserting game into db
    private fun addGameToDatabase(computerPlay: Int, userPlay: Int, result: TextView){
        mainScope.launch {
            val game = Game(
                date = Date(),
                moveComputer = computerPlay,
                movePlayer = userPlay,
                result = result.text.toString()             // convert result (TextView) to string so it can be saved in db
            )

            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
    }
}