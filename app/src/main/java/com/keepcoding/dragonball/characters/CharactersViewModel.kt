package com.keepcoding.dragonball.characters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.keepcoding.dragonball.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class CharactersViewModel:ViewModel() {

    val stateLiveData : MutableLiveData<FragmentListState> by lazy {
        MutableLiveData<FragmentListState>()
    }

    fun getCharacters(token:String){
        println("Token: $token")
        setValueOnMainThreadToError(FragmentListState.Loading)
        val client = OkHttpClient()
        val url = "https://dragonball.keepcoding.education/api/heros/all"
        val body = FormBody.Builder()
            .add("name","")
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer $token")
            .method("POST", body)
            .build()
        val call = client.newCall(request)

        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                setValueOnMainThreadToError(FragmentListState.Error(e.message.toString()))
                Log.d("Get Characters Error",e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.code != 200){
                    Log.d("Error", response.message)
                    setValueOnMainThreadToError(FragmentListState.Error(response.message))
                }else{
                    val responseBody = response.body?.string()

                    responseBody?.let {
                        Log.d("Success", it)
                    }

                    val charactersDtoArray:Array<CharacterDto> =Gson().fromJson(responseBody,Array<CharacterDto>::class.java)

                    val charactersArray = charactersDtoArray.map {
                        Character(it.id, it.name, it.description,it.photo, it.favorite)
                    }
                    Log.d("Success", charactersArray.toString())
                    setValueOnMainThreadToError(FragmentListState.SuccessCharacters(charactersArray))
                }
            }

        })
    }

    fun setValueOnMainThreadToError(value: FragmentListState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    sealed class FragmentListState{
        data class SuccessCharacters(val characterList : List<Character>) : FragmentListState()
        data class Error(val message : String): FragmentListState()
        object Loading : FragmentListState()
    }

}