package com.kunleen.sn.sportnewsapplication;

import android.graphics.Point;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        testargs.calu();
    }
}

class testargs {
    private static int[] array = {8, 7, 6, 5, 9,2,1};
    static int arr1length = 0;
    static int arr2length = 0;

    static int[] arr1;
    static int[] arr2;

    public static void calu() {
        arr1length = array.length % 2 == 0 ? array.length / 2 : array.length - 1 / 2;
        arr2length = array.length % 2 == 0 ? array.length / 2 : array.length + 1 / 2;
        arr1 = new int[arr1length];
        arr2 = new int[arr2length];
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        int s1 = 0;
        int k1 = 0;
        int s2 = 0;
        int k2 = 0;
        int k3 = 0;
        System.arraycopy(array, 0, arr1, 0, arr1length);
        System.arraycopy(array, array.length - arr1length, arr2, 0, arr2length);
        for (int i = 0; i < arr1length; i++) {
            for (int j = i + 1; j < arr1length; j++) {
                s1 = arr1[i] - arr1[j];
                if (s1 - k1 > 0) {
                    k1 = s1;
                    p1.x = arr1[i];
                    p1.y = arr1[j];
                }
            }
        }
        for (int a = 0; a < arr2length; a++) {
            for (int b = a + 1; b < arr2length; b++) {
                s2 = arr2[a] - arr2[b];
                if (s2 - k2 > 0) {
                    k2 = s2;
                    p2.x = arr2[a];
                    p2.y = arr2[b];
                }
            }
        }
        p3.x = p1.x - p1.y >= 0 ? p1.x : p1.y;
        p3.y = p2.x - p1.y >= 0 ? p2.y : p2.x;
        k3 = p3.x - p3.y;
        int max = k1;
        if (k2 - k1 >= 0) {
            max = k2;
        }
        if (k3 - max >= 0) {
            max = k3;
        }
        if (max == k1) {
            System.out.println("x=" + p1.x + "y=" + p1.y);
        } else if (max == k2) {
            System.out.println("x=" + p2.x + "y=" + p2.y);
        } else if (max == k3) {
            System.out.println("x=" + p3.x + "y=" + p3.y);
        }
    }
}