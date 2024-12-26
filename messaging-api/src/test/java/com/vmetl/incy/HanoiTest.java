package com.vmetl.incy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HanoiTest {

    @Test
    void move() {
        long moves = Hanoi.move(1, 3, 30);
        System.out.println("Total moves:" + moves);
    }
}