package com.example.tictactoexo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.tictactoexo.R

class MainActivity : AppCompatActivity() {

    private lateinit var board: Array<Array<Button>>
    private var currentPlayer = "X"
    private var gameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("button_${row}_$col", "id", packageName)
                findViewById<Button>(buttonId).apply {
                    setOnClickListener { onButtonClick(this, row, col) }
                }
            }
        }

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            resetBoard()
        }
    }

    private fun onButtonClick(button: Button, row: Int, col: Int) {
        if (button.text.isEmpty() && gameActive) {
            button.text = currentPlayer
            button.setTextColor(if (currentPlayer == "X") resources.getColor(R.color.red, null) else resources.getColor(R.color.blue, null))
            if (checkWin()) {
                showWinAnimation(currentPlayer)
                findViewById<TextView>(R.id.status_text).text = "Player $currentPlayer wins!"
                gameActive = false
            } else if (isBoardFull()) {
                findViewById<TextView>(R.id.status_text).text = "It's a draw!"
                gameActive = false
            } else {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                findViewById<TextView>(R.id.status_text).text = "Player $currentPlayer's turn"
            }
        }
    }

    private fun checkWin(): Boolean {
        for (i in 0..2) {
            if (board[i][0].text == currentPlayer && board[i][1].text == currentPlayer && board[i][2].text == currentPlayer) return true
            if (board[0][i].text == currentPlayer && board[1][i].text == currentPlayer && board[2][i].text == currentPlayer) return true
        }
        if (board[0][0].text == currentPlayer && board[1][1].text == currentPlayer && board[2][2].text == currentPlayer) return true
        if (board[0][2].text == currentPlayer && board[1][1].text == currentPlayer && board[2][0].text == currentPlayer) return true
        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { button -> button.text.isNotEmpty() } }
    }

    private fun resetBoard() {
        for (row in board) {
            for (button in row) {
                button.text = ""
                button.setBackgroundResource(R.drawable.grid_background)
            }
        }
        currentPlayer = "X"
        gameActive = true
        findViewById<TextView>(R.id.status_text).text = "Player X's turn"
        findViewById<LottieAnimationView>(R.id.confetti_animation).visibility = View.GONE
    }

    private fun showWinAnimation(winner: String) {
        findViewById<LottieAnimationView>(R.id.confetti_animation).apply {
            visibility = View.VISIBLE
            playAnimation()
        }
        Toast.makeText(this, "Congratulations! $winner wins!", Toast.LENGTH_LONG).show()
    }
}
