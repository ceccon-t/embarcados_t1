package br.ufrgs.inf.embarcados.battleship

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionRequestInterface {

    @POST("/sessionRequest")
    fun sendRequest(@Body reqModel: SessionRequestModel): Call<SessionResponseModel>
}