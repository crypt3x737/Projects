/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Scanner;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 7249805
 */
public class Bonerec_Parser {

    String file_path;

    /**
     * Creates new Bonerec_Parser.
     * @param String input - the mainForm object it is associated with
     * @return Bone_Description_Parser
     */
    public Bonerec_Parser(String input) {
        file_path = input;
    }
    
    /**
     * Traverses the xml files and calls read on them.
     * @param none
     * @return none
     */
    
    public Vector<Bonerec> parse() {
        // read and parse XML document
       Vector <Bonerec> record = new Vector <Bonerec>();
        
        try {
            // get root node of XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file_path);

            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("bonerec");
            record = read(nList);
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return record;

    }
    
    /**
     * Reads the data for each bone from the list of nodes.
     * @param NodeList nList - list of nodes to read from
     * @return none
     */
    
    public Vector<Bonerec> read(NodeList nList) 
    {
        Vector<Bonerec> lib=new Vector<Bonerec>();
        
        for (int i = 0; i < nList.getLength(); i++) 
        {   
            Bonerec temp = new Bonerec();
       
            Node nNode = nList.item(i);
            
            
            
            NodeList child = nNode.getChildNodes();

            for (int j = 0; j < child.getLength(); j++)
            {
                Node child_node = child.item(j);

                if (child_node.getNodeName() == "uniqueid") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.unique_id=sc.next();
                } 
                
                else if (child_node.getNodeName() == "year") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.year=sc.next();
                } 
                
                else if (child_node.getNodeName() == "objectnum") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.object_num=sc.next();
                }
                
                else if (child_node.getNodeName() == "part") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.part=sc.next();
                } 
                
                else if (child_node.getNodeName() == "previous") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.previous=sc.next();
                }
                
                else if (child_node.getNodeName() == "taxon") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.taxon=sc.nextLine();
                } 
                
                else if (child_node.getNodeName() == "element") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.element=sc.nextLine();
                }
                
                else if (child_node.getNodeName() == "subelement") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.sub_element=sc.nextLine();
                } 
                
                else if (child_node.getNodeName() == "side") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.side=sc.next();
                }
                
                else if (child_node.getNodeName() == "portion")
                {
                  Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.portion=sc.next();
                } 
                
                else if (child_node.getNodeName() == "completeness")
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.completeness=sc.next();
                }
                
                else if (child_node.getNodeName() == "expside") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.expside=sc.next();
                } 
                
                else if (child_node.getNodeName() == "crack") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.crack=sc.next();
                }
                
                else if (child_node.getNodeName() == "articulate") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.articulate=sc.next();
                } 
                
                else if (child_node.getNodeName() == "gender") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.gender=sc.next();
                }
                
                else if (child_node.getNodeName() == "remarks") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.remarks=sc.nextLine();
                } 
                
                else if (child_node.getNodeName() == "datefound") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.date_found=sc.next();
                }
                
                else if (child_node.getNodeName() == "foundby")
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.found_by=sc.next();
                } 
                
                else if (child_node.getNodeName() == "elevation") 
                {
                   Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.elevation=sc.next();
                }
                
                else if (child_node.getNodeName() == "updates") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.updates=sc.next();
                } 
                
                else if (child_node.getNodeName() == "mappic") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.mappic=sc.next();
                }
                
                else if (child_node.getNodeName() == "fieldnotes") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.field_notes=sc.next();
                } 
                
                else if (child_node.getNodeName() == "removed")
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.removed=sc.next();
                }
                
                else if (child_node.getNodeName() == "removedby") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.removed_by=sc.next();
                } 
                
                else if (child_node.getNodeName() == "azimuth") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.azimuth=sc.next();
                }
                
                else if (child_node.getNodeName() == "incline") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.incline=sc.next();
                } 
                
                else if (child_node.getNodeName() == "labrec") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.labrec=sc.next();
                }
                
                else if (child_node.getNodeName() == "objectid") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.object_id=sc.next();
                } 
                
                else if (child_node.getNodeName() == "shapelength") 
                {
                    Scanner sc = new Scanner(child_node.getTextContent().trim());
                    temp.shape_length=sc.next();
                }
            }
            lib.add(temp);
        }
        return lib;
        
    }
}
