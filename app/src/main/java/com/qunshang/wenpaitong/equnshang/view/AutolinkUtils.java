package com.qunshang.wenpaitong.equnshang.view;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import com.qunshang.wenpaitong.equnshang.data.RegexParser;

class AutolinkUtils {

    private static boolean isValidRegex(String regex){
        try {
            URL url = new URL("http:baidu.com");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return regex != null && !regex.isEmpty() && regex.length() > 2;
    }

    static String getRegexByAutoLinkMode(AutoLinkMode anAutoLinkMode, String customRegex) {
        switch (anAutoLinkMode) {
            case MODE_HASHTAG:
                return RegexParser.HASHTAG_PATTERN;
            case MODE_MENTION:
                return RegexParser.MENTION_PATTERN;
            case MODE_URL:
                return RegexParser.URL_PATTERN;
            case MODE_PHONE:
                return RegexParser.PHONE_PATTERN;
            case MODE_EMAIL:
                return RegexParser.EMAIL_PATTERN;
            case MODE_CUSTOM:
                if (!AutolinkUtils.isValidRegex(customRegex)) {
                    Log.e(AutoLinkTextView.TAG, "Your custom regex is null, returning URL_PATTERN");
                    return RegexParser.URL_PATTERN;
                } else {
                    return customRegex;
                }
            default:
                return RegexParser.URL_PATTERN;
        }
    }

}
