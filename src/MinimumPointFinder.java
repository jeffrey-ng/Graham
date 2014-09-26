import java.awt.*;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by jeffreyng on 2014-09-20.
 */
public class MinimumPointFinder {

    private Point[] points;
    private int threadCount;
    List<Thread> threads = new ArrayList<Thread>();


    public MinimumPointFinder(Point[] points, int threadCount) {

        this.points = points;
        this.threadCount = threadCount;
    }

    public Point minimum()
    {
        int perThreadSize = points.length/threadCount;
        Point[][] chunks = new Point[threadCount][];
        Point[] minPoints = new Point[threadCount];
        for (int i=0;i<threadCount;i++)
        {
            int start = i * perThreadSize;
            int length = Math.min(points.length - start, perThreadSize);
            Point[] tmp = new Point[length];
            System.arraycopy(points,start,tmp,0,length);
            chunks[i] = tmp;

            threads.add(new Thread(new MinimumPointFinderThread(chunks[i],minPoints, i), "T" + i));
        }
        for (Thread t: threads)
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
        return findMin(minPoints);
    }

    public static Point findMin(Point[] points)
    {
        Point small = points[0];
        for (int i =0;i<points.length;i++)
        {
            Point temp = points[i];

            if(temp.getY() < small.getY() || ((temp.getY() == small.getY()) && (temp.getX() == small.getX()))) {
                small = temp;

            }
        }
        return small;
    }

    public class MinimumPointFinderThread implements Runnable {
        Point[] chunk;
        Point[] minP;
        int threadID;

        public MinimumPointFinderThread(Point[] chunk, Point[] minP, int tID) {
            this.chunk = chunk;
            this.minP = minP;
            this.threadID = tID;
        }

        public void run()
        {
            minP[threadID] = findMin(chunk);
        }
    }
}
