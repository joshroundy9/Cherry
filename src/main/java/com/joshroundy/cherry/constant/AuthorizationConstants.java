package com.joshroundy.cherry.constant;

public class AuthorizationConstants {
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{3,12}$";
    public static final String USERNAME_ERROR_MESSAGE = "Username must be alphanumeric and less than 12 characters";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String EMAIL_ERROR_MESSAGE = "Email must be a valid email address";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    public static final String PASSWORD_ERROR_MESSAGE = "Password must be at least 8 characters long and include uppercase and lowercase letter, a number, and a special character";
    public static final String WEIGHT_ERROR_MESSAGE = "Weight must be a positive number less than 1000";
    public static final int WEIGHT_MAX = 1000;
}
