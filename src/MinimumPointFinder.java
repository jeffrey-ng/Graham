import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jeffreyng on 2014-09-20.
 */
public class MinimumPointFinder implements Runnable{

    private Point[] points;
    private ArrayList<Point> minPoints;


    public MinimumPointFinder(Point[] points, ArrayList minPoints) {

        this.points = points;
        this.minPoints = minPoints;
    }

    public void run(){
        Point min = findMin(points);
        listAdd(min);
    }

    public synchronized void listAdd(Point p) {
        minPoints.add(p);

    }

    private Point findMin(Point[] points)
    {
        Point small = points[0];
        for (int i =0;i<points.length;i++)
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

        return small;

    }
}