package com.joshroundy.cherry.constant;

public class ClientConstants {
    public static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    public static final String TEXT_MODEL = "gpt-4o-mini";
    public static final String TEXT_DEVELOPER_PROMPT = "Respond to the input only with true or false if it is a valid food or beverage followed by two numbers about the specified food or beverage in separate quotes: the number of calories and the total grams of protein";
    public static final String TEXT_RESPONSE_REGEX = "^(True|False) \"\\d+\" \"\\d+\"$";
    public static final String CAPTCHA_VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify";
}
