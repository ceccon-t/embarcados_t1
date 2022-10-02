package br.ufrgs.inf.embarcados.battleship.network.session

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionRequestInterface {

    @POST("/sessionRequest")
    fun sendRequest(@Body reqModel: SessionRequestModel): Call<SessionResponseModel>
}