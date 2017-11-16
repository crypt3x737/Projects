/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * Class to handle showing users information about each bone when they are 
 * clicked upon. 
 * 
 * @author 7275514
 */
public class boneInfo extends javax.swing.JFrame {

    private JLabel boneIdInfo;
    private JLabel completenessInfo;
    private JLabel dateFoundInfo;
    private JLabel elementInfo;
    private JLabel elevationInfo;
    private JLabel exposedInfo;
    private JLabel genderInfo;
    private JScrollPane scrollPane;
    private JLabel lengthInfo;
    private JLabel objectIdInfo;
    private JLabel objectInfo;
    private JLabel portionInfo;
    private JLabel previousInfo;
    private JTextArea remarksInfo;
    private JLabel subelementInfo;
    private JLabel taxonInfo;
    private JLabel yearDiscoveredInfo;
    private Container contents;
    
    
    /**
     * Creates new boneInfo.
     * @param boolean modal - 
     * @param Bonerec Record - the data about the bone which is to be displayed
     * @return boneInfo
     */
    
    public boneInfo(boolean modal, Bonerec Record) {
        //super(parent, Record.unique_id, modal);
        initComponents();
        myInitComponents();
        
        // set dialog box layout
        setLayout(new BorderLayout());
        setResizable(true);
        setMinimumSize(new Dimension(700, 350));
        
        // get image in here
        JPanel imagePanel = new JPanel();
        JLabel imageLabel;
        
        String directory = System.getProperty("user.dir") + "/bonexml/" + Record.unique_id + ".jpg";
        File checkIfImageExists = new File(directory);
        boolean exists = checkIfImageExists.exists();
       
        if (exists)
            imageLabel = new JLabel(new ImageIcon(directory));
        else
            imageLabel = new JLabel(new ImageIcon(System.getProperty("user.dir") + "/bonexml/default.jpg"));
        
        imageLabel.setToolTipText("Image of bone");
        
        // add image to the image panel, and place the image panel on the east side
        imagePanel.add(imageLabel);
        JPanel labelPanel = new JPanel();

        contents = getContentPane();
        contents.setLayout(new BorderLayout());
                
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        
        // insert Labels 
        labelPanel.add(boneIdInfo);
        labelPanel.add(completenessInfo);
        labelPanel.add(yearDiscoveredInfo);
        labelPanel.add(exposedInfo);
        labelPanel.add(objectInfo);
        labelPanel.add(genderInfo);
        labelPanel.add(previousInfo);
        labelPanel.add(dateFoundInfo);
        labelPanel.add(objectInfo);
        labelPanel.add(taxonInfo);
        labelPanel.add(elevationInfo);
        labelPanel.add(elementInfo);
        labelPanel.add(objectIdInfo);
        labelPanel.add(subelementInfo);
        labelPanel.add(lengthInfo);
        labelPanel.add(portionInfo);
        
        JPanel remarksPanel = new JPanel();
        remarksPanel.setLayout(new BorderLayout()); 
        
        JLabel titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 28));
        titleLabel.setText("Details for bone " + Record.unique_id);
        
        JPanel masterPanel = new JPanel();
        
        masterPanel.setLayout(new BorderLayout());
        masterPanel.add(labelPanel, BorderLayout.WEST);
        
        contents.add(titleLabel, BorderLayout.NORTH);
        contents.add(masterPanel, BorderLayout.WEST);
        contents.add(imagePanel, BorderLayout.EAST);
        contents.add(scrollPane, BorderLayout.SOUTH);

        pack();
        setForm(Record);
        setVisible(true);
        
        setTitle(Record.unique_id);
    }

    /**
     * Creates new boneInfo.
     * @param boolean modal - 
     * @param java.awt.Frame parent - the parent frame
     * @return boneInfo
     */
    public boneInfo(java.awt.Frame parent, boolean modal) {
        initComponents();
    }
    
    /**
     * Fills out each field in the form.
     * @param Bonerec _record - the data about the bone which is to be displayed
     * @return none
     */
    
    public void setForm(Bonerec _record) {
        // Fill in data fields, using defaults if null
        if (_record.unique_id != null)
            boneIdInfo.setText("Bone ID: " + _record.unique_id);
         else 
            boneIdInfo.setText("Bone ID: unavailable");

        if (_record.completeness != null) {
            completenessInfo.setText("Completeness: " + _record.completeness);
        } else {
            completenessInfo.setText("Completeness: unavailable");
        }

        if (_record.date_found != null) {
            dateFoundInfo.setText("Date Found: " + _record.date_found);
        } else {
            dateFoundInfo.setText("Date Found: unavailable");
        }

        if (_record.element != null) {
            elementInfo.setText("Element: " + _record.element);
        } else {
            elementInfo.setText("Element: unavailable");
        }

        if (_record.elevation != null) {
            elevationInfo.setText("Elevation: " + _record.elevation);
        } else {
            elevationInfo.setText("Elevation: unavailable");
        }

        if (_record.expside != null) {
            exposedInfo.setText("Exposed side: " + _record.expside);
        } else {
            exposedInfo.setText("Exposed side: unavailable");
        }

        if (_record.gender != null) {
            genderInfo.setText("Gender: " + _record.gender);
        } else {
            genderInfo.setText("Gender: unavailable");
        }

        if (_record.shape_length != null) {
            lengthInfo.setText("Length: " + _record.shape_length);
        } else {
            lengthInfo.setText("Length: unavailable");
        }

        if (_record.object_id != null) {
            objectIdInfo.setText("Object ID: " + _record.object_id);
        } else {
            objectIdInfo.setText("Object ID: unavailable");
        }

        if (_record.object_num != null) {
            objectInfo.setText("Object number: " + _record.object_num);
        } else {
            objectInfo.setText("Object number: unavailable");
        }

        if (_record.portion != null) {
            portionInfo.setText("Portion: " + _record.portion);
        } else {
            portionInfo.setText("Portion: unavailable");
        }

        if (_record.previous != null) {
            previousInfo.setText("Previous: " + _record.previous);
        } else {
            previousInfo.setText("Previous: unavailable");
        }

        if (_record.remarks != null) {
            remarksInfo.setText("Remarks: \n" + _record.remarks);
        } else {
            remarksInfo.setText("Remarks: unavailable");
        }

        if (_record.sub_element != null) {
            subelementInfo.setText("Subelement: " + _record.sub_element);
        } else {
            subelementInfo.setText("Subelement: unavailable");
        }

        if (_record.taxon != null) {
            taxonInfo.setText("Taxon: " + _record.taxon);
        } else {
            taxonInfo.setText("Taxon: unavailable");
        }

        if (_record.year != null) {
            yearDiscoveredInfo.setText("Year: " + _record.year);
        } else {
            yearDiscoveredInfo.setText("Year: unavailable");
        }

    }
    
    /**
     * Initializes all the components of the frame.
     * @param boolean none
     * @return none
     */
    
    private void myInitComponents()
    {
        boneIdInfo = new JLabel();
        yearDiscoveredInfo = new JLabel();
        objectInfo = new JLabel();
        previousInfo = new JLabel();
        taxonInfo = new JLabel();
        elementInfo = new JLabel();
        subelementInfo = new JLabel();
        portionInfo = new JLabel();
        completenessInfo = new JLabel();
        exposedInfo = new JLabel();
        genderInfo = new JLabel();
        dateFoundInfo = new JLabel();
        elevationInfo = new JLabel();
        lengthInfo = new JLabel();
        objectIdInfo = new JLabel();
        
        remarksInfo = new JTextArea(""); 
        remarksInfo.setBorder(null);
        remarksInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        remarksInfo.setBackground(new Color(0, 0, 0, 0));
        remarksInfo.setEditable(false);
        remarksInfo.setLineWrap(true);
        
        scrollPane = new JScrollPane(remarksInfo);
        scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scrollPane.setBorder(null);
        
        boneIdInfo.setText("boneInfo");
        boneIdInfo.setFont(new Font("Serif", Font.PLAIN, 14));

        yearDiscoveredInfo.setText("yearInfo");
        yearDiscoveredInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        objectInfo.setText("objectInfo");
        objectInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        previousInfo.setText("prev Info");
        previousInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        taxonInfo.setText("taxonInfo");
        taxonInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        elementInfo.setText("elementInfo");
        elementInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        subelementInfo.setText("subelementInfo");
        subelementInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        portionInfo.setText("boneInfo");
        portionInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        completenessInfo.setText("completenessInfo");
        completenessInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        exposedInfo.setText("exposedInfo");
        exposedInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        genderInfo.setText("genderInfo");
        genderInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        dateFoundInfo.setText("dateFoundInfo");
        dateFoundInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        elevationInfo.setText("elevationInfo");
        elevationInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        lengthInfo.setText("lengthInfo");
        lengthInfo.setFont(new Font("Serif", Font.PLAIN, 14));
        
        objectIdInfo.setText("objectIdInfo");
        objectIdInfo.setFont(new Font("Serif", Font.PLAIN, 14));
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setSize(new java.awt.Dimension(817, 800));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 786, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 436, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
