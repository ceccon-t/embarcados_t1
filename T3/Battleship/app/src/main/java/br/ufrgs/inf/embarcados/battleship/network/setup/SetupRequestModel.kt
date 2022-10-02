package br.ufrgs.inf.embarcados.battleship.network.setup

data class SetupRequestModel (
    val sessionKey: String,
    val myId: String,
    val patrol: String,
    val destroyer: String,
    val battleship: String,
    val carrier: String
)