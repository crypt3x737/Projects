import java.util.Vector;
import java.awt.geom.Point2D;

/**
 * Class to keep track of items in the bones.xml file.
 */
public class Bone_Description {
    
    Vector<Vector<Point2D.Double>> polylines; 

    double x_min = 0;
    double y_min = 0;
    double x_max = 0;
    double y_max = 0;
    int n_shapes = 0;
    int n_polylines = 0;
    int n_vertices = 0;
    String id = "";

}
