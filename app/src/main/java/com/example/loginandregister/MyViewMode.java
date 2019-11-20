package com.example.loginandregister;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewMode extends ViewModel {
    private MutableLiveData<String> stringMutableLiveData;

    public void init()
    {
        stringMutableLiveData=new MutableLiveData<>();
    }
    public void sendMessage(String msg)
    {
        stringMutableLiveData.setValue(msg);
    }
    public LiveData<String> getMeessage()
    {
        return stringMutableLiveData;
    }
}
