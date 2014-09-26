import java.awt.*;

/**
 * Created by jeffreyng on 2014-09-17.
 */

public class Graham {

    public static void main(String[] args) throws InterruptedException{

        int questionNumber = Integer.parseInt(args[0]);
        int problemSize = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);
        final long startTimeRandom;
        final long startTimeMin;
        final long startTimeSort;
        final long startTimeTotal;


        final long timeRandom;
        final long timeMin;
        final long timeSort;
        final long timeTotal;

        final long FIXEDTIMECOST=0;

        //Part 1
        startTimeRandom=System.currentTimeMillis();
        Point[] generatedPoints = RandomVertices(problemSize,threadCount);
        timeRandom = System.currentTimeMillis() - startTimeRandom;


        //Part 2
        startTimeMin=System.currentTimeMillis();
        Point smallPoint = minPoint(generatedPoints,threadCount);
        timeMin = System.currentTimeMillis() - startTimeMin;

        //Part 3
        startTimeSort=System.currentTimeMillis();
        Point[] sortedPoints = polarSort(generatedPoints,threadCount,smallPoint);
        timeSort = System.currentTimeMillis() - startTimeSort;

        //Part 4
        Point[] ConvexHullArray = ConvexHull(sortedPoints);

        timeTotal = timeRandom + timeMin + timeSort + FIXEDTIMECOST;
        System.out.println("~~Problem Size: " + problemSize + " #Threads: "+ threadCount + " ConvexHull size: " + ConvexHullArray.length);
        switch (questionNumber) {
            case 1:
                System.out.println("~~Time for Random Point Generation: "+ timeRandom+"ms");
                break;
            case 2:
                System.out.println("~~Time for Finding Minimum Point: "+ timeMin+"ms");
                break;
            case 3:
                System.out.println("~~Time for Sorting based on angular values: "+ timeSort +"ms");
                break;
            case 4:

                System.out.println("~~Total Time: " + timeTotal+"ms");
                break;

        }



    }


    private static Point[] RandomVertices(int problemSize,int threadCount){

        CoordinateGenerator c = new CoordinateGenerator(problemSize,threadCount);
        Point[] generatedPoints = c.generateRandom();
        if (generatedPoints.length != problemSize) {
            System.out.println("Random Array of incorrect size");
        }
        //Safety check for empty points
//        for(int i=0;i<generatedPoints.length;i++){
//            if (generatedPoints[i]==null) {
//                System.out.println("Empty cell in Random Array");
//            }
//        }

        return generatedPoints;

    }

    public static Point minPoint(Point[] points, int threadCount) {

        MinimumPointFinder m = new MinimumPointFinder(points,threadCount);
        return m.minimum();
    }

    public static Point[] polarSort(Point[] points, int threadCount, Point minPoint) {
        ConcurrentQuickSort qs = new ConcurrentQuickSort(points,threadCount,minPoint);
        return qs.QSort();
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
       return GrahamScan.getHull(sortedPoints);
    }


}
