import java.awt.*;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by jeffreyng on 2014-09-25.
 */
public class GrahamScan {

    public static Point[] getHull(Point[] sortedPoints) {

        //First point is min point
        Stack<Point> s = new Stack<Point>();
        s.push(sortedPoints[0]);
        s.push(sortedPoints[1]);

        for (int i = 2; i < sortedPoints.length; i++) {
            Point front = sortedPoints[i];
            Point middle = s.pop();
            Point back = s.peek();

            int t = CrossProduct(back, middle, front);

            //Counter Clockwise
            if (t > 0) {
                s.push(middle);
                s.push(front);
            }
            //Clockwise
            else if (t < 0) {
                i--;
            }
            //Colin
            else {
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
