/*
 * Mammoth site kiosk.
 * Zoom handled with mouse scroll wheel.
 * Pan is click and drag.
 *
 * KNOWN BUG: Sometimes the application will open and nothing will appear in 
 * the pane. The workaround here is to close the application and reopen. This 
 * is a nondeterministic bug. 
 */

import javax.swing.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * Main class that is the main interface to the user, fields requests for
 * painting, filtering, etc.
 *
 */
public class mainForm extends javax.swing.JFrame {

    // Vectors to hold library of bones and walk way
    Vector<Bonerec> bone_records = new Vector<Bonerec>();
    Vector<Bone_Description> bone_descriptions = new Vector<Bone_Description>();
    Vector<Walkway> walk_detail = new Vector<Walkway>();

    // Vectors to hold filters, as well as unique values of each one found
    // within file
    Vector<Filter_Search> filters = new Vector<Filter_Search>();
    Set<String> completeness = new HashSet<String>();
    Set<String> taxon = new HashSet<String>();
    Set<String> subelement = new HashSet<String>();
    Set<String> element = new HashSet<String>();
    Set<String> expside = new HashSet<String>();
    Vector<String> gender = new Vector<String>();
    Boolean file_choose;
    String path;

    // Useful variables
    double elev_max, elev_min, length_min, length_max, element_min, element_max;
    int date_max, date_min;
    boolean fileLoaded;

    // Easy access of directory
    String boneFile = System.getProperty("user.dir") + "/bonexml/";
    Bone_Description_Parser parseBone = new Bone_Description_Parser(boneFile);

    boneInfo boneDetails;

    /**
     * Creates new form mainForm, calls initialization of the form components.
     * Asks the user if they want to open default file location, and if yes then
     * calls method to read in all relevant XML files.
     */
    public mainForm() {
        // Initialize form
        super("Bone bed viewer");
        initComponents();
        myInitComponents();
        pack();
        setLocationRelativeTo(null);   // this centers window on screen 

        // enable things and get pane set up
        filterButton.setEnabled(false);
        contents = getContentPane();
        contents.setLayout(new BorderLayout());

        // add things to pane
        contents.add(toolBar, BorderLayout.PAGE_START);
        contents.add(filterPanel, BorderLayout.WEST);
        contents.add(slider, BorderLayout.SOUTH);

        // default disable all filter buttons
        disableAllFilterButtons();

        // ask what to draw
        int reply = JOptionPane.showConfirmDialog(null, "Would you like to open Bones.xml from current directory?", "Open file", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            file_choose = false;
            // open file 
            String currentDirectory = System.getProperty("user.dir");
            String directory = currentDirectory + "/bonexml/bones.xml";
            fileSelected.setText("File Selected: bones.xml");
            // read the library
            readLibrary(directory);
            contents.add(obj, BorderLayout.CENTER);

            pack();
            chooseFileButton.setEnabled(false);
            enableAllFilterButtons();
        }
        pack();
        setVisible(true);

    }

