package org.example.sample.log;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatingHistory {
    //TODO сохранение и чтение данных из файлов
    public void saveToFile(String nick, ArrayList<String> list){
        try (ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(String.format("history_%s.txt", nick)))){
            String[] text = list.toArray(new String[0]);
            file.writeObject(text);

        }catch (Exception e){
            e.getMessage();
        }
    }

    public String[] readFromFile(String nick){
        String[] text;
        try (ObjectInputStream file = new ObjectInputStream(new FileInputStream(String.format("history_%s.txt", nick)))){
            text = (String[]) file.readObject();
            return text;
        }catch (Exception e){
            e.getMessage();
            return text = new String[0];
        }
    }
}
