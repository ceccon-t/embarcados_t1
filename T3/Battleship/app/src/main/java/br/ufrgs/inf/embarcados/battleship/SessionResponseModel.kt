package br.ufrgs.inf.embarcados.battleship

data class SessionResponseModel (
    val granted: Boolean,
    val sessionKey: String,
    val yourId: String
)