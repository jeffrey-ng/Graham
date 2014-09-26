import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jeffreyng on 2014-09-25.
 */
public class ConcurrentQuickSort {
    private static volatile int count = 0;
    private static final Object countLock = new Object();
    private static final Object arrayLock = new Object();
    private static int maxThreadCount;
    private Point[] points;
    private Point minPoint;


    public ConcurrentQuickSort(Point[] points,int ThreadCount, Point min) {
        this.points = points;
        this.maxThreadCount = ThreadCount;
        this.minPoint = min;
    }

    public Point[] QSort()
    {
        int start = 0;
        int end = points.length-1;
        ConcurrentQuickSortThread q = new ConcurrentQuickSortThread(start,end);

        Thread t =new Thread(q);
        t.start();
        try
        {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!isSorted(points)) {
            System.out.println("Array is not sorted");
        }
        return points;
    }

    private boolean isSorted(Point[] points)
    {
        boolean sorted = true;
        for (int i = 0; i < points.length - 1; i++) {
            Point a = points[i];
            Point b = points[i+1];
            if (comparePolarPoints(a,b,minPoint) == 1) {
                sorted = false;
                break;
            }
        }
        return sorted;
    }


    private int comparePolarPoints(Point a, Point b, Point c)
    {
        try {
            if (a==b || a.equals(b))
            {
                return 0;
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }



        double tA = Math.atan2(a.getY() - c.getY(), a.getX() - c.getX());
        double tB = Math.atan2(b.getY() - c.getY(), b.getX() - c.getX());

        if (tA < tB) return -1;
        else if (tA > tB) return 1;
        else {
            double dA = Math.sqrt(((c.getX() - a.getX()) * (c.getX()- a.getX()))+((c.getY() - a.getY()) * (c.getY()- a.getY())));
            double dB = Math.sqrt(((c.getX() - b.getX()) * (c.getX()- b.getX()))+((c.getY() - b.getY()) * (c.getY()- b.getY())));
            if (dA < dB) return -1;
            else return 1;
        }

    }

    public class ConcurrentQuickSortThread implements Runnable {

        volatile int leftIndex;
        volatile int rightIndex;


        public ConcurrentQuickSortThread( int left, int right)
        {
            //this.points = points;
            this.leftIndex=left;
            this.rightIndex=right;

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


                ConcurrentQuickSortThread leftQS = new ConcurrentQuickSortThread(left,pivotIndex);
                Thread lt = new Thread(leftQS);
                threads.add(lt);

                ConcurrentQuickSortThread rightQS = new ConcurrentQuickSortThread(pivotIndex+1,right);
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

        private int partition(Point[] points, int i, int j)
        {
            Point pivot = points[i];
            while (i< j)
            {
                while(comparePolarPoints(points[i],pivot, minPoint) == -1)
                {
                    i++;
                }
                while(comparePolarPoints(points[j],pivot, minPoint) == 1)
                {
                    j--;
                }
                swap(i,j,points);
            }
            return i;
        }


        public void incrementCountTwo() {
            synchronized (countLock)
            {
                count+=2;
            }


        }

        public int getCount() {
            synchronized (countLock)
            {
                return count;

            }
        }
        private  boolean canSplit() {
            synchronized (countLock)
            {
                int currentCount = getCount();
                //  System.out.println("current count" + currentCount);
                if (currentCount+2 > maxThreadCount) {


                    return false;

                }

                return true;
            }
        }

    }

}
