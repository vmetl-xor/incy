package com.vmetl.incy;

public class Hanoi {

    private static long counter;

    public static long move(int from, int to, int amount) {
        if (amount == 1) {
            counter++;
//            if (counter % 1_000_000 == 0) {
//                System.out.println(counter);
//            }
//            System.out.println(from + " -> " + to);
        } else {
            int intermediate = 6 - from - to;

            move(from, intermediate, amount - 1);
            move(from, to, 1);
            move(intermediate, to, amount - 1);
        }

        return counter;
    }

}
