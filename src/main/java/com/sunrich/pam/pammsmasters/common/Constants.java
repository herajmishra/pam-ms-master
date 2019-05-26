package com.sunrich.pam.pammsmasters.common;

public class Constants {

    private Constants() {
    }
    public static final String WHITE_SPACE = " ";
    public static final String COLON = ":";

    /**
     * REGEX PATTERNS
     */
    public static class Patterns {
        private Patterns() {
        }

        public static final String GST_NO = "\\d{2}[A-Z]{5}\\d{4}[A-Z]\\d[Z][A-Z\\d]";
        public static final String PAN_NO = "[A-Z]{5}[0-9]{4}[A-Z]";
        public static final String TAN_NO = "[A-Z]{4}\\d{5}[A-Z]";
        public static final String CURRENCY = "[A-Z]{3}";
        public static final String PHONE = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
    }

}
