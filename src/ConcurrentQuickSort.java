import sun.jvm.hotspot.runtime.Threads;

import java.awt.*;
import java.util.*;

/**
 * Created by jeffreyng on 2014-09-21.
 */
public class ConcurrentQuickSort implements Runnable {

    private static int count = 0;

    private Point[] points;
    int leftIndex;
    int rightIndex;
    int minSize = 20;
    Point min;
    int maxThreadCount;
    private static final Object countLock = new Object();
    private static final Object arrayLock = new Object();




    public ConcurrentQuickSort(Point[] points, int left, int right, Point min,int maxThreadCount)
    {
        this.points = points;
        this.leftIndex=left;
        this.rightIndex=right;
        this.min = min;
        this.maxThreadCount=maxThreadCount;

    }

    public void run(){
        sort(points,leftIndex,rightIndex);
    }


    private void sort(Point[] points, int left, int right) {

        if (left >= right || left < 0 || right >= points.length)
        {
            return;
        }
        int pivotIndex = partition(points,left,right);

        //TODO: Splitting into too many threads. Fix.
        if (canSplit())
        {
            java.util.List<Thread> threads = new ArrayList<Thread>();


            ConcurrentQuickSort leftQS = new ConcurrentQuickSort(points,left,pivotIndex,min,maxThreadCount);
            Thread lt = new Thread(leftQS);
            threads.add(lt);

            ConcurrentQuickSort rightQS = new ConcurrentQuickSort(points,pivotIndex+1,right,min,maxThreadCount);
            Thread rt = new Thread(rightQS);
            threads.add(rt);

            incrementCountTwo();


           for(Thread t: threads) {
               t.start();
           }

            try
            {
                for(Thread t: threads) {
                   t.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            sort(points,left,pivotIndex);
            sort(points,pivotIndex+1,right);
        }
    }
    private void swap (int i, int j, Point[] array) {
        synchronized (arrayLock) {
            Point temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private Point getPoint(Point[] array, int i) {
        synchronized (arrayLock) {
            return array[i];
        }
    }
    private void setPoint(Point[] array, int i, Point p) {
        synchronized (arrayLock) {
            array[i] = p;
        }
    }


    private int partition(Point[] points, int i, int j)
    {
        Point pivot = points[i];
        while (i< j)
        {
            while(comparePolarPoints(points[i],pivot) == -1)
            {
                i++;
            }
            while(comparePolarPoints(points[j],pivot) == 1)
            {
                j--;
            }
            swap(i,j,points);
        }
        return i;
    }

    private int comparePolarPoints(Point a, Point b)
    {
        if (a==b || a.equals(b))
        {
            return 0;
        }


        double tA = Math.atan2(a.getY() - min.getY(), a.getX() - min.getX());
        double tB = Math.atan2(b.getY() - min.getY(), b.getX() - min.getX());

        if (tA < tB) return -1;
        else if (tA > tB) return 1;
        else {
            double dA = Math.sqrt(((min.getX() - a.getX()) * (min.getX()- a.getX()))+((min.getY() - a.getY()) * (min.getY()- a.getY())));
            double dB = Math.sqrt(((min.getX() - b.getX()) * (min.getX()- b.getX()))+((min.getY() - b.getY()) * (min.getY()- b.getY())));
            if (dA < dB) return -1;
            else return 1;
        }

    }
    public void incrementCountTwo() {
        synchronized (countLock)
        {
            count+=2;
        }


    }



    public static int getCount() {
        synchronized (countLock)
        {
            return count;

        }
    }
    private  boolean canSplit() {
        synchronized (countLock)
        {
            int currentCount = getCount();
            //System.out.println("current count" + currentCount);
            if (currentCount+2 > maxThreadCount) {


                return false;

            }

            return true;
        }
    }

}
