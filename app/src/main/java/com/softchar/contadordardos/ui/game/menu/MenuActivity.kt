package com.softchar.contadordardos.ui.game.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.softchar.contadordardos.R
import com.softchar.contadordardos.ui.game.content.DartCounterActivity
import kotlin.system.exitProcess

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val playerCountSpinner: Spinner = findViewById(R.id.spinnerPlayerCount)
        val playerScoreSpinner: Spinner = findViewById(R.id.spinnerPlayerScore)
        val startGameButton: Button = findViewById(R.id.buttonStartGame)
        val buttonSocialMedia = findViewById<ImageButton>(R.id.buttonSocialMedia)

        buttonSocialMedia.setOnClickListener {
            val url = "https://www.linkedin.com/in/rodrigo-ezequiel-vallejos/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        startGameButton.setOnClickListener {
            val playerCount = playerCountSpinner.selectedItem.toString().toInt()
            val playerScore = playerScoreSpinner.selectedItem.toString().toInt()
            val intent = Intent(this, DartCounterActivity::class.java).apply {
                putExtra("PLAYER_COUNT", playerCount)
                putExtra("PLAYER_SCORE", playerScore)
            }
            startActivity(intent)
            finish()
        }
    }

}