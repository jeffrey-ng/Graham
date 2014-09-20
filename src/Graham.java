import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by jeffreyng on 2014-09-17.
 */

public class Graham {

    public static void main(String[] args) throws InterruptedException{

        int questionNumber = Integer.parseInt(args[0]);
        int problemSize = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);


        //Part 1
        Object[] generatedVertices = RandomVertices(problemSize,threadCount);

        //Part 2
        Point smallPoint = minPoint(generatedVertices,threadCount);

        System.out.println(generatedVertices.length);

    }


    private static Object[] RandomVertices(int problemSize,int threadCount) throws  InterruptedException{
        Set<Point> points = Collections.synchronizedSet(new HashSet<Point>());
        List<Thread> threads = new ArrayList<Thread>();

        // Split the problem equaly amount the threads
        int perThreadSize = problemSize/threadCount;
        int i;
        for (i =0;i<threadCount;i++) {
            threads.add(new Thread(new CoordinateGenerator(points, perThreadSize, i + ".txt"), "T" + i));
        }


        long startTime=System.currentTimeMillis();
       i=0;
        while (i< threads.size())
        {
            Thread t = threads.get(i);
            t.start();
            i++;
        }

        for (i =0;i<threads.size();i++){
            Thread t = threads.get(i);
            try
            {
                t.join();
            } catch (Exception e) {

            }

        }
        final long time = System.currentTimeMillis() - startTime;
        System.out.println("Total Time: "+time);
        return points.toArray();
    }

    private static Point minPoint(Object[] p, int threadCount) {


        Point[] points = Arrays.copyOf(p,p.length,Point[].class);
        ArrayList<Point> minPoints = new ArrayList<Point>();
        List<Thread> threads = new ArrayList<Thread>();
        int perThreadSize = points.length/threadCount;
        Point[][] chunks = new Point[threadCount][];
        //Split array up.
        int i;
        for ( i = 0; i < threadCount; ++i) {
            int start = i*perThreadSize;
            int length = Math.min(points.length - start, perThreadSize);

            Point[] temp = new Point[length];
            System.arraycopy(points, start,temp,0,length);
            chunks[i] = temp;
        }

        for ( i =0;i<threadCount;i++) {
            threads.add(new Thread(new MinimumPointFinder(chunks[i],minPoints), "T" + i));
        }
        i=0;
        while (i< threads.size())
        {
            Thread t = threads.get(i);
            t.start();
            i++;
        }
        for (i =0;i<threads.size();i++){
            Thread t = threads.get(i);
            try
            {
                t.join();
            } catch (Exception e) {

            }

        }

        Point small = minPoints.get(0);

        for (i =0;i<minPoints.size();i++)
        {
            //TODO: refactor later. Ugly
            if (points[i].getY() < small.getY())
            {
                small = points[i];
            } else if (points[i].getY() == small.getY() &&
                    points[i].getX() < small.getX())
            {
                small = points[i];
            }
        }

        System.out.println(small);
        return small;
    }

}
