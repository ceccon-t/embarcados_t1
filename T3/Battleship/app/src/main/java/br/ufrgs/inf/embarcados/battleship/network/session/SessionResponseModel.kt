package br.ufrgs.inf.embarcados.battleship.network.session

data class SessionResponseModel (
    val granted: Boolean,
    val sessionKey: String,
    val yourId: String
)