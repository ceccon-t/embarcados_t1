package br.ufrgs.inf.embarcados.battleship.network.setup

data class SetupResponseModel(
    val sessionKey: String,
    val player1: String,
    val player2: String,
    val playerOnePatrol: String,
    val playerOneDestroyer: String,
    val playerOneBattleship: String,
    val playerOneCarrier: String,
    val playerTwoPatrol: String,
    val playerTwoDestroyer: String,
    val playerTwoBattleship: String,
    val playerTwoCarrier: String
)