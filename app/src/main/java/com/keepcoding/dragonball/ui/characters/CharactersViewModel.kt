package com.keepcoding.dragonball.ui.characters

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.keepcoding.dragonball.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class CharactersViewModel : ViewModel() {
    val BASE_URL = "https://dragonball.keepcoding.education/api/"

    val stateLiveData: MutableLiveData<FragmentListState> by lazy {
        MutableLiveData<FragmentListState>()
    }

    val charactersList: MutableLiveData<List<DbCharacter>?> = MutableLiveData()

    val selectedCharacter: MutableLiveData<DbCharacter> = MutableLiveData()
    val randomCharacter: MutableLiveData<DbCharacter> = MutableLiveData()

    val charactersAlive: MutableLiveData<Int> = MutableLiveData()

    fun getCharacters(token: String) {
        println("Token: $token")
        setValueOnMainThreadToError(FragmentListState.Loading)
        val client = OkHttpClient()
        val url = "${BASE_URL}heros/all"
        val body = FormBody.Builder()
            .add("name", "")
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .method("POST", body)
            .build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                setValueOnMainThreadToError(FragmentListState.Error(e.message.toString()))
                Log.d("Get Characters Error", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.code != 200) {
                    Log.d("Error", response.message)
                    setValueOnMainThreadToError(FragmentListState.Error(response.message))
                } else {
                    val responseBody = response.body?.string()

                    responseBody?.let {
                        Log.d("Success", it)
                    }

                    val charactersDtoArray: Array<DbCharacterDto> =
                        Gson().fromJson(responseBody, Array<DbCharacterDto>::class.java)

                    val charactersArray = charactersDtoArray.map {
                        if (it.name == "Goku" || it.name == "Vegeta") {
                            DbCharacter(it.id, it.name, it.description, it.photo, it.favorite)
                        } else {
                            DbCharacter(it.id, it.name, it.description, it.photo, it.favorite, 0, 0)
                        }

                    }

                    charactersList.postValue(charactersArray)
                    charactersAlive.postValue(charactersArray.count())

                    Log.d("Success", charactersArray.toString())
                    setValueOnMainThreadToError(FragmentListState.SuccessCharacters(charactersArray))
                }
            }

        })
    }

    fun setSelectedCharacter(character: DbCharacter) {
        selectedCharacter.postValue(character)
    }

    fun getRandomCharacter() {
        if (charactersList.value?.isNotEmpty() == true) {
            var randomItem = charactersList.value!!.random()
            println("Random item $randomItem")

            if (randomItem.id != selectedCharacter.value!!.id && randomItem.currentLife > 0) {
                randomCharacter.postValue(randomItem)
            } else {
                while (randomItem.id == selectedCharacter.value!!.id || randomItem.currentLife == 0) {
                    randomItem = charactersList.value!!.random()
                }
                randomCharacter.postValue(randomItem)
            }
        }
    }

    fun fight() {
        val firstCharacter = selectedCharacter.value
        val secondCharacter = randomCharacter.value

        val damage1 = (10..60).random()
        val damage2 = (10..60).random()

        if (firstCharacter != null) {
            var resultHealth1 = firstCharacter.currentLife - damage1

            if (resultHealth1 <= 0) {
                resultHealth1 = 0
            }

            firstCharacter.currentLife = resultHealth1
            selectedCharacter.postValue(firstCharacter!!)
        }

        if (secondCharacter != null) {
            var resultHealth2 = secondCharacter.currentLife - damage2

            if (resultHealth2 <= 0) {
                resultHealth2 = 0
            }
            secondCharacter.currentLife = resultHealth2
            randomCharacter.postValue(secondCharacter!!)
        }

    }

    fun checkSurvivors(context: Context) {
        getSurvivor().let {
            if (it != null) {
                if (it.name.isBlank()) charactersAlive.postValue(0)
                else charactersAlive.postValue(1)
            }
        }
    }

    fun getSurvivor(): DbCharacter? {
        val aliveCharacters = charactersList.value?.filter { it.currentLife > 0 }

        if (aliveCharacters?.count() == 1) {
            return aliveCharacters[0]
        }
        if (aliveCharacters?.isEmpty() == true) {
            return DbCharacter("", "", "", "", false)
        }
        return null
    }

    fun reset(){
        val newCharactersList = charactersList.value?.map {

            DbCharacter(it.id, it.name, it.description, it.photo, it.favorite, 100, 100)
        }

        Log.d("Lista", newCharactersList.toString())

        charactersAlive.postValue(newCharactersList?.count())
        charactersList.postValue(newCharactersList)
        setValueOnMainThreadToError(FragmentListState.SuccessCharacters(newCharactersList ?: listOf()))
    }

    fun setValueOnMainThreadToError(value: FragmentListState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    sealed class FragmentListState {
        data class SuccessCharacters(val characterList: List<DbCharacter>) : FragmentListState()
        data class Error(val message: String) : FragmentListState()
        object Loading : FragmentListState()
    }

}