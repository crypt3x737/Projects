import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * Class to keep track of walkway characteristics.
 * @author 7249805
 */
public class Walkway {
    
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
