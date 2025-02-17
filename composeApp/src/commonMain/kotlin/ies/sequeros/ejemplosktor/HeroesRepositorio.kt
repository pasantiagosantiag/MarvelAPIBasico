package ies.sequeros.ejemplosktor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch

class HeroesRepositorio {
    //var json: MutableState<JsonObject> = JsonObject()
    val client = HttpClient() {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting() // ✅ Formatea JSON legible
                serializeNulls()
            }
        }
    }

    suspend fun getAll(): List<Heroe> {
        //hash 56396be214829c66d4fea11546dd80ca
        //public key a7acf61237bbea7f60a15f4831ccc66a
        val heroes = mutableListOf<Heroe>()
        val response: HttpResponse =
            client.get("https://gateway.marvel.com/v1/public/characters?ts=1&apikey=a7acf61237bbea7f60a15f4831ccc66a&hash=56396be214829c66d4fea11546dd80ca") {
                header("Accept", "application/json")
            }
        var json = JsonParser.parseString(response.bodyAsText()).asJsonObject
        json["data"].asJsonObject["results"].asJsonArray.forEach {
            var heroe = Heroe(
                it.asJsonObject["name"].asString,
                it.asJsonObject["description"].asString,
                it.asJsonObject["thumbnail"].asJsonObject["path"].asString + "/landscape_medium." + it.asJsonObject["thumbnail"].asJsonObject["extension"].asString
            )
            heroes.add(heroe)
        }
        return heroes
    }
}