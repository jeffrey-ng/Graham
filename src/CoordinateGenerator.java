import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeffreyng on 2014-09-17.
 */
public class CoordinateGenerator {


    public Map<Point, Boolean> points = new ConcurrentHashMap<Point, Boolean>();
    public Point[] generatedPoints;
    List<Thread> threads = new ArrayList<Thread>();
    private int problemSize;
    private int threadCount;
    Random position;



    public CoordinateGenerator(int problemSize, int threadCount)
    {
        this.problemSize = problemSize;
        this.threadCount = threadCount;
        this.generatedPoints = new Point[problemSize];
    }

    public Point[] generateRandom()
    {
        position = new Random();
        int padding = 2;
        final Random[] rand = new Random[threadCount * padding];
        for (int i =0;i<threadCount*padding;++i)
        {
            rand[i]=new Random();
        }

        points.clear();
        int perThreadSize = problemSize / threadCount;
        int perThreadRemainder = problemSize % threadCount;
        int last = 0;
        for (int i = 0; i < threadCount; i++) {
            int incrementSize = (i < perThreadRemainder) ? perThreadSize + 1 : perThreadSize;
            int start = last;
            int end = start + incrementSize;
           // System.out.println("start: " + start + " end: " + end + " Thread: " + i);
            threads.add(new Thread(new CoordinateGeneratorThread(start, end,rand[i]), "T" + i));
            last += incrementSize;
        }


        for (Thread t : threads)
        {
            t.start();
        }
        for (Thread t : threads)
        {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return generatedPoints;
    }


    public class CoordinateGeneratorThread implements Runnable {
        int start;
        int end;
        Random rnd;

        public CoordinateGeneratorThread(int start, int end, Random rnd)
        {
            this.start = start;
            this.end = end;
            this.rnd = rnd;
        }

        public void run()
        {
            long s = System.currentTimeMillis();
            for (int i = start; i < end; i++) {
                while (true) {
                    Point test = new Point();
                    test.x = rnd.nextInt();
                    test.y = rnd.nextInt();
                    if (points.put(test, true) == null) {
                        generatedPoints[i] = test;
                        break;
                    }
                }
            }
            long e = System.currentTimeMillis() - s;
           // System.out.println("mytime: "+ e + " start: " + start + " end: "+ end);
        }
    }
}

