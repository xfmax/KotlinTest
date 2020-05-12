package com.base.mykotlintest

import android.util.Log
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel(){

    suspend fun launchDataLoad(){
        sortList();
    }

    private suspend fun sortList() {
        Log.d("xbase","sortList")
    }
}