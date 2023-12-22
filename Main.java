import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main (String[] args) {

        int[] array = {1, 2, 3, 4, 5, 6};
        calculateParallelArraySum(array);
    }


    public static void calculateParallelArraySum(int[] array) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while (array.length > 1) {
            int newIterationArrayLength = (array.length % 2 == 0) ? array.length / 2 : (array.length / 2) + 1;

            int[] tempArray = new int[newIterationArrayLength];
            int[] results = new int[newIterationArrayLength];
            CountDownLatch latch = new CountDownLatch(newIterationArrayLength);

            for (int i = 0; i < newIterationArrayLength; i++) {

                final int index2 = array.length - 1 - i;
                int value = array[i] + ((index2 >= newIterationArrayLength) ? array[index2] : 0);

                int finalIndex = i;

                executorService.execute(() -> {
                    results[finalIndex] = value;
                    latch.countDown();
                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.arraycopy(results, 0, tempArray, 0, newIterationArrayLength);

            System.out.println("проміжний результат обрахунку : " + Arrays.toString(tempArray));
            array = tempArray;
        }

        executorService.shutdown();


        System.out.println("фінальний результат : " + array[0]);
    }

}