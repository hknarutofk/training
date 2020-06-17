package com.example.demo;

import java.io.IOException;

/**
 * @author yeqiang
 * @since 6/16/20 4:39 PM
 */
public class ClearConsoleTest {
    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 100; i++) {
            System.out.println("fsdfljdsfjlkdsfjlksajflkdsjlfdsjlfdslkfdlsjk");
        }
        System.out.print("\033\143");

    }
}
