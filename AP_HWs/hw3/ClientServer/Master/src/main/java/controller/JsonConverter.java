package controller;

import com.google.gson.Gson;

import model.Task;

public class JsonConverter {

    public static Task convertJsonDataToTask(String data){
        Gson gson= new Gson();
        Task task=gson.fromJson(data,Task.class);
        return task;
    }


}
