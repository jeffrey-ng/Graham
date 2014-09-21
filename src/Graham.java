import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by jeffreyng on 2014-09-17.
 */

public class Graham {

    private enum Turn {CLOCKWISE,COUNTER,COLIN};

    public static void main(String[] args) throws InterruptedException{

        int questionNumber = Integer.parseInt(args[0]);
        int problemSize = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);


        //Part 1
        long startTimeRandom=System.currentTimeMillis();

        Object[] generatedVertices = RandomVertices(problemSize,threadCount);
        Point[] generatedPoints = Arrays.copyOf(generatedVertices, generatedVertices.length, Point[].class);
        final long timeRandom = System.currentTimeMillis() - startTimeRandom;

        System.out.println("Time for Random: "+ timeRandom);

        //Part 2
        long startTimeMin=System.currentTimeMillis();

        Point smallPoint = minPoint(generatedPoints,threadCount);
        final long timeMin = System.currentTimeMillis() - startTimeMin;
        System.out.println("Time for Min: "+ timeMin);


        long startTimeSort=System.currentTimeMillis();
        ConcurrentQuickSort q = new ConcurrentQuickSort(generatedPoints,0,generatedPoints.length-1,smallPoint,threadCount);

        Thread t =new Thread(q);
        t.start();
        try
        {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final long timeSort = System.currentTimeMillis() - startTimeSort;
        System.out.println("Time for Sort: "+ timeSort);


        boolean sorted = true;

        for (int i = 0; i < generatedPoints.length - 1; i++) {
            Point a = generatedPoints[i];
            Point b = generatedPoints[i+1];
            if (comparePolarPoints(a,b,smallPoint) == 1) {
                System.out.println("Not sorted");
                sorted = false;
                break;
            }
        }

        if(sorted)System.out.println("Sorted");
        System.out.println(generatedPoints.length);

        Point[] ConvexHullArray = ConvexHull(generatedPoints);

        System.out.println(ConvexHullArray.length);

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
        return points.toArray();
    }

    public static Point minPoint(Point[] points, int threadCount) {


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

        Point small = MinimumPointFinder.findMin(points);
        return small;
    }
    public static int comparePolarPoints(Point a, Point b, Point min)
    {
        try
        {
            if (a==b || a.equals(b))
            {

                return 0;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }



        double tA = Math.atan2(a.getY() - min.getY(), a.getX() - min.getX());
        double tB = Math.atan2(b.getY() - min.getY(), b.getX() - min.getX());

        if (tA < tB)
        {return -1;}
        else if (tA > tB)
        {return 1;}
        else {
            double dA = Math.sqrt(((min.getX() - a.getX()) * (min.getX()- a.getX()))+((min.getY() - a.getY()) * (min.getY()- a.getY())));
            double dB = Math.sqrt(((min.getX() - b.getX()) * (min.getX()- b.getX()))+((min.getY() - b.getY()) * (min.getY()- b.getY())));
            if (dA < dB) return -1;
            else return 1;
        }

    }

    public static Point[] ConvexHull(Point[] sortedPoints)
    {

        //First point is min point
        Stack<Point> s = new Stack<Point>();
        s.push(sortedPoints[0]);
        s.push(sortedPoints[1]);

        for (int i = 2; i < sortedPoints.length;i++)
        {
            Point front = sortedPoints[i];
            Point middle = s.pop();
            Point back = s.peek();

            int t = CrossProduct(back,middle,front);

            //Counter Clockwise
            if (t>0)
            {
                s.push(middle);
                s.push(front);
            }
            //Clockwise
            else if (t < 0)
            {
                i--;
            }
            //Colin
            else
            {
                s.push(front);
            }
        }
        //s.push(sortedPoints[0]);

        Object[] convexArray = s.toArray();

        return Arrays.copyOf(convexArray, convexArray.length, Point[].class);

    }

    private static int CrossProduct(Point a, Point b, Point c){
        //Cross Product
        double cp = ((b.getX() - a.getX()) * (c.getY() - a.getY())) - ((b.getY() - a.getY()) * (c.getX()-a.getX()));
        return (int)cp;

    }
}
