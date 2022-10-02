package br.ufrgs.inf.embarcados.battleship.network.match

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface MatchRequestInterface {

    @POST("/match")
    fun sendRequest(@Body reqModel: MatchRequestModel): Call<MatchResponseModel>
}