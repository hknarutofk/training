package com.example.demo.util;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yeqiang
 * @since 9/23/20 4:35 PM
 */
@Slf4j
public class SortUtil {
    static int counter = 0;

    static void fastSort(int[] array, int l, int r) {
        if (l >= r) {
            return;
        }
        log.debug("l={}, r={}", l, r);
        printArray(array, l, r);
        int left = l, right = r;
        int key = array[left];
        int emptyIndex = left;
        log.debug("index={}, key={}", emptyIndex, key);
        while (left < right) {
            // 从右向左扫描，找到一个比基准数小或相等的值
            for (; right > left; right--) {
                if (array[right] <= key) {
                    // 找到，替换掉左边游标指向的数值
                    array[left] = array[right];
                    emptyIndex = right;
                    printArray(array, l, r);
                    for (int i = 0; i < left; i++) {
                        System.out.print("   ");
                    }
                    System.out.println("^");
                    for (int i = 0; i < right; i++) {
                        System.out.print("   ");
                    }
                    System.out.println("-");
                    // 需要从左向右扫描，找到比基准数大的值填充右边空位
                    for (; left < right; left++) {
                        counter++;
                        if (array[left] > key) {
                            array[right] = array[left];
                            emptyIndex = left;
                            printArray(array, l, r);
                            for (int i = 0; i < left; i++) {
                                System.out.print("   ");
                            }
                            System.out.println("-");
                            for (int i = 0; i < right; i++) {
                                System.out.print("   ");
                            }
                            System.out.println("^");
                            break;
                        }
                    }
                }
            }

        }
        array[emptyIndex] = key;
        printArray(array, l, r);
        for (int i = 0; i < emptyIndex; i++) {
            System.out.print("   ");
        }
        System.out.println("K" + emptyIndex);
        System.out.println("===========");
        // 左边数组 范围ll lr, 右边数组范围rl rr
        int ll = l;
        int lr = emptyIndex - 1;
        int rl = emptyIndex + 1;
        int rr = r;
        if (lr - ll > 0) {
            fastSort(array, ll, lr);
        }
        if (rr - rl > 0) {
            fastSort(array, rl, rr);
        }

    }

    static void printArray(int[] array, int left, int right) {
        for (int i = 0; i < array.length; i++) {
            if (left == i || right == i) {
                System.out.print("\033[32m" + array[i] + "\033[0m ");
            } else {
                System.out.print(array[i] + " ");
            }
        }
        System.out.println();
    }

    static void bubble_sort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                counter++;
                if (array[i] > array[j]) {
                    int tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                    // 交换一次，打印一次
                    printArray(array, i, j);
                }
            }
        }
    }

    public static void main(String[] args) {
        SortUtil.counter = 0;
        Random random = new Random();
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(89) + 10;
        }
        int[] array2 = array.clone();
        printArray(array, 0, array.length - 1);
        fastSort(array, 0, array.length - 1);
        printArray(array, 0, array.length - 1);

        // check
        for (int i = 0; i < array.length; i++) {
            if (i <= array.length - 2 && array[i] > array[i + 1]) {
                System.out.print("\033[33m" + array[i] + "\033[0m ");
            } else {
                System.out.print(array[i] + " ");
            }
        }
        System.out.println("");
        System.out.println("counter=" + counter);

        System.out.println("---------------");

        counter = 0;
        printArray(array2, 0, array2.length - 1);
        bubble_sort(array2);
        printArray(array2, 0, array.length - 1);

        // check
        for (int i = 0; i < array2.length; i++) {
            if (i <= array2.length - 2 && array2[i] > array2[i + 1]) {
                System.out.print("\033[33m" + array2[i] + "\033[0m ");
            } else {
                System.out.print(array2[i] + " ");
            }
        }
        System.out.println("");
        System.out.println("counter=" + counter);
    }
}
