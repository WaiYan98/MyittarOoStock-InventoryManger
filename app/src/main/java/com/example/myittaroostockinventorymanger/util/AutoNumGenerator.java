package com.example.myittaroostockinventorymanger.util;

import java.util.Random;

public class AutoNumGenerator {

    //this method generate random num 1 to 5
    public static int generateNum() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }
}