    /**
     * Sets up forms components. All layouts and GUI components are generated
     * and built by hand. This method is where all panels, labels, etc. are
     * instantiated and added to the appropriate panel.
     *
     */
    private void myInitComponents() {
        // instantiate variables
        toolBar = new JPanel();
        chooseFileButton = new javax.swing.JButton();
        filterButton = new javax.swing.JButton();
        fileSelected = new javax.swing.JLabel();
        filterPanel = new JPanel();
        filterPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        filterPanel.setPreferredSize(new Dimension(getWidth() / 4, this.getHeight()));

        // set up panels that go inside filter panel on left side of window
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // set up slider
        slider = new JSlider(1, 15);
        slider.setBorder(BorderFactory.createTitledBorder("Details scale"));
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setVisible(true);
        slider.setValue(15);
        slider.setPaintTrack(rootPaneCheckingEnabled);
        slider.setToolTipText("smaller numbers indicate higher importance; higher numbers indicate finer detail");
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });

        // use GridLayout in filter panel to split in half
        filterGroupLayout = new GridLayout(0, 1);
        filterPanel.setLayout(filterGroupLayout);

        // vector to hold genders - shouldn't change
        String[] genderArray = {"No Selection", "Undesignated", "Male", "Female"};
        Collections.addAll(gender, genderArray);

        // add some buttons to filterPanel for filtering purposes
        setUpFilterPanel();

        // set up main form JFrame characteristics
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bone Bed Viewer");
        setPreferredSize(new java.awt.Dimension(1500, 1000));
        setResizable(true);

        // set up toolbar at the top
        toolBar.setBorder(BorderFactory.createLineBorder(Color.gray));
        toolBar.setLayout(new BorderLayout());
        toolBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        // set up choose file button at top
        chooseFileButton.setText("Choose File...");
        chooseFileButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        chooseFileButton.setFocusable(false);
        chooseFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chooseFileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        chooseFileButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                file_choose = true;
                chooseFileButtonActionPerformed(evt);
            }
        });

        // labels to tell user what file they're using, status of program
        fileSelected.setText("File Selected: None");
        fileSelected.setToolTipText("File currently being viewed");
        fileSelected.setName("file_address");

        // do layout things       
        toolBar.add(chooseFileButton, BorderLayout.WEST);
        toolBar.add(fileSelected, BorderLayout.EAST);
        pack();

    }

    /**
     * Helper method: simply disables all buttons in the filter panel.
     *
     */
    private void disableAllFilterButtons() {
        taxonButton.setEnabled(false);
        subelementButton.setEnabled(false);
        completenessButton.setEnabled(false);
        exposedSideButton.setEnabled(false);
        genderButton.setEnabled(false);
        dateFoundButton.setEnabled(false);
        elevationButton.setEnabled(false);
        lengthButton.setEnabled(false);
        resetButton.setEnabled(false);
        slider.setEnabled(false);
    }

    /**
     * Helper method: simply enables all buttons in the filter panel.
     *
     */
    private void enableAllFilterButtons() {
        taxonButton.setEnabled(true);
        subelementButton.setEnabled(true);
        completenessButton.setEnabled(true);
        exposedSideButton.setEnabled(true);
        genderButton.setEnabled(true);
        dateFoundButton.setEnabled(true);
        elevationButton.setEnabled(true);
        lengthButton.setEnabled(true);
        resetButton.setEnabled(true);
        slider.setEnabled(true);
    }

    /**
     * Instantiates and positions all components in the filter panel on the left
     * side of the screen. Includes instantiation and organization of color 
     * labels, radio buttons, titles, the legend, alignments for various panel
     * layouts, and finally adding everything together to the main panel.
     */
    private void setUpFilterPanel() {
        // panel for radio buttons
        JPanel buttonGroupPanel = new JPanel();
        buttonGroupPanel.setPreferredSize(new Dimension(4 * (getHeight() / 10), filterPanel.getWidth()));

        // for radio buttons
        GridLayout buttonGroupPanelLayout = new GridLayout(0, 1);
        buttonGroupPanelLayout.setVgap(2);
        buttonGroupPanel.setLayout(buttonGroupPanelLayout);
        ButtonGroup filterButtonGroup = new ButtonGroup();

        // <editor-fold defaultstate="collapsed" desc="Set up color labels">   
        blackLabel = new JLabel();
        blackLabel.setBackground(Color.BLACK);
        blackLabel.setOpaque(true);

        redLabel = new JLabel();
        redLabel.setBackground(Color.RED);
        redLabel.setOpaque(true);

        orangeLabel = new JLabel();
        orangeLabel.setBackground(Color.ORANGE);
        orangeLabel.setOpaque(true);

        yellowLabel = new JLabel();
        yellowLabel.setBackground(Color.YELLOW);
        yellowLabel.setOpaque(true);

        greenLabel = new JLabel();
        greenLabel.setBackground(Color.GREEN);
        greenLabel.setOpaque(true);

        blueLabel = new JLabel();
        blueLabel.setBackground(Color.BLUE);
        blueLabel.setOpaque(true);

        indigoLabel = new JLabel();
        indigoLabel.setBackground(Color.CYAN);
        indigoLabel.setOpaque(true);

        violetLabel = new JLabel();
        violetLabel.setBackground(Color.MAGENTA);
        indigoLabel.setOpaque(true);

        blackInfoLabel = new JLabel("");
        redInfoLabel = new JLabel("");
        orangeInfoLabel = new JLabel("");
        yellowInfoLabel = new JLabel("");
        greenInfoLabel = new JLabel("");
        blueInfoLabel = new JLabel("");
        indigoInfoLabel = new JLabel("");
        violetInfoLabel = new JLabel("");

        currentFilter = "none";
        // </editor-fold>  

        // <editor-fold defaultstate="collapsed" desc="Set up picker items">   
        pickerTitle = new JLabel("Please pick a " + currentFilter, SwingConstants.LEFT);
        pickerTitle.setFont(new Font("Serif", Font.PLAIN, 12));
        pickerSubTitle = new JLabel(" to view: ", SwingConstants.LEFT);
        pickerSubTitle.setFont(new Font("Serif", Font.PLAIN, 12));

        comboBox = new JComboBox();
        comboBox.setPreferredSize(new Dimension(230, 35));
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        comboBox.setEnabled(false);

        // </editor-fold>  
        // <editor-fold defaultstate="collapsed" desc="Set up title area">
        filterTitle = new JLabel("Filter Bones");
        filterTitle.setFont(new Font("Serif", Font.PLAIN, 24));

        chooseFilter = new JLabel("Choose a filter...");
        chooseFilter.setFont(new Font("Serif", Font.PLAIN, 16));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Set up legend area">
        // setup some titles
        legendTitle = new JLabel("Legend");
        legendTitle.setFont(new Font("Serif", Font.PLAIN, 24));

        chosenFilter = new JLabel("Filtering by nothing");
        chosenFilter.setFont(new Font("Serif", Font.PLAIN, 18));

        JPanel legendBoxes = new JPanel();
        GridLayout labelPanelLayout = new GridLayout(0, 2); // 2 columns
        legendBoxes.setLayout(labelPanelLayout);
        legendBoxes.setPreferredSize(new Dimension(300, 200));
        legendBoxes.setMaximumSize(legendBoxes.getPreferredSize());

        // legend
        legendBoxes.add(blackLabel);
        legendBoxes.add(blackInfoLabel);
        legendBoxes.add(redLabel);
        legendBoxes.add(redInfoLabel);
        legendBoxes.add(orangeLabel);
        legendBoxes.add(orangeInfoLabel);
        legendBoxes.add(yellowLabel);
        legendBoxes.add(yellowInfoLabel);
        legendBoxes.add(orangeLabel);
        legendBoxes.add(orangeInfoLabel);
        legendBoxes.add(greenLabel);
        legendBoxes.add(greenInfoLabel);
        legendBoxes.add(blueLabel);
        legendBoxes.add(blueInfoLabel);
        legendBoxes.add(indigoLabel);
        legendBoxes.add(indigoInfoLabel);
        legendBoxes.add(violetLabel);
        legendBoxes.add(violetInfoLabel);

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Declare radio titles and buttons and events">
        setUpRadioButtons(); // an attempt at modularity...

        resetButton.setSelected(true);
        filterButtonGroup.add(elevationButton);
        filterButtonGroup.add(completenessButton);
        filterButtonGroup.add(dateFoundButton);
        filterButtonGroup.add(exposedSideButton);
        filterButtonGroup.add(genderButton);
        filterButtonGroup.add(lengthButton);
        filterButtonGroup.add(subelementButton);
        filterButtonGroup.add(taxonButton);
        filterButtonGroup.add(resetButton);

        buttonGroupPanel.add(resetButton);

        // quantifiable things
        buttonGroupPanel.add(elevationButton);
        buttonGroupPanel.add(lengthButton);
        buttonGroupPanel.add(dateFoundButton);
        // qualitative things
        buttonGroupPanel.add(completenessButton);
        buttonGroupPanel.add(exposedSideButton);
        buttonGroupPanel.add(genderButton);
        buttonGroupPanel.add(subelementButton);
        buttonGroupPanel.add(taxonButton);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Set alignments for boxlayouts">
        filterTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        filterTitle.setHorizontalAlignment(JLabel.LEFT);
        chooseFilter.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        chooseFilter.setHorizontalAlignment(JLabel.LEFT);

        resetButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        elevationButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        lengthButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        dateFoundButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        completenessButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        exposedSideButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        genderButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        subelementButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        taxonButton.setAlignmentX(JButton.LEFT_ALIGNMENT);

        legendTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        chosenFilter.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pickerTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pickerSubTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        comboBox.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        legendBoxes.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        // </editor-fold>      

        // add things to top panel
        topPanel.add(filterTitle);
        topPanel.add(chooseFilter);
        topPanel.add(resetButton);

        // quantitative options
        topPanel.add(elevationButton);
        topPanel.add(lengthButton);
        topPanel.add(dateFoundButton);

        // qualitative options
        topPanel.add(completenessButton);
        topPanel.add(exposedSideButton);
        topPanel.add(genderButton);
        topPanel.add(subelementButton);
        topPanel.add(taxonButton);

        // add things to bottom panel
        bottomPanel.add(legendTitle);
        bottomPanel.add(legendTitle);
        bottomPanel.add(chosenFilter);
        bottomPanel.add(pickerTitle);
        bottomPanel.add(pickerSubTitle);
        bottomPanel.add(comboBox);

        // add legend colors
        bottomPanel.add(legendBoxes);

        // finally put filter panel together
        filterPanel.add(topPanel);
        filterPanel.add(bottomPanel);

    }

    /**
     * Helper method to instantiate and add item listeners to radio buttons. 
     * Each radio button has a listener that will, if the button is not already
     * selected, call repaint and a method that will set up the GUI accordingly.
     *
     */
    private void setUpRadioButtons() {
        // each radio button is set up very similarly.
        taxonButton = new JRadioButton("Taxon");
        taxonButton.setFont(new Font("Serif", Font.PLAIN, 16));
        taxonButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                // make sure we don't do any more work than we need to
                if (currentFilter != "taxon") {
                    whichButton("taxon");  // set up GUI options for Taxon filters
                    repaint(); // repaint screen
                }
            }
        });

        subelementButton = new JRadioButton("Subelement");
        subelementButton.setFont(new Font("Serif", Font.PLAIN, 16));
        subelementButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "subelement") {
                    whichButton("subelement");
                    repaint();
                }
            }
        });

        completenessButton = new JRadioButton("Completeness");
        completenessButton.setFont(new Font("Serif", Font.PLAIN, 16));
        completenessButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "completeness") {
                    whichButton("completeness");
                    repaint();
                }
            }
        });

        exposedSideButton = new JRadioButton("Exposed Side");
        exposedSideButton.setFont(new Font("Serif", Font.PLAIN, 16));
        exposedSideButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "exposed") {
                    whichButton("exposed");
                    repaint();
                }
            }
        });

        genderButton = new JRadioButton("Gender");
        genderButton.setFont(new Font("Serif", Font.PLAIN, 16));
        genderButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "gender") {
                    whichButton("gender");
                    repaint();
                }
            }
        });

        dateFoundButton = new JRadioButton("Year");
        dateFoundButton.setFont(new Font("Serif", Font.PLAIN, 16));
        dateFoundButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "datefound") {
                    whichButton("datefound");
                    repaint();
                }
            }
        });

        elevationButton = new JRadioButton("Elevation");
        elevationButton.setFont(new Font("Serif", Font.PLAIN, 16));
        elevationButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "elevation") {
                    whichButton("elevation");
                    repaint();
                }
            }
        });

        lengthButton = new JRadioButton("Length");
        lengthButton.setFont(new Font("Serif", Font.PLAIN, 16));
        lengthButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (currentFilter != "length") {
                    whichButton("length");
                    repaint();
                }
            }
        });

        resetButton = new JRadioButton("No Filters");
        resetButton.setFont(new Font("Serif", Font.PLAIN, 16));
        resetButton.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                whichButton("none");
                repaint();
            }
        });
    }

    /**
     * Facilitates changing the GUI according to filter buttons through a
     * central location in the code. 
     *
     * @param button String containing the button just pressed
     */
    private void whichButton(String button) {
        currentFilter = button;
        updateOptionsAndLegendPanel();
    }

    /**
     * Uses a switch statement to update GUI according to current settings. Will
     * update combo boxes for some selections, or disable combo boxes for
     * others. Updates title labels and other things to make GUI match what it
     * should.
     */
    private void updateOptionsAndLegendPanel() {
        String filter;
        switch (currentFilter) {
            case "elevation":
                setElevationLegend();
                filter = currentFilter;
                chosenFilter.setText("Filtering by " + filter);
                pickerTitle.setVisible(false);
                pickerSubTitle.setVisible(false);
                comboBox.setEnabled(false);
                break;
            case "datefound":
                 filter = "year"; // we want things to look nice
                 setYearLegend();
                 chosenFilter.setText("Filtering by " + filter);
                pickerTitle.setVisible(false);
                pickerSubTitle.setVisible(false);
                comboBox.setEnabled(false);
                break;
            case "length":
                // reset important fields in range panel:
                /*if (currentFilter.equals("datefound")) {
                    filter = "year"; // we want things to look nice
                    setYearLegend();
                } else if (currentFilter.equals("elevation")) {
                    setElevationLegend();
                    filter = currentFilter;
                } */
                    setLengthLegend();
                    filter = currentFilter;
                //}

                // disable the picker stuff and set title there to reflect that
                chosenFilter.setText("Filtering by " + filter);
                pickerTitle.setVisible(false);
                pickerSubTitle.setVisible(false);
                comboBox.setEnabled(false);
                //disableAllColorLabels();

                break;
            case "taxon":
            case "subelement":
            case "completeness":
            case "exposed":
            case "gender":
                // reset important fields in picker panel:

                pickerTitle.setText("Please pick a " + currentFilter);
                pickerTitle.setVisible(true);

                // all of these are similar
                // check which filter is being applied
                if (currentFilter.equals("taxon")) {
                    chosenFilter.setText("Filtering by " + currentFilter);
                    pickerTitle.setText("Please pick a " + currentFilter);

                    // create new vector based on unique items found earlier
                    Vector v = new Vector(taxon);
                    v.add(0, "No Selection");
                    DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                    // set data souce of combo box to be list of unique items
                    comboBox.setModel(model);
                    // pick the first thing in the list as a default
                    comboBox.setSelectedItem("No Selection");
                    comboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            repaint();
                        }
                    });
                    pack();
                    setTaxonLegend(); // set legend accordingly
                } else if (currentFilter.equals("completeness")) {
                    chosenFilter.setText("Filtering by " + currentFilter);
                    pickerTitle.setText("Please pick a " + currentFilter);
                    Vector v = new Vector(completeness);
                    v.add(0, "No Selection");
                    DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                    comboBox.setModel(model);
                    comboBox.setSelectedIndex(0);
                    comboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            repaint();
                        }
                    });
                    setCompletenessLegend();
                } else if (currentFilter.equals("exposed")) {
                    chosenFilter.setText("Filtering by exposed side");
                    pickerTitle.setText("Please pick an exposed side");
                    Vector v = new Vector(expside);
                    v.add(0, "No Selection");
                    DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                    comboBox.setModel(model);
                    comboBox.setSelectedIndex(0);
                    comboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            repaint();
                        }
                    });
                    setExposedSideLegend();
                } else if (currentFilter.equals("gender")) {
                    chosenFilter.setText("Filtering by gender");
                    pickerTitle.setText("Please pick a " + currentFilter);
                    DefaultComboBoxModel model = new DefaultComboBoxModel(gender);
                    comboBox.setModel(model);
                    comboBox.setSelectedIndex(0);
                    comboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            repaint();
                        }
                    });
                    setGenderLegend();
                } else if (currentFilter.equals("subelement")) {
                    chosenFilter.setText("Filtering by " + currentFilter);
                    Vector v = new Vector(subelement);
                    DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                    comboBox.setModel(model);
                    comboBox.setSelectedIndex(0);
                    comboBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            repaint();
                        }
                    });
                    setSubElementLegend();
                }
                comboBox.setEnabled(true); // enable combo boxes
                pickerSubTitle.setVisible(true); // show subtitle
                break;
            case "none":
            default:
                // set things up so user can't mess with things when no
                // filter has been selected
                chosenFilter.setText("Filtering by nothing");
                comboBox.setEnabled(false);
                pickerTitle.setVisible(false);
                pickerSubTitle.setVisible(false);
                showAllColorBlocks();
                disableAllColorLabels();
                break;
        }
    }

    /**
     * Helper method to set up elevation legend for the filter panel.
     * Dynamically fills each of the colors and labels from the elevations
     * determined from the plot object (called obj).
     */        
    private void setElevationLegend() {
        // fill them in dynamically
        blackInfoLabel.setText(String.format("%.2f to %.2f", obj.elevations.get(0), obj.elevations.get(1)));
        redInfoLabel.setText(String.format("%.2f to %.2f", obj.elevations.get(1), obj.elevations.get(2)));
        orangeInfoLabel.setText(String.format("%.2f to %.2f", obj.elevations.get(2), obj.elevations.get(3)));
        yellowInfoLabel.setText(String.format("%.2f to %.2f", obj.elevations.get(3), obj.elevations.get(4)));
        greenInfoLabel.setText(String.format("%.2f to %.2f", obj.elevations.get(4), obj.elevations.get(5)));
        blueInfoLabel.setText(String.format("%.2f to %.2f", obj.elevations.get(5), obj.elevations.get(6)));

        // set the info and color block labels to true
        blackInfoLabel.setVisible(true);
        blackLabel.setVisible(true);
        blueLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);
        orangeInfoLabel.setVisible(true);
        orangeLabel.setVisible(true);
        greenInfoLabel.setVisible(true);
        greenLabel.setVisible(true);
        blueInfoLabel.setVisible(true);
        blueLabel.setVisible(true);

        // set things to false that we don't need
        indigoInfoLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Helper method to set up year legend for the filter panel.
     * Dynamically fills each of the colors and labels from the elevations
     * determined from the plot object (called obj).
     */  
    private void setYearLegend() {
        // fill them in dynically
        blackInfoLabel.setText(String.format("%.0f to %.0f", obj.years.get(0), obj.years.get(1)));
        redInfoLabel.setText(String.format("%.0f to %.0f", obj.years.get(1), obj.years.get(2)));
        orangeInfoLabel.setText(String.format("%.0f to %.0f", obj.years.get(2), obj.years.get(3)));
        yellowInfoLabel.setText(String.format("%.0f to %.0f", obj.years.get(3), obj.years.get(4)));
        greenInfoLabel.setText(String.format("%.0f to %.0f", obj.years.get(4), obj.years.get(5)));
        blueInfoLabel.setText(String.format("%.0f to %.0f", obj.years.get(5), obj.years.get(6)));

        // set things we need to visible
        blackInfoLabel.setVisible(true);
        blueLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);
        orangeInfoLabel.setVisible(true);
        orangeLabel.setVisible(true);
        greenInfoLabel.setVisible(true);
        greenLabel.setVisible(true);
        blueInfoLabel.setVisible(true);
        blueLabel.setVisible(true);

        // set things that we don't need to not visible
        indigoInfoLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Helper method to set up lengths legend for the filter panel.
     * Dynamically fills each of the colors and labels from the lengths
     * determined from the plot object (called obj).
     */  
    private void setLengthLegend() {
        // fill in lengths dynamically
        blackInfoLabel.setText(String.format("%.2f to %.2f", obj.lengths.get(0), obj.lengths.get(1)));
        redInfoLabel.setText(String.format("%.2f to %.2f", obj.lengths.get(1), obj.lengths.get(2)));
        orangeInfoLabel.setText(String.format("%.2f to %.2f", obj.lengths.get(2), obj.lengths.get(3)));
        yellowInfoLabel.setText(String.format("%.2f to %.2f", obj.lengths.get(3), obj.lengths.get(4)));
        greenInfoLabel.setText(String.format("%.2f to %.2f", obj.lengths.get(4), obj.lengths.get(5)));
        blueInfoLabel.setText(String.format("%.0f to %.0f", obj.lengths.get(5), obj.lengths.get(6)));

        // set things visible that should be visible
        blackInfoLabel.setVisible(true);
        blueLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);
        orangeInfoLabel.setVisible(true);
        orangeLabel.setVisible(true);
        greenInfoLabel.setVisible(true);
        greenLabel.setVisible(true);
        blueInfoLabel.setVisible(true);
        blueLabel.setVisible(true);

        // set things to not visible that shouldn't be.
        indigoInfoLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Set up colors and labels in legend to reflect gender filters. Fill in 
     * from hard-coded gender vector.
     */
    private void setGenderLegend() {
        // fill from gender vector 
        blackInfoLabel.setText(gender.get(1));
        redInfoLabel.setText(gender.get(2));
        yellowInfoLabel.setText(gender.get(3)); 
        
        // set things visible that should be
        blackInfoLabel.setVisible(true);
        blackLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);

        // turn off the rest...
        orangeInfoLabel.setVisible(false);
        orangeLabel.setVisible(false);
        greenInfoLabel.setVisible(false);
        greenLabel.setVisible(false);
        blueInfoLabel.setVisible(false);
        blueLabel.setVisible(false);
        indigoInfoLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Set up colors and labels in legend to reflect completeness filters. Fill
     * dynamically from completeness set generated from reading in files. 
     */
    private void setCompletenessLegend() {
        // create completeness vector from set of strings
        Vector completenessVector = new Vector(completeness);

        // dynamically fill in from completeness vector
        blackInfoLabel.setText(completenessVector.get(0).toString());
        redInfoLabel.setText(completenessVector.get(1).toString());
        yellowInfoLabel.setText(completenessVector.get(2).toString());
        orangeInfoLabel.setText(completenessVector.get(3).toString());
                
        // set things that should be visible
        blackInfoLabel.setVisible(true);
        blackLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);
        orangeInfoLabel.setVisible(true);
        orangeLabel.setVisible(true);

        // turn off the rest
        greenInfoLabel.setVisible(false);
        greenLabel.setVisible(false);
        blueInfoLabel.setVisible(false);
        blueLabel.setVisible(false);
        indigoInfoLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Set up colors and labels in legend to reflect taxon filters. Fill
     * dynamically from taxon set generated from reading in files. 
     */
    private void setTaxonLegend() {
        // get dynamic taxon set of strings
        Vector taxonVector = new Vector(taxon);

        blackInfoLabel.setText(taxonVector.get(0).toString());
        redInfoLabel.setText(taxonVector.get(1).toString());
        orangeInfoLabel.setText(taxonVector.get(2).toString());
        yellowInfoLabel.setText(taxonVector.get(0).toString());
        
        // set things visible that should be visible
        blackInfoLabel.setVisible(true);
        blackLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        orangeInfoLabel.setVisible(true);
        orangeLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);

        // turn off the rest...
        greenInfoLabel.setVisible(false);
        greenLabel.setVisible(false);
        blueInfoLabel.setVisible(false);
        blueLabel.setVisible(false);
        indigoInfoLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);

    }

    /**
     * Set up colors and labels in legend to turn off everything when selecting
     * sub elements. There are too many for the colors that we have available.
     */
    private void setSubElementLegend() {
        disableAllColorLabels();
        
        blackLabel.setVisible(false); 
        redLabel.setVisible(false);
        orangeLabel.setVisible(false);
        yellowLabel.setVisible(false);        
        greenLabel.setVisible(false);
        blueLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }
    
    /**
     * Set up colors and labels in legend to reflect exposed side filters. 
     * Options are dynamic to what has been filled in the expside set
     * of strings. Hides colors that aren't necessary.
     */
    private void setExposedSideLegend() {
        Vector exposedSideVector = new Vector(expside);

        // dyanmically fill in from exposed side set of strings
        blackInfoLabel.setText(exposedSideVector.get(0).toString());
        redInfoLabel.setText(exposedSideVector.get(1).toString());
        orangeInfoLabel.setText(exposedSideVector.get(2).toString());
        yellowInfoLabel.setText(exposedSideVector.get(3).toString());        
        greenInfoLabel.setText(exposedSideVector.get(4).toString());
        blueInfoLabel.setText(exposedSideVector.get(5).toString());;
        
        // set things to visible that should be visible
        blackInfoLabel.setVisible(true);
        blackLabel.setVisible(true);
        redInfoLabel.setVisible(true);
        redLabel.setVisible(true);
        orangeInfoLabel.setVisible(true);
        orangeLabel.setVisible(true);
        yellowInfoLabel.setVisible(true);
        yellowLabel.setVisible(true);
        greenInfoLabel.setVisible(true);
        greenLabel.setVisible(true);
        blueInfoLabel.setVisible(true);
        blueLabel.setVisible(true);
        
        // turn off violet since we don't need it
        violetInfoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Helper method to show all blocks of color within the filter pane.
     */
    private void showAllColorBlocks() {
        blackLabel.setVisible(false);
        redLabel.setVisible(false);
        orangeLabel.setVisible(false);
        yellowLabel.setVisible(false);
        greenLabel.setVisible(false);
        blueLabel.setVisible(false);
        indigoLabel.setVisible(false);
        violetLabel.setVisible(false);
    }

    /**
     * Helper method to hide all blocks of color within the filter pane.
     */
    private void disableAllColorLabels() {
        blackInfoLabel.setVisible(false);
        redInfoLabel.setVisible(false);
        orangeInfoLabel.setVisible(false);
        yellowInfoLabel.setVisible(false);
        greenInfoLabel.setVisible(false);
        blueInfoLabel.setVisible(false);
        indigoInfoLabel.setVisible(false);
        violetInfoLabel.setVisible(false);
    }

    
    /**
     * Method to facilitate user choosing a file to read in to the program. It
     * will only by default show files that are XML type. Then goes into read
     * library per usual and sets up the GUI for bone bed exploration.
     * 
     * @param evt Event sent in from button
     */    
    private void chooseFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("XML Documents", "xml"));
        fc.setAcceptAllFileFilterUsed(true);
        fc.showOpenDialog(this);
        String fileName = fc.getSelectedFile().getName();
        String directory = fc.getSelectedFile().getAbsolutePath();
        path = fc.getSelectedFile().getParent();
        fileSelected.setText("File Selected: " + fileName);

        // read the library
        readLibrary(directory);
        contents.add(obj, BorderLayout.CENTER);
        filterButton.setEnabled(false);
        chooseFileButton.setEnabled(false);
        enableAllFilterButtons();
        pack();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bone Bed Viewer");
        setPreferredSize(new java.awt.Dimension(1000, 800));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 975, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Read in file with all the bones, and then read in each individual file.
     * Also parses walkway. 
     *
     * @param directory the directory containing the .xml file
     */
    public void readLibrary(String directory) {

        Bonerec_Parser descParser = new Bonerec_Parser(directory); // Parse bones.xml
        bone_records = descParser.parse();

        // walk through all the bones that we have, open them up 
        for (int i = 0; i < bone_records.size(); i++) {

            fill(i);
            // get the file name
            if (file_choose == false) {
                boneFile = System.getProperty("user.dir") + "/bonexml/" + bone_records.get(i).unique_id + ".xml";
            } else {
                boneFile = path + "/" + bone_records.get(i).unique_id + ".xml";
            }

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                
                /* document doesn't get used here, but is necessary for 
                try/catch loop in parse */
                Document document = builder.parse(boneFile);
                // parse this bone
                parseBone = new Bone_Description_Parser(boneFile);
                parseBone.parse(); // return one <uniqueid>.xml

                // add it to the master bone_descriptions list
                bone_descriptions.add(parseBone.bone_description);
            } catch (Exception e) {
                // do this in console in case there are many files.
                System.out.println("File not found -" + bone_records.get(i).unique_id + ".xml");
            }
        }
        // declare an object and bring in walkway from established path.
        Walkway_Parser walk_object;
        if (file_choose == false) {
            walk_object = new Walkway_Parser((System.getProperty("user.dir")) + "/bonexml/walkway.xml");
            walk_object.parse();
        } else {
            walk_object = new Walkway_Parser(path + "/walkway.xml");
            walk_object.parse();
        }
        
        // get all unique values from files
        unique_list();
        walk_detail.add(walk_object.walk_details);
        obj = new Plot(this);

    }
    
    /**
     * Creates lists of unique items for each record. This is used later in 
     * filtering and creating a dynamic GUI.
     *
     */
    public void unique_list() {
        elev_max = Double.parseDouble(filters.get(0).elevation);
        elev_min = Double.parseDouble(filters.get(0).elevation);
        element_min = Double.parseDouble(filters.get(0).element);
        element_max = Double.parseDouble(filters.get(0).element);
        date_max = Integer.parseInt(filters.get(0).year);
        date_min = Integer.parseInt(filters.get(0).year);
        length_max = Double.parseDouble(filters.get(0).length);
        length_min = Double.parseDouble(filters.get(0).length);

        for (int i = 0; i < filters.size(); i++) {

            if (filters.get(i).completeness != null) {
                completeness.add(filters.get(i).completeness);
            }

            if (filters.get(i).subelement != null) {
                subelement.add(filters.get(i).subelement);
            }

            if (filters.get(i).taxon != null) {
                taxon.add(filters.get(i).taxon);
            }

            if (filters.get(i).element != null) {
                element.add(filters.get(i).element);
            }

            if (filters.get(i).exposed_side != null) {
                expside.add(filters.get(i).exposed_side);
            }

            if (filters.get(i).elevation != null) {
                if (elev_max < Double.parseDouble(filters.get(i).elevation)) {
                    elev_max = Double.parseDouble(filters.get(i).elevation);
                }
                if (elev_min > Double.parseDouble(filters.get(i).elevation)) {
                    elev_min = Double.parseDouble(filters.get(i).elevation);
                }
            }

            if (filters.get(i).year != null) {
                if (date_max < Integer.parseInt(filters.get(i).year)) {
                    date_max = Integer.parseInt(filters.get(i).year);
                }
                if (date_min > Integer.parseInt(filters.get(i).year)) {
                    date_min = Integer.parseInt(filters.get(i).year);
                }
            }

            if (filters.get(i).length != null) {
                if (length_max < Double.parseDouble(filters.get(i).length)) {
                    length_max = Double.parseDouble(filters.get(i).length);
                }
                if (length_min > Double.parseDouble(filters.get(i).length)) {
                    length_min = Double.parseDouble(filters.get(i).length);
                }
            }

            if (filters.get(i).element != null) {
                if (element_max < Double.parseDouble(filters.get(i).element)) {
                    element_max = Double.parseDouble(filters.get(i).element);
                }
                if (element_min > Double.parseDouble(filters.get(i).element)) {
                    element_min = Double.parseDouble(filters.get(i).element);
                }
            }

        }
    }

    /**
     * Used when records are being read in to also fill in Filter_Search object
     * with all bone records.
     *
     * @param i current index of bone_record being read in and sent to FilterSearch
     */
    public void fill(int i) {

        Filter_Search temp = new Filter_Search();

        temp.year = bone_records.get(i).year;
        temp.completeness = bone_records.get(i).completeness;
        temp.element = bone_records.get(i).element;
        temp.elevation = bone_records.get(i).elevation;
        temp.exposed_side = bone_records.get(i).expside;
        temp.gender = bone_records.get(i).gender;
        temp.id = bone_records.get(i).unique_id;
        temp.subelement = bone_records.get(i).sub_element;
        temp.taxon = bone_records.get(i).taxon;
        temp.length = bone_records.get(i).shape_length;

        filters.add(temp);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        mainForm gui = new mainForm();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public javax.swing.JButton chooseFileButton;
    private javax.swing.JLabel fileSelected;
    public javax.swing.JButton filterButton;
    public JPanel toolBar;
    private Container contents;
    public JPanel filterPanel;
    public Plot obj;

    private JLabel filterTitle;
    private JLabel chooseFilter;
    public JRadioButton taxonButton;
    public JRadioButton subelementButton;
    public JRadioButton completenessButton;
    public JRadioButton exposedSideButton;
    public JRadioButton genderButton;
    public JRadioButton dateFoundButton;
    public JRadioButton elevationButton;
    public JRadioButton lengthButton;
    public JRadioButton resetButton;
    private JLabel legendTitle;
    public JLabel chosenFilter;
    public GridLayout filterGroupLayout = new GridLayout(0, 1);

    private JLabel blackLabel;
    private JLabel redLabel;
    private JLabel orangeLabel;
    private JLabel yellowLabel;
    private JLabel greenLabel;
    private JLabel blueLabel;
    private JLabel indigoLabel;
    private JLabel violetLabel;

    public JLabel blackInfoLabel;
    public JLabel redInfoLabel;
    public JLabel orangeInfoLabel;
    public JLabel yellowInfoLabel;
    public JLabel greenInfoLabel;
    public JLabel blueInfoLabel;
    public JLabel indigoInfoLabel;
    public JLabel violetInfoLabel;

    public String currentFilter;
    public JSlider slider;
    public JPanel topPanel;
    public JPanel bottomPanel;

    public JLabel rangeSubTitle;
    public JLabel pickerSubTitle;
    public JLabel pickerTitle;
    JComboBox comboBox;
}
