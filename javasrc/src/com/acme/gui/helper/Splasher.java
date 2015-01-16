package com.acme.gui.helper;

/**
 * Unused.
 */

public class Splasher {
    public static void main(String[] args) {
        SplashWindow.splash(Splasher.class.getResource("splash.gif"));
        SplashWindow.invokeMain("SubScapeGUI", args);
        SplashWindow.disposeSplash();
    }
}
