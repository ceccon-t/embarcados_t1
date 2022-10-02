package br.ufrgs.inf.embarcados.battleship.network.match

data class MatchRequestModel (
    val sessionKey: String,
    val myId: String,
    val turn: Int,
    val myLastAction: String
)