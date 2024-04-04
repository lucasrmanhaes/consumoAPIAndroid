package com.lucas.consultacep.services

import com.lucas.consultacep.model.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EnderecoService {

    @GET("{cep}/json/")
    fun getEnderecoByCep(@Path("cep") cep: String): Call<Endereco>

    @GET("{uf}/{localidade}/{logradouro}/json/")
    fun getListEndereco(@Path("uf") uf: String, @Path("localidade") localidade: String, @Path("logradouro") logradouro: String
    ): Call<List<Endereco>>

}