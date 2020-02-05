/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.utilities;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author Supriya
 */
public class Activate {

    public interface User32 extends W32APIOptions {

        User32 instance = (User32) Native.loadLibrary("user32", User32.class,
                DEFAULT_OPTIONS);

        boolean ShowWindow(HWND hWnd, int nCmdShow);

        boolean SetForegroundWindow(HWND hWnd);

        HWND FindWindow(String winClass, String title);

        int SW_SHOW = 1;

    }

    public static HWND getWindowHandle(String title) {
        User32 user32 = User32.instance;
        HWND hWnd = user32.FindWindow(null, title); // Sets focus to my opened 'Downloads' folder
        return hWnd;
    }

    public static void showWindow(HWND hWnd) {
        User32 user32 = User32.instance;

        user32.ShowWindow(hWnd, User32.SW_SHOW);
        user32.SetForegroundWindow(hWnd);
    }

//    public static void main(String[] args) throws InterruptedException {
//        User32 user32 = User32.instance;
//        HWND hWnd = user32.FindWindow(null, "CAD /03 (1403 - DEMO1403 ANKIND)"); // Sets focus to my opened 'Downloads' folder
//        user32.ShowWindow(hWnd, User32.SW_SHOW);
//        user32.SetForegroundWindow(hWnd);
//        Thread.sleep(2000);
//        hWnd = user32.FindWindow(null, "COPPER /03 (1403 - DEMO1403 ANKIND)"); // Sets focus to my opened 'Downloads' folder
//        user32.ShowWindow(hWnd, User32.SW_SHOW);
//        user32.SetForegroundWindow(hWnd);
//        Thread.sleep(2000);
//        hWnd = user32.FindWindow(null, "GOLD /02 (1403 - DEMO1403 ANKIND)"); // Sets focus to my opened 'Downloads' folder
//        user32.ShowWindow(hWnd, User32.SW_SHOW);
//        user32.SetForegroundWindow(hWnd);
//        Thread.sleep(2000);
//
//    }
}
