/*
 * Name: Karthikan Suntharesan
 * Student Number: 300240065
 */

/*
 * This class is responsible for creating the points and assigning them labels
 */
public class Point3D {
    private double x;
    private double y;
    private double z;
    private int label;

    /*
     * method assigns the point the x,y and z values from the parameters
     * @param value of x
     * @param value of y
     * @param value of z
     */
    public Point3D (double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.label = 0;
    }

    /*
     * getter method for the x value of the point
     * @return value of x
     */
    public double getX()
    {
        return this.x;
    }

    /*
     * getter method for the value of y
     * @return the value of y
     */
    public double getY()
    {
        return this.y;
    }

    /*
     * getter method for the value of z
     * @return value of z
     */
    public double getZ()
    {
        return this.z;
    }

    /*
     * method calculates the euclidian distance between two points and returns that double value
     * @param point to compare to
     * @return distance between the points
     */
    public double distance(Point3D pt)
    {
        return Math.sqrt(Math.pow((pt.x - this.x),2) + Math.pow((pt.y - this.y),2)
         + Math.pow((pt.z - this.z), 2));
    }

    /*
     * getter method for the label of the point
     * @return label number
     */
    public int getLabel()
    {
        return this.label;
    }

    /*
     * setter method for the point's label
     * @param label number
     */
    public void setLabel(int num)
    {
        this.label = num;
    }
}