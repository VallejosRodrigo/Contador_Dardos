package com.softchar.contadordardos.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softchar.contadordardos.data.model.Player

class DartCounterViewModel : ViewModel() {


    val MAX_NUMBER = 1002

    private val _players = MutableLiveData<List<MutableLiveData<Player>>>()
    val players: MutableLiveData<List<MutableLiveData<Player>>> get() = _players

    private val _winner = MutableLiveData<Int?>()
    val winner: LiveData<Int?> get() = _winner

    fun setNumPlayers(numPlayers: Int, targetScore: Int) {
        val newPlayers = List(numPlayers) {
            MutableLiveData(Player(target = targetScore, initialTarget = targetScore))
        }
        _players.value = newPlayers
    }

    fun addToPlayer(playerIndex: Int, number: Int) {
        val playerList = _players.value ?: return
        val playerLiveData = playerList[playerIndex]
        val player = playerLiveData.value ?: Player()


        player.sum += number
        player.target -= number
        player.numbersAdded.add(number)

        playerLiveData.value = player

        Log.d("DartCounterViewModel", "Player $playerIndex: sum = ${player.sum}, target = ${player.target}")
    }

    fun backFromPlayer(playerIndex: Int) {
        val playerList = _players.value ?: return
        val playerLiveData = playerList[playerIndex]
        val player = playerLiveData.value ?: Player()
        if (player.sum > 0 && player.numbersAdded.isNotEmpty()) {
            val lastNumber = player.numbersAdded.removeAt(player.numbersAdded.size - 1)
            player.sum -= lastNumber
            player.target += lastNumber
            playerLiveData.value = player
        }
    }

    fun resetPlayer(playerIndex: Int) {
        val playerList = _players.value ?: return
        val playerLiveData = playerList[playerIndex]
        val player = playerLiveData.value ?: Player()
        player.sum = 0
        player.target = player.initialTarget
        player.numbersAdded.clear()
        playerLiveData.value = player
    }


}