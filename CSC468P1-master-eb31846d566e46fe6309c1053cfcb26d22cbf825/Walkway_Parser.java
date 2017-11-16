import java.awt.geom.Point2D;
import java.util.Scanner;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class to facilitate parsing the walkway. 
 * @author 7249805
 */
public class Walkway_Parser {
    String file_path;
     
    public Walkway walk_details = new Walkway();
     
    /**
     * Creates new Walkway_Parser.
     * @param String input - the mainForm object it is associated with
     * @return Bone_Description_Parser
     */
    public Walkway_Parser(String input) {
        file_path = input;
    }
    
    /**
     * Traverses the xml files and calls read on them.
     * @param none
     * @return none
     */
    
    public void parse() {
        // read and parse XML document

        try {
            // get root node of XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            
            try
            {
            Document document = builder.parse(file_path);

            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("polyline");

            Element root = document.getDocumentElement();

            read(nList, root);
            }
            catch(Exception e)
            {
                System.out.println("File not found - "+file_path);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Reads the data for the walkway from the list of nodes.
     * @param NodeList nList - list of nodes to read from
     * @return none
     */
    
    public void read(NodeList nList, Node root) {
        double x_min = 0;
        double y_min = 0;
        double x_max = 0;
        double y_max = 0;

        int n_shapes = 0;
        int n_polylines = 0;
        int n_vertices = 0;

        String id = "";

        int counter = 0;
        NodeList child = root.getChildNodes();

        for (int i = 1; i < child.getLength(); i = i + 2) {
            
            Node child_node = child.item(i);
            
            if (child_node.getNodeName() == "shape") {
                NodeList sub_child = child_node.getChildNodes();

                for (int k = 1; k < sub_child.getLength(); k = k + 2) {

                    Node sub_child_node = sub_child.item(k);

                    if (sub_child_node.getNodeName() == "id") {
                        Scanner sc = new Scanner(sub_child_node.getTextContent().trim());
                        id = sc.next();
                        counter++;
                    } else if (sub_child_node.getNodeName() == "npolylines") {
                        Scanner sc = new Scanner(sub_child_node.getTextContent().trim());
                        n_polylines = sc.nextInt();
                        counter++;
                    } else if (sub_child_node.getNodeName() == "nvertices") {
                        Scanner sc = new Scanner(sub_child_node.getTextContent().trim());
                        n_vertices = sc.nextInt();
                        counter++;
                    } else if (sub_child_node.getNodeName() == "xymin") {
                        Scanner sc = new Scanner(sub_child_node.getTextContent().trim());
                        x_min = sc.nextDouble();
                        y_min = sc.nextDouble();
                        counter++;
                    } else if (sub_child_node.getNodeName() == "xymax") {
                        Scanner sc = new Scanner(sub_child_node.getTextContent().trim());
                        x_max = sc.nextDouble();
                        y_max = sc.nextDouble();
                        counter++;
                    }

                    if (counter == 6) {
                        i = child.getLength();
                        k = sub_child.getLength();
                    }
                }

            } else if (child_node.getNodeName() == "nshapes") {
                Scanner sc = new Scanner(child_node.getTextContent().trim());
                n_shapes = sc.nextInt();
                counter++;
            }

            if (counter == 6) {
                i = child.getLength();
            }
        }

        Walkway temp = new Walkway();
        
        temp.polylines = new Vector<Vector<Point2D.Double>>();

        for (int i = 0; i < nList.getLength(); i++) {
            //Bone_Description temp = new Bone_Description();

            Vector<Point2D.Double> polyLine = new Vector<Point2D.Double>();

            Node nNode = nList.item(i);

            NodeList list = nNode.getChildNodes();

            for (int j = 1; j < list.getLength(); j = j + 2) {
                Node node = list.item(j);

                if (node.getNodeName() == "xy") {

                    String str = node.getTextContent().trim();
                    String[] splited = str.split(" ");
                    Point2D.Double newPoint = new Point2D.Double();
                    newPoint.x = Double.parseDouble(splited[0]);
                    newPoint.y = Double.parseDouble(splited[1]);

                    polyLine.add(newPoint);
                }

                temp.polylines.add(polyLine);
            }
            temp.n_polylines = n_polylines;
            temp.id = id;
            temp.n_shapes = n_shapes;
            temp.x_max = x_max;
            temp.x_min = x_min;
            temp.y_max = y_max;
            temp.y_min = y_min;
            temp.n_vertices = n_vertices;
            walk_details = temp;
        }
    }
    
}
