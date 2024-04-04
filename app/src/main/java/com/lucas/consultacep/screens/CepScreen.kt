package com.lucas.consultacep.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucas.consultacep.components.CardEndereco
import com.lucas.consultacep.model.Endereco
import com.lucas.consultacep.services.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.security.auth.login.LoginException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CepScreen() {
    var cepState by remember { mutableStateOf("") }
    var ufState by remember { mutableStateOf("") }
    var localidadeState by remember { mutableStateOf("") }
    var logradouroState by remember { mutableStateOf("") }

    var listEnderecoState by remember { mutableStateOf(listOf<Endereco>()) }

    var listEnderecoStateException by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "CONSULTA CEP", fontSize = 24.sp)
                Text(
                    text = "Encontre o seu endereço",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = cepState,
                    onValueChange = {
                        cepState = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Qual o CEP procurado?")
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                var call = RetrofitFactory().getEnderecoService().getEnderecoByCep(cepState)
                                call.enqueue(object : Callback<Endereco>{
                                    override fun onResponse(
                                        p0: Call<Endereco>,
                                        p1: Response<Endereco>
                                    ) {

                                    }
                                    override fun onFailure(p0: Call<Endereco>, p1: Throwable) {

                                    }

                                })
                            }
                        ){
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Não sabe o CEP?",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    OutlinedTextField(
                        value = ufState,
                        onValueChange = {
                            ufState = it
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        label = {
                            Text(text = "UF?")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            keyboardType = KeyboardType.Text
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = localidadeState,
                        onValueChange = {
                            localidadeState = it
                        },
                        modifier = Modifier.weight(2f),
                        label = {
                            Text(text = "Qual a cidade?")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = logradouroState,
                        onValueChange = {
                            logradouroState = it
                        },
                        modifier = Modifier.weight(2f),
                        label = {
                            Text(text = "O que lembra do nome da rua?")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                    IconButton(
                        onClick = {
                            var call = RetrofitFactory().getEnderecoService().getListEndereco(
                                uf = ufState,
                                localidade = localidadeState,
                                logradouro = logradouroState
                            )
                            call.enqueue(object : Callback<List<Endereco>>{

                                override fun onResponse(
                                    p0: Call<List<Endereco>>,
                                    p1: Response<List<Endereco>>
                                ) {
                                    listEnderecoState = p1.body()!!
                                }

                                override fun onFailure(
                                    p0: Call<List<Endereco>>,
                                    p1: Throwable
                                ) {
                                    listEnderecoStateException = p1.message.toString()
                                }

                            })
                        }
                    ){
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn() {
            items(listEnderecoState) {
                CardEndereco(it)
            }
        }

    }
}