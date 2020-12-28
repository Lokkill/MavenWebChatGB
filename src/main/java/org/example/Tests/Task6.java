package org.example.Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Task6 {


    @Test
    public void test1_1(){
        int[] arr = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        Assertions.assertArrayEquals(new int[]{1, 7}, numbersAfterValue(arr, 4));
    }

    @Test
    public void test1_2() {
        int[] arr2 = {1, 2, 3, 4, 5, 6, 7, 8};
        Assertions.assertArrayEquals(new int[]{5, 6, 7, 8}, numbersAfterValue(arr2, 4));
    }

    @Test
    public void test1_3() {
        int[] arr3 = {};
        Assertions.assertArrayEquals(new int[]{1, 7}, numbersAfterValue(arr3, 5));
    }

    public static int[] numbersAfterValue(int[] arr, int value) {
        if (arr.length == 0) throw new NullPointerException();;
        if (Arrays.binarySearch(arr, value) > 0) {
            ArrayList<Integer> promList = new ArrayList<>();
            for (int i = arr.length - 1; i >= 0; i--) {
                if (arr[i] == value) {
                    return promList.stream().mapToInt(v -> v).toArray();
                } else {
                    promList.add(0, arr[i]);
                }
            }
        } else {
            throw new RuntimeException();
        }
        return null;
    }

    @Test
    public void test2_1(){
        int[] arr1 = {4,1,4,1,4,1};
        Assertions.assertTrue(onlyTwoNumbers(arr1, 1, 4));
    }

    @Test
    public void test2_2() {
        int[] arr2 = {1,1,1,2,4,4,4};
        Assertions.assertTrue(onlyTwoNumbers(arr2, 1, 4));
    }

    @Test
    public void test2_3() {
        int[] arr3 = {1,1,1,1,1};
        Assertions.assertTrue(onlyTwoNumbers(arr3, 1, 4));
    }

    public boolean onlyTwoNumbers(int[] arr, int num1, int num2){
        if (arr.length == 0) return false;
        HashSet<Integer> list = new HashSet<>();
        Arrays.stream(arr).forEach(list::add);
        if (list.size() == 2){
            return list.stream().filter(x -> x == num1 || x == num2).count() == 2;
        } else {
            return false;
        }
    }
}
