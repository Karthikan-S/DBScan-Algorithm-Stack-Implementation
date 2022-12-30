/*
 * Name: Karthikan Suntharesan
 * Student Number: 300240065
 */
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * This class takes a dataset of points and then groups them in clusters
 * based on how dense they are and returns a csv file with the clusters and an rgb value associated to
 * each point 
 */
public class DBScan{
    List<Point3D> points;
    private double eps;
    private double minPts;
    private int clusters;

    /*
     * This method is responsible for creating an object of this class by providing the csv file containing the points
     * It uses the command line to give inputs for the csv file, a double the eps value which is the distance that
     * points need to be within to be considered neighbors, and minPoints which is how many neighbors a point needs to
     * be considered a cluster
     * 
     * @param args[0] The file name.csv
     * @param args[1] The value of eps
     * @param args[2] The value of minPts
     */
    public static void main(String args[])
    {
        // DBScan scan = new DBScan(read("Point_Cloud_1.csv"));

        // scan.setEps(0.9);
        // scan.setMinPts(10);

        // scan.findClusters();
        // scan.save("Point_Cloud_1.csv");
        try {
            DBScan scan = new DBScan(read(args[0]));
            scan.setEps(Double.valueOf(args[1]).doubleValue());
            scan.setMinPts(Double.valueOf(args[2]).doubleValue());
            scan.findClusters();
            scan.save(args[0]); 
    
            
          } catch (Exception e) {
            // If we arrive here, it is because either there were no
            // command line arguments, or they were invalid
            if (args.length != 0)
              System.out.println("Invalid arguments on command line");
          }
       
    }

    /*
     * This method constructs the DBScan object with a input of a list of points
     * @param list of points
     */
    public DBScan(List<Point3D> list)
    {
        points = list;
        eps = 0;
        minPts = 0;
        clusters = 0;
    }

    /*
     * setter method for the eps value
     * @param the value of eps
     */
    public void setEps(double eps)
    {
        this.eps = eps;
    } 

    /*
     * setter method for minPts
     * @param the value of minPts
     */
    public void setMinPts(double minPts)
    {
        this.minPts = minPts;
    }

    /*
     * The main algorithm for determining if a point has enough neighbors to be considered a cluster
     * then gives every point a label based on their cluster
     */
    public void findClusters() 
    {
        NearestNeighbors neighbors = new NearestNeighbors(points);
        for(Point3D p: points)
        {
            if (p.getLabel() != 0) // point is already labelled 
            {
                continue;
            }

            List<Point3D> n = neighbors.rangeQuery(eps, p); //finding neighbors
            if(n.size() < minPts) // doesnt have enough neighbors to be classified as a cluster
            {
                p.setLabel(-1); // -1 = noise
                continue;
            }

            clusters += 1; // increment the amount of clusters
            p.setLabel(clusters); //label this point into the current cluster
            Stack<Point3D> s = new Stack<Point3D>(); //create a stack
            for (Point3D N: n)
            {
                s.push(N); // pushing all neighbors into a stack
            }
            while(!(s.isEmpty())) 
            {
                Point3D q = s.pop(); // getting each neighbor
                if (q.getLabel() == -1) q.setLabel(clusters); // if this neighbor was noise then it is an edge of this cluster
                if (q.getLabel() != 0) continue; //the neighbor already belongs to another cluster
                q.setLabel(clusters); // settin neigbor to be apart of the same cluster as the core point p

                List<Point3D> n1 = neighbors.rangeQuery(eps, q); //getting neighbors of the neighbor point
                if(n1.size() > minPts) //checking if it is a core point
                {
                    for(Point3D point: n1 )
                    {
                        s.push(point); //if it is a core point adding all neighbors to the stack to include them in the cluster
                    }
                }

            }
        }

    }

    /*
     * getter method for number of clusters within the dataset
     * @return value of clusters
     */
    public int getNumberOfClusers()
    {
        return clusters;
    }

    /*
     * getter method for the list of points
     * @return the list of points
     */
    public List<Point3D> getPoints()
    {
        return points;
    }

    /*
     * method that reads the data from the csv file and creates the list of points
     * @param the csv file name
     * @return list of points from the file 
     */
    public static List<Point3D> read(String filename)
    {
        String line = ""; //empty string
        String splitter = ","; //the seperator of the file
        List<Point3D> points = new ArrayList<Point3D>();

        try{
        BufferedReader reader = new BufferedReader(new FileReader(filename)); //creating reader object
        while ((line = reader.readLine()) != null) //while we have not reached last line of the file
        {
            String[] lines = line.split(splitter); // seperating each line into an array 
            if(lines[0].equals("x")) continue;
            try{
                points.add(new Point3D(Double.parseDouble(lines[0]), 
                 Double.parseDouble(lines[1]), Double.parseDouble(lines[2]))); //adding points to the list
            }
            catch(NumberFormatException ex)
            {
                ex.printStackTrace();
            }


        }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        // System.out.println(points.get(1).getZ());
        return points;

    }

    /*
     * method creates a new csv file with the appropriate cluster labels and rgb values
     * assigned to each point, and names it appropriately using the original file's name,
     * the eps value, minPts value and total amount of clusters
     * @param original file name
     */
    public void save(String filename)
    {

        String [][] stringPoints = new String[points.size() + 1][];//array of arrays of points to use for file writing
        stringPoints[0] = new String[]{"x","y","z","C","R","G","B"}; 
        for(int i=1; i < stringPoints.length; i++) //going through every point in the list
        {
            if(i==1) stringPoints[i] = new String[]{"0","0","0","0","0","0","0"};
            else
            {
                try{ //adding array of points to the array
                String x = Double.toString(points.get(i-1).getX()); //typecasting each value to add to the array
                String y = Double.toString(points.get(i-1).getY());
                String z = Double.toString(points.get(i-1).getZ());
                String label = Integer.toString(points.get(i-1).getLabel());
                String rgb = Double.toString((double)(points.get(i-1).getLabel())/this.getNumberOfClusers());
                stringPoints[i] = new String[]{x,y,z,label,rgb,rgb,rgb}; //adding the point to the array
                }
                catch(NumberFormatException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
   
        // creating the csv file and naming it appropriately 
        File csvFile = new File(filename.substring(0,filename.indexOf(".")) +"_clusters"+"_"+eps+"_"+(int)minPts+"_"+getNumberOfClusers()+".csv");
        
        try (FileWriter fileWriter = new FileWriter(csvFile)) { //writing into the csv file
            for (String[] p: stringPoints)
            {
                StringBuilder line = new StringBuilder();
                for (int i=0;i<p.length; i++) //going word by word of each line
                {
                    line.append(p[i]);
                    if (i != p.length-1) line.append(',');
                }
                line.append("\n");
                fileWriter.write(line.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
        
        
}
