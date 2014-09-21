import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;

public class GrahamTest extends TestCase {

    @Test
    public void testMain() throws Exception {
        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);

        Point[] points = new Point[]{a, b, c, d, e, f, g, h, i};


//        Point[] points = new Point[]{a, b, c, d, e, f, g};
        ConcurrentQuickSort q = new ConcurrentQuickSort(points,0,points.length-1,f,2);

        Thread t =new Thread(q);
        t.start();
        try
        {
            t.join();
        } catch (InterruptedException exc) {
        }

        Point[] convex = Graham.ConvexHull(points);

        assertEquals(convex.length, 4);

        assertEquals(convex[0], f);
        assertEquals(convex[1], i);
        assertEquals(convex[2], d);
        assertEquals(convex[3], a);
    }

    @Test
    public void testMinPoint() throws Exception {

        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);
        Point[] points = new Point[]{a, b, c, d, e, f, g, h, i};


        Point lowest1 = Graham.minPoint(points, 1);
        Point lowest2 = Graham.minPoint(points, 2);
        Point lowest4 = Graham.minPoint(points, 4);

        assertEquals(lowest1,f);
        assertEquals(lowest2,f);
        assertEquals(lowest4,f);

        assertNotEquals(lowest1,a);

    }

    @Test
    public void testPolarPoints() throws Exception {

        Point f = new Point (4,2);

        Point c = new Point (3,3);
        Point h = new Point (5,3);

        Point e = new Point (4,4);
        Point d = new Point (4,6);

        int result1 = Graham.comparePolarPoints(c,h,f);
        int result2 = Graham.comparePolarPoints(e,d,f);

        assertEquals(result1,1);
        assertEquals(result2,-1);
    }
    @Test
    public void testSortedPoints() throws Exception {

        Point a = new Point(2, 2);
        Point b = new Point(1, 1);
        Point c = new Point(0, 0);
        Point d = new Point(2, 0);
        Point e = new Point(1, 0);
        Point f = new Point(0, 1);
        Point g = new Point(0, 2);





        Point[] points = new Point[]{a, b, c, d, e, f, g};


        ConcurrentQuickSort q = new ConcurrentQuickSort(points,0,points.length-1,c,1);

        Thread t =new Thread(q);
        t.start();
        try
        {
            t.join();
        } catch (InterruptedException exc) {
        }


        assertEquals(points.length, 7);

        assertEquals(points[0], c);
        assertEquals(points[1], e);
        assertEquals(points[2], d);
        assertEquals(points[3], b);
        assertEquals(points[4], a);
        assertEquals(points[5], f);
        assertEquals(points[6], g);



    }


}