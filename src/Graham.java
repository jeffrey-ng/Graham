import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by jeffreyng on 2014-09-17.
 */

public class Graham {

    public static void main(String[] args) throws InterruptedException{

        int questionNumber = Integer.parseInt(args[0]);
        int problemSize = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);



        Thread[] threads = new Thread[threadCount];
        System.out.println("Hello WOrld");
        Object[] dfdf = RandomVertices(problemSize,threads);

        System.out.println(dfdf.length);

    }


    private static Object[] RandomVertices(int problemSize,Thread[] threads) throws  InterruptedException{
        Set<Point> points = new HashSet<Point>();


        int distributedProblemSize = problemSize/threads.length;
        for (int i = 0; i < threads.length;i++) {
            threads[i] = new Thread(new CoordinateGenerator(points,problemSize),"T"+i);
            threads[i].start();
        }

        for (int i = 0; i < threads.length;i++) {
            threads[i].join();
        }

        return points.toArray();
    }


}
