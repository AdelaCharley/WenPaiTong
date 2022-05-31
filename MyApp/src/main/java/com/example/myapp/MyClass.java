package com.UNI89F14E3.myapp;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class MyClass implements Cloneable {

    public static void main(String[] args){

        LinkedHashMap<Integer,String> stringLinkedHashMap = new LinkedHashMap<>();
        stringLinkedHashMap.put(10,"fd");
        stringLinkedHashMap.get(10);
        new LinkedHashSet<String>();
        System.currentTimeMillis();
    }

    private static String timeDifference(String timeEnd, String timeStart){
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        try{
            System.out.println(System.currentTimeMillis());
            Date t1 = df.parse(timeEnd);
            System.out.println(t1.getTimezoneOffset());
            Date t2 = df.parse(timeStart);
            System.out.println(t2.getTimezoneOffset());
            long diff = t1.getTime() - t2.getTime();
            System.out.println(diff);
            return (df.format(diff));
        } catch (Exception e){
            return e.toString();
        }

    }

    private void set(){}

}
