package com.studyorganizer.googleschedule.extras;

import java.util.Random;

public class RandomRequestIdGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int REQUEST_ID_LENGTH = 10;

    public static String generateRandomRequestId() {
        Random random = new Random();
        StringBuilder requestId = new StringBuilder();

        for (int i = 0; i < REQUEST_ID_LENGTH; i++) {
            requestId.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));

            if (i == 2 || i == 6) {
                requestId.append("-");
            }
        }

        return requestId.toString();
    }
}
