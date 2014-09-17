import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by jeffreyng on 2014-09-17.
 */
public class CoordinateGenerator implements Runnable {


    private Set<Point> points;
    private static int count = 0;
    private int problemSize;
    Random position;
    volatile boolean finished = false;
    private static final Object countLock = new Object();


    public CoordinateGenerator (Set<Point> points, int problemSize) {
        this.problemSize = problemSize;
        this.points = points;
        position = new Random();
    }

    public void stopThread() {
        finished = true;
    }

    public void run(){

            while (!finished) {
                Thread thread = Thread.currentThread();
                System.out.println("ThreadName: "+ thread.getName());
                Point test = new Point();
                test.x = position.nextInt();
                test.y = position.nextInt();
                insertSet(test);
                //incrementCount();

            }

        return;
    }


    private void insertSet(Point p) {
        synchronized (countLock) {
            if (count < problemSize) {
                count++;
                points.add(p);
                System.out.println("count:" + count);
            } else {
                finished = true;
            }
        }
    }

    public static synchronized void incrementCount() {
        count++;
    }

    public static synchronized int getCount() {
        return count;

    }

}
