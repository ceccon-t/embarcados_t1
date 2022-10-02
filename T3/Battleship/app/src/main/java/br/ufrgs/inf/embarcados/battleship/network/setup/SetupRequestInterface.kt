package br.ufrgs.inf.embarcados.battleship.network.setup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SetupRequestInterface {

    @POST("/setup")
    fun sendRequest(@Body reqModel: SetupRequestModel): Call<SetupResponseModel>
}