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

        getShoppingListFromDatabase()

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
            val result: TextView = findViewById(R.id.result) as TextView
            if (computerPlay == userPlay){
                result.text = "Draw!"
            } else if (computerPlay == "rock" && userPlay == "scissors" || computerPlay == "paper" && userPlay == "rock" ||
                computerPlay == "scissors" && userPlay == "paper"){
                result.text = "You lose!"
            } else {
                result.text = "You win!"
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

            createItemTouchHelper().attachToRecyclerView(rvShoppingList)

        }

        @SuppressLint("InflateParams")
        private fun showAddProductdialog() {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.add_product_dialog_title))
            val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)
            val productName = dialogLayout.findViewById<EditText>(R.id.txt_product_name)
            val amount = dialogLayout.findViewById<EditText>(R.id.txt_amount)

            builder.setView(dialogLayout)
            builder.setPositiveButton(R.string.dialog_ok_btn) { _: DialogInterface, _: Int ->
                addProduct(productName, amount)
            }
            builder.show()
        }

        private fun addProduct(txtProductName: EditText, txtAmount: EditText) {
            if (validateFields(txtProductName, txtAmount)) {
                mainScope.launch {
                    val product = Game(
                        productName = txtProductName.text.toString(),
                        productQuantity = txtAmount.text.toString().toShort()
                    )

                    withContext(Dispatchers.IO) {
                        productRepository.insertProduct(product)
                    }

                    getShoppingListFromDatabase()
                }
            }
        }

        private fun validateFields(
            txtProductName: EditText
            , txtAmount: EditText
        ): Boolean {
            return if (txtProductName.text.toString().isNotBlank()
                && txtAmount.text.toString().isNotBlank()
            ) {
                true
            } else {
                Toast.makeText(
                    activity,
                    "Please fill in the fields (amount must be a number)!",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }


        /**
         * Create a touch helper to recognize when a user swipes an item from a recycler view.
         * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         * and uses callbacks to signal when a user is performing these actions.
         */
        private fun createItemTouchHelper(): ItemTouchHelper {

            // Callback which is used to create the ItemTouch helper. Only enables left swipe.
            // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
            val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                // Enables or Disables the ability to move items up and down.
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // Callback triggered when a user swiped an item.
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
//                products.removeAt(position)
//                shoppingListAdapter.notifyDataSetChanged()
                    val productToDelete = products[position]
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            productRepository.deleteProduct(productToDelete)
                        }
                        getShoppingListFromDatabase()
                    }


                }
            }
            return ItemTouchHelper(callback)
        }

        private fun getShoppingListFromDatabase() {
            mainScope.launch {
                val shoppingList = withContext(Dispatchers.IO) {
                    productRepository.getAllProducts()
                }
                this@GamesFragment.products.clear()
                this@GamesFragment.products.addAll(shoppingList)
                this@GamesFragment.shoppingListAdapter.notifyDataSetChanged()
            }
        }

        private fun removeAllProducts() {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    productRepository.deleteAllProducts()
                }
                getShoppingListFromDatabase()
            }
        }


    }