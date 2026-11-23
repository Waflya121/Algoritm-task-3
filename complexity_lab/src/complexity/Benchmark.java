package complexity;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Benchmark {

    // Размеры из задания
    static final int[] SIZES = {
            10, 100, 1_000, 2_000, 4_000, 8_000,
            16_000, 32_000, 64_000, 128_000
    };

    static final int WARMUP_RUNS = 3;
    static final int MEASURE_RUNS = 5;

    interface Measurable {
        Object run();
    }

    /**
     * Измеряет время выполнения задачи:
     * 1) прогрев (WARMUP_RUNS),
     * 2) MEASURE_RUNS замеров через System.nanoTime(),
     * 3) возврат массива отдельных значений и среднего.
     */
    static double[] measure(Measurable task) {
        // Прогрев
        for (int i = 0; i < WARMUP_RUNS; i++) task.run();

        double[] times = new double[MEASURE_RUNS];
        for (int i = 0; i < MEASURE_RUNS; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            times[i] = (end - start) / 1_000_000.0; // мс
        }
        return times;
    }

    static double avg(double[] a) {
        double s = 0;
        for (double v : a) s += v;
        return s / a.length;
    }

    static String fmt(double[] a) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) {
            sb.append(String.format(Locale.US, "%.4f", a[i]));
            if (i < a.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    static void printHeader(String title) {
        System.out.println();
        System.out.println("==================================================================");
        System.out.println(title);
        System.out.println("==================================================================");
        System.out.printf("%-10s | %-45s | %-10s%n", "N", "Отдельные значения (мс)", "Среднее (мс)");
        System.out.println("------------------------------------------------------------------");
    }

    static void printRow(int n, double[] times) {
        System.out.printf("%-10d | %-45s | %.4f%n", n, fmt(times), avg(times));
    }

    public static void main(String[] args) {
        Random rnd = new Random(42);

        // ============================================================
        // Задание 1. Доступ по индексу — O(1)
        // ============================================================
        printHeader("Задание 1. Доступ к элементу массива по индексу (гипотеза O(1))");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            // Подготовим последовательность индексов заранее, чтобы не измерять генерацию
            int[] indices = new int[n];
            for (int i = 0; i < n; i++) indices[i] = rnd.nextInt(n);

            double[] times = measure(() -> Algorithms.accessByIndex(arr, indices));
            printRow(n, times);
        }

        // ============================================================
        // Задание 2. Бинарный поиск — O(log N)
        // Три случая: первый элемент, случайный, отсутствующий
        // ============================================================
        printHeader("Задание 2. Бинарный поиск (гипотеза O(log N)) — первый элемент");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateSorted(n, rnd);
            int target = arr[0];
            double[] times = measure(() -> Algorithms.binarySearch(arr, target));
            printRow(n, times);
        }

        printHeader("Задание 2. Бинарный поиск — случайный элемент");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateSorted(n, rnd);
            int target = arr[rnd.nextInt(n)];
            double[] times = measure(() -> Algorithms.binarySearch(arr, target));
            printRow(n, times);
        }

        printHeader("Задание 2. Бинарный поиск — отсутствующий элемент");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateSorted(n, rnd);
            int target = arr[n - 1] + 1; // заведомо больше максимума
            double[] times = measure(() -> Algorithms.binarySearch(arr, target));
            printRow(n, times);
        }

        // ============================================================
        // Задание 3. Линейный поиск — O(N)
        // ============================================================
        printHeader("Задание 3. Линейный поиск (гипотеза O(N)) — первый элемент (лучший)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            int target = arr[0];
            double[] times = measure(() -> Algorithms.linearSearch(arr, target));
            printRow(n, times);
        }

        printHeader("Задание 3. Линейный поиск — случайный элемент (средний)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            int target = arr[rnd.nextInt(n)];
            double[] times = measure(() -> Algorithms.linearSearch(arr, target));
            printRow(n, times);
        }

        printHeader("Задание 3. Линейный поиск — отсутствующий элемент (худший)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            int target = Integer.MIN_VALUE; // почти наверняка отсутствует
            double[] times = measure(() -> Algorithms.linearSearch(arr, target));
            printRow(n, times);
        }

        // ============================================================
        // Задание 4. Подсчёт элементов, превышающих target — O(N)
        // ============================================================
        printHeader("Задание 4. Подсчёт элементов > target — все соответствуют (target = MIN)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            int target = Integer.MIN_VALUE;
            double[] times = measure(() -> Algorithms.countGreater(arr, target));
            printRow(n, times);
        }

        printHeader("Задание 4. Подсчёт элементов > target — случайное количество");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            int target = arr[rnd.nextInt(n)];
            double[] times = measure(() -> Algorithms.countGreater(arr, target));
            printRow(n, times);
        }

        printHeader("Задание 4. Подсчёт элементов > target — никто не соответствует (target = MAX)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandom(n, rnd);
            int target = Integer.MAX_VALUE;
            double[] times = measure(() -> Algorithms.countGreater(arr, target));
            printRow(n, times);
        }

        // ============================================================
        // Задание 5. Самостоятельно: поиск максимума — O(N)
        // ============================================================
        printHeader("Задание 5. Поиск максимума — возрастающий массив (лучший)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateAscending(n);
            double[] times = measure(() -> Algorithms.findMax(arr));
            printRow(n, times);
        }

        printHeader("Задание 5. Поиск максимума — случайный массив (средний)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateRandomForMax(n, rnd);
            double[] times = measure(() -> Algorithms.findMax(arr));
            printRow(n, times);
        }

        printHeader("Задание 5. Поиск максимума — убывающий массив (худший)");
        for (int n : SIZES) {
            int[] arr = Algorithms.generateDescending(n);
            double[] times = measure(() -> Algorithms.findMax(arr));
            printRow(n, times);
        }

        System.out.println();
        System.out.println("Готово. Скопируйте таблицы в Excel для построения графиков.");
    }
}