package com.iescampanillas.arassistant.utils;

import java.util.Random;

public final class Generator {

    private static Generator generatorInstance = null;

    private Generator() {
    }

    public static Generator getInstance() {
        if(generatorInstance == null) {
            generatorInstance = new Generator();
        }
        return generatorInstance;
    }

    /**
     * Id generator for Tasks and Reminders
     * Valid Characters 0-9, a-z, A-Z
     *
     * @return Returns a random string
     * */
    public static String generateId(String prefix) {
        Random random = new Random();
        int length = 30;
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        int num, start;
        for (int i = 0; i < length; i++){
            switch(random.nextInt(4)) {
                case 0:
                    num = 9;
                    start = 48;
                    break;
                case 1:
                    num = 26;
                    start = 65;
                    break;
                default:
                    num = 26;
                    start = 97;
                    break;
            }
            tempChar = (char) (random.nextInt(num) + start);
            randomStringBuilder.append(tempChar);
        }
        return prefix + randomStringBuilder.toString();
    }
}
