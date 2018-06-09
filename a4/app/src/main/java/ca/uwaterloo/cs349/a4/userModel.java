package ca.uwaterloo.cs349.a4;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by yuyuxiao on 2018-03-26.
 */

public class userModel {
    private static String name;
    private static int qNum;  // question number chosen

    public static int qCurrent;
    public static boolean[] result1 = new boolean[5];

    public static int ans1 = 0;
    public static boolean[] ans2 = new boolean[4];
    public static int ans3 = 0;
    public static int ans4 = 0;
    public static boolean[] ans5 = new boolean[4];

    private userModel() {
        //result1 = new boolean[5];
    }

    public static String getName() {
        return name;
    }

    public static void setName(String update) {
        name = update;
    }

    public static int getqNum() {return qNum;}

    public static void setqNum(int update) { qNum = update; }

    public static void clear() {
        name = null;
        result1 = new boolean[5];
        qNum = 0;
        qCurrent = 0;

        ans1 = 0;
        ans2 = new boolean[4];
        ans3 = 0;
        ans4 = 0;
        ans5 = new boolean[4];

    }

    public static int getFinalResult() {
        int finalResult = 0;
        for (boolean r: result1) {
            if (r) {
                ++finalResult;
            }
        }
        return finalResult;
    }
}
