package com.softchar.contadordardos.ui.game.content

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.softchar.contadordardos.R
import com.softchar.contadordardos.ui.game.menu.MenuActivity
import com.softchar.contadordardos.ui.viewmodel.DartCounterViewModel

class DartCounterActivity : AppCompatActivity() {

    private val dartCounterViewModel : DartCounterViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dart_counter)

        val playerContainer = findViewById<LinearLayout>(R.id.playerContainer)
        val playerResetAll = findViewById<Button>(R.id.buttonResetAll)
        val numPlayers = intent.getIntExtra("PLAYER_COUNT", 1) // Valor por defecto 3
        val playerScore = intent.getIntExtra("PLAYER_SCORE", 101) // Valor por defecto 301

        dartCounterViewModel.setNumPlayers(numPlayers, playerScore)

        dartCounterViewModel.winner.observe(this) { winnerIndex ->
            winnerIndex?.let {
                showVictoryDialog(winnerIndex)
            }
        }

        dartCounterViewModel.players.observe(this) { players ->
            playerContainer.removeAllViews()

            players.forEachIndexed { index, playerLiveData ->
                val playerView = LayoutInflater.from(this).inflate(R.layout.player_view, playerContainer, false)

                val playerNameTextView = playerView.findViewById<TextView>(R.id.playerName)
                val playerEqualsTextView = playerView.findViewById<TextView>(R.id.playerEquals)
                val playerScoreTextView = playerView.findViewById<TextView>(R.id.playerScore)
                val playerMissingPointsTextView = playerView.findViewById<TextView>(R.id.playerMissingPoints)
                val playerInput = playerView.findViewById<EditText>(R.id.playerInput)
                val buttonAdd = playerView.findViewById<Button>(R.id.buttonAdd)
                val buttonBack = playerView.findViewById<Button>(R.id.buttonBack)
                val buttonReset = playerView.findViewById<Button>(R.id.buttonReset)
                val goToMenuButton = findViewById<Button>(R.id.buttonGoToMenu)

                when (index) {
                    0 -> {
                        playerNameTextView.setTextColor(getColor(R.color.blue))
                        playerEqualsTextView.setTextColor(getColor(R.color.blue))
                        playerScoreTextView.setTextColor(getColor(R.color.blue))
                    }
                    1 -> {
                        playerNameTextView.setTextColor(getColor(R.color.red))
                        playerEqualsTextView.setTextColor(getColor(R.color.red))
                        playerScoreTextView.setTextColor(getColor(R.color.red))
                    }
                    2 -> {
                        playerNameTextView.setTextColor(getColor(R.color.green))
                        playerEqualsTextView.setTextColor(getColor(R.color.green))
                        playerScoreTextView.setTextColor(getColor(R.color.green))
                    }
                    3 -> {
                        playerNameTextView.setTextColor(getColor(R.color.orange))
                        playerEqualsTextView.setTextColor(getColor(R.color.orange))
                        playerScoreTextView.setTextColor(getColor(R.color.orange))
                    }
                    4 -> {
                        playerNameTextView.setTextColor(getColor(R.color.purple))
                        playerEqualsTextView.setTextColor(getColor(R.color.purple))
                        playerScoreTextView.setTextColor(getColor(R.color.purple))
                    }
                    5 -> {
                        playerNameTextView.setTextColor(getColor(R.color.teal))
                        playerEqualsTextView.setTextColor(getColor(R.color.teal))
                        playerScoreTextView.setTextColor(getColor(R.color.teal))
                    }
                    6 -> {
                        playerNameTextView.setTextColor(getColor(R.color.brown))
                        playerEqualsTextView.setTextColor(getColor(R.color.brown))
                        playerScoreTextView.setTextColor(getColor(R.color.brown))
                    }
                    7 -> {
                        playerNameTextView.setTextColor(getColor(R.color.cyan))
                        playerEqualsTextView.setTextColor(getColor(R.color.cyan))
                        playerScoreTextView.setTextColor(getColor(R.color.cyan))
                    }
                    else -> {
                        playerNameTextView.setTextColor(getColor(R.color.default_color))
                        playerEqualsTextView.setTextColor(getColor(R.color.default_color))
                        playerScoreTextView.setTextColor(getColor(R.color.default_color))
                    }
                }

                goToMenuButton.setOnClickListener {
                    showExitConfirmationDialog()
                }

                playerLiveData.observe(this) { player ->
                    playerNameTextView.text = player.name
                    playerScoreTextView.text = player.sum.toString()
                    playerMissingPointsTextView.text = player.target.toString()
                }

                playerNameTextView.setOnClickListener {
                    showEditNameDialog(index)
                }

                buttonAdd.setOnClickListener {
                    addPlayers(index, playerInput)
                }

                buttonBack.setOnClickListener {
                    dartCounterViewModel.backFromPlayer(index)
                }

                buttonReset.setOnClickListener {
                    dartCounterViewModel.resetPlayer(index)
                }

                playerResetAll.setOnClickListener{
                    resetAllPlayers()
                }

                playerContainer.addView(playerView)
            }
        }

        // Inicializa el OnBackPressedCallback
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun addPlayers(index: Int, playerInput: EditText){
        val numberInput = playerInput.text.toString()
        if (numberInput.isNotEmpty()) {
            val number = numberInput.toInt()
            if (number >= dartCounterViewModel.MAX_NUMBER) {
                Toast.makeText(this, "Número demasiado grande", Toast.LENGTH_SHORT).show()
            } else {
                val player = dartCounterViewModel.players.value?.get(index)?.value
                if (player != null) {
                    if (number <= player.target) {
                        if (player.target - number == 0)
                            showVictoryDialog(index)
                        dartCounterViewModel.addToPlayer(index, number)
                        playerInput.text.clear()
                    } else {
                        Toast.makeText(this, "Superaste el límite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Ingresá un número", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetAllPlayers() {
        dartCounterViewModel.players.value?.forEachIndexed { index, playerLiveData ->
            playerLiveData.value?.let { player ->
                dartCounterViewModel.resetPlayer(index)
            }
        }
    }

    private fun showEditNameDialog(playerIndex: Int) {
        val playerLiveData = dartCounterViewModel.players.value?.get(playerIndex)
        val player = playerLiveData?.value
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_name, null)
        val editNameEditText = dialogView.findViewById<EditText>(R.id.editNameEditText)

        editNameEditText.setText(player?.name)
        editNameEditText.setSelection(editNameEditText.text.length)

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
            .setCancelable(false)
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Guardar") { dialog, _ ->
                val newName = editNameEditText.text.toString()
                if (newName.length in 1..6) {
                    player?.name = newName
                    playerLiveData?.value = player
                } else {
                    Toast.makeText(this, "El nombre debe tener entre 1 a 6 carácteres", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.setTitle("Ingresa un Nombre")
        alertDialog.show()
    }

    private fun showVictoryDialog(winnerIndex: Int) {
        val playerLiveData = dartCounterViewModel.players.value?.get(winnerIndex)
        val player = playerLiveData?.value
        val playerName = player?.name ?: "Jugador ${winnerIndex + 1}"

        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_victory, null)
        val victoryImage = dialogView.findViewById<ImageView>(R.id.victoryImage)

        victoryImage.setImageResource(getRandomVictoryImage())

        dialogBuilder.setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.setTitle("¡Victoria! >> $playerName << ha ganado!")
        alertDialog.show()
    }

    private fun getRandomVictoryImage(): Int {
        val images = listOf(
            R.drawable.victory_image_1,
            R.drawable.victory_image_2
        )
        return images.random()
    }

    private fun showExitConfirmationDialog() {
        // Crea el AlertDialog.Builder
        val builder = AlertDialog.Builder(this)

        // Configura el título y el mensaje del diálogo
        builder.setTitle("Confirmar salida")
            .setMessage("¿Estás seguro de que quieres ir al menú? Todos los cambios se perderán.")

        builder.setPositiveButton("Sí") { dialog, _ ->
            navigateToMenu()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}




