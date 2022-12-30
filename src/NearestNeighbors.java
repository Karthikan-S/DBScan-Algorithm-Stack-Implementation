/*
 * Name: Karthikan Suntharesan
 * Student Number: 300240065
 */
import java.util.*;
/*
 * this class is responsible for creating a list of points which are in eps distance of a point
 */
public class NearestNeighbors {
    List<Point3D> points;

    /*
     * method copies the list of all points
     * @param list of points
     */
    public NearestNeighbors(List<Point3D> list)
    {
        points = list;

    }

    /*
     * method creates the list of points which are deemed neighbors if they are within eps distance of the point
     * which it is being compared to.
     * @param eps value
     * @param point which is compared to the rest of the points
     * @return list of neighbors to that point
     */
    public List<Point3D> rangeQuery(double eps, Point3D P)
    {
        List<Point3D> neighbors = new Stack<Point3D>(); //create empty list of neighbors

        for (Point3D point : points)
        {
            if(P.distance(point) <= eps) // checking if each point is close enough to be considered neighbors
            {
                neighbors.add(point); //adding neighbors to the list
            }
        }

        return neighbors;

    }

}
