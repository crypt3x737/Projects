    
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 public class Plot extends JPanel
{
    boolean drag=true;
    mainForm obj2;
    Vector<Bone_Description> bone_description;
    Vector<Bonerec> bone_rec=new Vector<Bonerec>();
    Vector<Walkway> walk_details=new Vector<Walkway>();
    Vector<Color> colors=new Vector<Color>();
    Vector<Double> elevations=new Vector<Double>();
    Vector<Double> lengths=new Vector<Double>();
    Vector<Double> years=new Vector<Double>();
    Vector<Double> elements=new Vector<Double>();
    double height=0.0;
    double width=0.0;
    double xPoint = 0;
    double yPoint = 0;
    double xOffset = 0.0;
    double yOffset = 0.0;
    double zoomVal = 1.0;
    Point origin = new Point(getWidth()/2, getHeight() / 2);
    Point mousePt;
    boolean walk;
    boolean move=false;
    /**
     * Sets the default size for the plot JPanel object.
     */
    
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    /**
     * Creates new Plot, initializes key variables, and adds listeners.
     * @param mainForm gui - the mainForm object it is associated with
     * @return Plot
     */
    
    public Plot(mainForm gui) {

        obj2 = gui;
        bone_description= obj2.bone_descriptions;
        walk_details=obj2.walk_detail;
        bone_rec=obj2.bone_records;
        obj2.add(this, BorderLayout.CENTER);
        
            xOffset = - gui.getWidth() / 3.0;
            yOffset = - gui.getHeight() / 3.0;
        
        add_colors();
        
        
        addMouseListener( new MouseAdapter()
        {
              public void mousePressed(MouseEvent e){
                drag=true;
                mousePt = e.getPoint();
            }
              
            
              public void mouseMoved(MouseEvent e){
               repaint();
            }
            

            public void mouseClicked( MouseEvent e )
            {
                if(drag)
                {
                MapMethods testMap = new MapMethods(bone_description);
                Point2D.Double testPoint = new Point2D.Double();
                
                //Double x1 = (polyline.get(k).x - walk_details.get(0).x_min)*scale + xOffset + (this.getWidth() / 2.0);
               // Double y1 = (walk_details.get(0).y_max-polyline.get(k).y)*scale + yOffset + (this.getHeight() / 2.0);
                                
                testPoint.x = ((e.getX() - xOffset - width)/(15*zoomVal)) + walk_details.get(0).x_min;
                testPoint.y = (-1)*((e.getY()-yOffset-height)/(15*zoomVal)-walk_details.get(0).y_max);  
                
                System.out.println(gui.getWidth()/2.0+"  -  "+gui.getHeight()/2.0);
                System.out.println("dfs - "+testPoint.x+ " : "+testPoint.y);
                System.out.println("click - "+e.getX()+" : "+e.getY());
                
                Vector<String> boneResults = new Vector<String>();
        
                testMap.findRangeOfElevations(obj2.bone_records);
        
                boneResults = testMap.pointWithinBone(testPoint);
                    
                // handle if there was only one result
                if (boneResults.size() == 1)
                {
                    for (int i = 0; i < obj2.bone_records.size(); i++)
                    {
                        if (obj2.bone_records.get(i).unique_id.equals(boneResults.get(0)))
                        obj2.boneDetails = new boneInfo(true, obj2.bone_records.get(i));
                    }
                }
                else if (boneResults.size() > 1)
                {
                            System.out.println("fasda");
                    Bonerec closestBone = new Bonerec();
                    closestBone = testMap.findClosestBone(bone_description, obj2.bone_records, boneResults, testPoint);
                    obj2.boneDetails = new boneInfo(true, closestBone);
                }
            }
            }
        } );
        
        addMouseMotionListener(new MouseAdapter()
        {
                public void mouseDragged(MouseEvent e) {
                drag=false;
                xOffset += e.getX() - mousePt.x;
                yOffset += e.getY() - mousePt.y;
                mousePt=e.getPoint();
                
                repaint();
                
                }
                
                
        });
                
        
        addMouseWheelListener( new MouseAdapter()
        {
               public void  mouseWheelMoved(MouseWheelEvent e)
               {    
                  
                        double oldZoom = zoomVal;
                        double tempX;
                        double tempY;
                        //double mouseX = e.getX();
                        //double mouseY = e.getY();
                        //zoomVal *= Math.pow(0.8, (double)notches);
                        //if(zoomVal>30)
                        //    zoomVal=30;
                        //if(zoomVal<0.1)
                        //    zoomVal=0.1;
                        if( e.getPreciseWheelRotation() == -1)
                        {
                            zoomVal *= 1.05;
                        }
                        else{
                            zoomVal *= .95;

                        }
                        //tempX = mouseX;
                        //tempY = mouseY;
                        //mouseX = (mouseX *oldZoom)/zoomVal;
                        //mouseY = (mouseY *oldZoom)/zoomVal;
                        xOffset = (xOffset /oldZoom)*zoomVal;
                        yOffset = (yOffset /oldZoom)*zoomVal;
                        //xOffset += (obj2.topPanel.getWidth() / 2.0) * zoomVal;
                        //yOffset += (obj2.topPanel.getHeight() / 2.0) * zoomVal;
                        repaint();
                    

               }
        
        });

        
        obj2.setVisible(true);
        for(int i=0;i<50;i++)
            repaint();

    }
    
    /**
     * Adds colors to the display and determines which to use on each bone.
     * @param none
     * @return none
     */
    
    public void add_colors()
    {
        colors.add(Color.black);
        colors.add(Color.red);
        colors.add(Color.yellow);
        colors.add(Color.orange);
        colors.add(Color.green);
        colors.add(Color.blue);
        colors.add(Color.cyan);
        colors.add(Color.GRAY);
        colors.add(Color.darkGray);
        colors.add(Color.magenta);
        colors.add(Color.PINK);
                
                
       
        double full=obj2.elev_max-obj2.elev_min;
        double half=full/6;
        double counter=obj2.elev_min;
        while(counter<obj2.elev_max)
        {
            elevations.add(counter);
            counter=counter+half;
        }
        
        full=obj2.date_max-obj2.date_min;
        half=full/6;
        years.add(obj2.date_min*1.0);
        counter=obj2.date_min;
        while(counter<obj2.date_max)
        {
            years.add(counter);
            counter=counter+half;
        }
        
        full=obj2.length_max-obj2.length_min;
        half=full/6;
        lengths.add(obj2.length_min);
        counter=obj2.length_min;
        while(counter<obj2.length_max)
        {
            lengths.add(counter);
            counter=counter+half;
        }
        
        full=obj2.element_max-obj2.element_min;
        half=full/14;
        elements.add(obj2.element_min);
        counter=obj2.element_min;
        while(counter<obj2.element_max)
        {
            elements.add(counter);
            counter=counter+half;
        }
    }
    
    /**
     * paints bones to the Plot opject.
     * @param Graphics g
     * @return none
     */
    
    public void paintComponent(Graphics g) {      
        
        super.paintComponent(g);
        
        try
        {
            for (int i = 0; i < bone_description.size(); i++) {
                walk=false;
                Bone_Description currentBone = new Bone_Description();
                
                currentBone = bone_description.elementAt(i);
                getColor(g,currentBone.id);
                for (int j = 0; j < currentBone.n_polylines; j++)
                {  
                    Vector<Point2D.Double> polyline=currentBone.polylines.get(j);

                    for(int k = 0; k < polyline.size()-1;k++)
                    {
                        double scale=15*zoomVal;
                        height=this.getHeight() / 2.0;
                        width=this.getWidth() / 2.0;
                        
                        Double x1 = (polyline.get(k).x - walk_details.get(0).x_min)*scale + xOffset + (this.getWidth() / 2.0);
                        Double y1 = (walk_details.get(0).y_max-polyline.get(k).y)*scale + yOffset + (this.getHeight() / 2.0);
                        Double x2 = (polyline.get(k+1).x  -  walk_details.get(0).x_min)*scale + xOffset + (this.getWidth() / 2.0);
                        Double y2 = (walk_details.get(0).y_max-polyline.get(k+1).y )*scale + yOffset + (this.getHeight() / 2.0);
                        
                        g.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
                        
                        
                        //System.out.println(x1 + "    " + y1 + "    " + x2 + "       " + y2);
                    }
                }
            }
           
            for(int i = 0; i < walk_details.size(); i++)
            {
                walk=true;
                Walkway paths = new Walkway();
                paths = walk_details.get(i);
                g.setColor(Color.black);
                for(int j = 0; j < paths.polylines.size(); j++)
                {
                    Vector<Point2D.Double> polyline=paths.polylines.get(j);
                    
                    for( int k = 0; k < polyline.size()-1; k++)
                    {
                        
                       double scale=15*zoomVal;
                        Double x1 = (polyline.get(k).x - walk_details.get(0).x_min)*scale + xOffset + (this.getWidth() / 2.0);
                        Double y1 = (walk_details.get(0).y_max-polyline.get(k).y )*scale + yOffset + (this.getHeight() / 2.0);
                        Double x2 = (polyline.get(k+1).x  -  walk_details.get(0).x_min)*scale + xOffset + (this.getWidth() / 2.0);
                        Double y2 = (walk_details.get(0).y_max-polyline.get(k+1).y)*scale + yOffset + (this.getHeight() / 2.0);
                        g.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
                        

                    }
                }
            }           
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void getColor(Graphics g,String id) {
              // Get the color currently displayed by the component.
              Filter_Search temp=new Filter_Search();
              
              for(int i=0;i<obj2.filters.size();i++)
              {
                  if(id.equals(obj2.filters.get(i).id))
                  {
                   temp=obj2.filters.get(i);
                   break;
                  }
              }
                            
              g.setColor(Color.black);
              
              if(walk==false)
              {
                   if(obj2.resetButton.isSelected()==true)
                {
                        if(temp.element!=null && temp.element!="")
                        {
                            if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                            {
                               g.setColor(Color.black);
                            }
                            else 
                               g.setColor(getBackground());
                        }
                       
                     
                }
                  else if(obj2.genderButton.isSelected()==true)
                {
                      if(temp.gender!=null && temp.gender!="" )
                     { 
                           if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                        {   
                            if(obj2.comboBox.getSelectedItem().toString().equals("No Selection"))
                            {
                                 
                                    for(int i=0;i<obj2.gender.size();i++)
                                    {
                                        if(temp.gender.toLowerCase().equals(obj2.gender.get(i).toLowerCase()))
                                        {
                                            g.setColor(colors.get(i-1));
                                          
                                            break;
                                        }
                                    }
                            }
                           else
                           {
                            if( temp.gender.toLowerCase().equals(obj2.comboBox.getSelectedItem().toString().toLowerCase()))
                                g.setColor(colors.get(obj2.comboBox.getSelectedIndex()-1));
                            else
                                g.setColor(getBackground());
                            }
                       }
                        else
                        g.setColor(getBackground());
                    }
                    else
                        g.setColor(getBackground());
                 }
                else if(obj2.lengthButton.isSelected()==true)
                {
                       if(temp.length!=null && temp.length!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                         for(int i=0;i<lengths.size()-1;i++)
                         {
                             if(Double.parseDouble(temp.length)<lengths.get(i))
                             {
                                 g.setColor(colors.get(i));
                                 break;
                             }
                         }

                     }
                         else 
                             g.setColor(getBackground());
                         
                     }
                       else
                           g.setColor(getBackground());
                }
                else if(obj2.dateFoundButton.isSelected()==true)
                {
                    if(temp.year!=null && temp.year!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                         for(int i=0;i<years.size()-1;i++)
                         {
                             if(Integer.parseInt(temp.year)<years.get(i))
                             {
                                 g.setColor(colors.get(i));
                                 break;
                             }
                         }     
                     }
                         else
                             g.setColor(getBackground());
                     }
                    else
                        g.setColor(getBackground());
                }
                else if(obj2.elevationButton.isSelected()==true)
                {
                     if(temp.elevation!=null && temp.elevation!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                         for(int i=0;i<elevations.size()-1;i++)
                         {
                             if(Double.parseDouble(temp.elevation)<elevations.get(i))
                             {
                                 g.setColor(colors.get(i));
                                 break;
                             }
                         }

                     }
                         else
                             g.setColor(getBackground());
                     }

                }
                else if(obj2.exposedSideButton.isSelected()==true)
                {
                     if(temp.exposed_side!=null && temp.exposed_side!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                           if(obj2.comboBox.getSelectedItem().toString().equals("No Selection"))
                            {
                                 
                                    for(int i=0;i<obj2.comboBox.getItemCount();i++)
                                    {
                                        if(temp.exposed_side.toLowerCase().equals(obj2.comboBox.getItemAt(i).toString().toLowerCase()))
                                        {
                                            g.setColor(colors.get(i-1));
                                          
                                            break;
                                        }
                                    }
                            }
                           else
                           {  
                         if(temp.exposed_side.equals(obj2.comboBox.getSelectedItem()))
                         {

                             g.setColor(colors.get(obj2.comboBox.getSelectedIndex()-1));
                         }
                         else
                         {
                             g.setColor(getBackground());
                         }
                       }
                     }
                         else
                             g.setColor(getBackground());
                         
                     }
                    else
                        g.setColor(getBackground());
                }
                else if(obj2.taxonButton.isSelected()==true)
                {
                     if(temp.taxon!=null && temp.taxon!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                            if(obj2.comboBox.getSelectedItem().toString().equals("No Selection"))
                            {
                                 
                                    for(int i=0;i<obj2.comboBox.getItemCount();i++)
                                    {
                                        if(temp.taxon.toLowerCase().equals(obj2.comboBox.getItemAt(i).toString().toLowerCase()))
                                        {
                                            g.setColor(colors.get(i-1));
                                          
                                            break;
                                        }
                                    }
                            }
                           else
                           {  
                         if(temp.taxon.equals(obj2.comboBox.getSelectedItem()))
                         {

                             g.setColor(colors.get(obj2.comboBox.getSelectedIndex()-1));
                         }
                         else
                         {
                             g.setColor(getBackground());
                         }
                           }
                         
                         
                       }
                         else
                             g.setColor(getBackground());

                     }
                    else
                        g.setColor(getBackground());
                }
                else if(obj2.subelementButton.isSelected()==true)
                {
                      if(temp.subelement!=null && temp.subelement!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                         if(temp.subelement.equals(obj2.comboBox.getSelectedItem()))
                         {

                             g.setColor(Color.black);
                         }
                         else
                         {
                             g.setColor(getBackground());
                         }
                       }
                         else
                             g.setColor(getBackground());

                     }
                    else
                        g.setColor(getBackground());
                }
                else if(obj2.completenessButton.isSelected()==true)
                {                 
                    if(temp.completeness!=null && temp.completeness!="" )
                     { 
                         if(Double.parseDouble(temp.element)<=elements.get(obj2.slider.getValue()-1))
                       {
                           if(obj2.comboBox.getSelectedItem().toString().equals("No Selection"))
                            {
                                 
                                    for(int i=0;i<obj2.comboBox.getItemCount();i++)
                                    {
                                        if(temp.completeness.toLowerCase().equals(obj2.comboBox.getItemAt(i).toString().toLowerCase()))
                                        {
                                            g.setColor(colors.get(i-1));
                                          
                                            break;
                                        }
                                    }
                            }
                           else
                           {  
                         if(temp.completeness.equals(obj2.comboBox.getSelectedItem()))
                         {

                             g.setColor(colors.get(obj2.comboBox.getSelectedIndex()-1));                         }
                         else
                         {
                             g.setColor(getBackground());
                         }
                           }
                       }
                         else
                             g.setColor(getBackground());

                     }
                    else
                        g.setColor(getBackground());
                }   
              }
    }
}