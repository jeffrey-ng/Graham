import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
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
    String file;
    volatile boolean finished = false;
    private static final Object countLock = new Object();


    public CoordinateGenerator (Set<Point> points, int problemSize, String filename) {
        this.problemSize = problemSize;
        this.points = points;
        position = new Random();
        file = filename;
    }


    public void run(){
      ArrayList<Point> pointss = new ArrayList<Point>();
      for (int i = 0; i < problemSize;i++) {
          Point test = new Point();
          test.x = position.nextInt();
          test.y = position.nextInt();
          pointss.add(test);
          //insertSet(test);
      }
        for (int i =0;i<pointss.size();i++)
        {
            insertSet(pointss.get(i));
        }
//        try
//        {
//            PrintWriter print = new PrintWriter(new File(file));
//            print.println(pointss.size());
//            for (int i =0;i<pointss.size();i++)
//            {
//                print.println(pointss.get(i));
//            }
//            print.close();
//        } catch (Exception e)
//        {
//
//        }

    }


    private void insertSet(Point p) {
        synchronized (countLock) {
//            if (count < problemSize) {
//                count++;
//                points.add(p);
////                System.out.println("count:" + count);
//            } else {
//                finished = true;
//            }
            points.add(p);
        }
    }

    public  synchronized void incrementCount() {
        if (count < problemSize) {
            count++;
        } else {
            finished = true;
        }

    }

    public static synchronized int getCount() {
        return count;

    }

}
