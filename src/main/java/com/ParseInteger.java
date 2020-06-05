package com;

public class ParseInteger {

    public static Integer parse(String input) {
        return Integer.parseInt(input);
    }

    public static Long parseLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void log(String content) {
        System.out.println(content);
    }

    public static int convert(int raw) {
        return 1 + raw;
    }

    public static boolean convert(float raw) {
        return raw == 1;
    }

    public static char convert(String input) {
        return 'a';
    }
}
