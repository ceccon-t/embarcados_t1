package br.ufrgs.inf.embarcados.battleship.network.match

data class MatchResponseModel (
    val sessionKey: String,
    val turn: Int,
    val lastAction: String,
    val lastActor: String
)