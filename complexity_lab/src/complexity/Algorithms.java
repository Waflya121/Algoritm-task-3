package complexity;

import java.util.Arrays;
import java.util.Random;

public class Algorithms {

    // ---------- 1. Доступ по индексу ----------
    // Гипотеза: O(1)
    public static int accessByIndex(int[] arr, int[] indices) {
        int sum = 0;
        for (int idx : indices) {
            sum += arr[idx]; // само измеряемое выражение
        }
        return sum;
    }

    // ---------- 2. Бинарный поиск ----------
    // Гипотеза: O(log N)
    public static int binarySearch(int[] arr, int target) {
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    // ---------- 3. Линейный поиск ----------
    // Гипотеза: O(N), в среднем N/2, худший — N
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // ---------- 4. Подсчёт элементов по условию ----------
    // Гипотеза: O(N) — всегда проходим весь массив
    public static int countGreater(int[] arr, int target) {
        int count = 0;
        for (int v : arr) {
            if (v > target) count++;
        }
        return count;
    }

    // ---------- 5. Самостоятельно: поиск максимума ----------
    // Гипотеза: O(N). Лучший/средний/худший случай по числу сравнений — всегда N-1,
    // но ветвление и кэш могут влиять. Проверим на трёх конфигурациях данных.
    public static int findMax(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }

    // ---------- Генераторы данных ----------

    public static int[] generateRandom(int n, Random rnd) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        return a;
    }

    public static int[] generateSorted(int n, Random rnd) {
        int[] a = generateRandom(n, rnd);
        Arrays.sort(a);
        return a;
    }

    // Возрастающий массив — лучший случай для поиска максимума
    public static int[] generateAscending(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    // Убывающий массив — худший случай (каждый раз обновляем max)
    public static int[] generateDescending(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        return a;
    }

    // Случайный — средний случай
    public static int[] generateRandomForMax(int n, Random rnd) {
        return generateRandom(n, rnd);
    }
}