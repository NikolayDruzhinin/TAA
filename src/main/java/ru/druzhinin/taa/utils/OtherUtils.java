package ru.druzhinin.taa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OtherUtils {
    private static final Logger logger = LogManager.getLogger(OtherUtils.class);

    public static final String DEPRECATED_ELEMENT = "|DEPRECATED|";
    public static final boolean DETECT_IDEA_LAUNCH = detectIdea();

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("InterruptedException in method OtherUtils.safeSleep(...)", e);
        }
    }

    public static double parseRusDouble(String s) throws NumberFormatException
    {
        if(s == null || s.isEmpty()) {
            throw new NumberFormatException();
        }

        s = s.replaceAll(",", ".");
        s = s.trim();

        return Double.parseDouble(s);
    }

    public static boolean detectIdea()
    {
        try {
            Class.forName("com.intellij.rt.execution.application.AppMainV2");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}