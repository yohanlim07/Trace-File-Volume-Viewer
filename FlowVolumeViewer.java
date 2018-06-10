import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class FlowVolumeViewer extends JFrame implements ActionListener, ItemListener{

    private String currentLine;

    private ArrayList<Double> timestamp;
    private ArrayList<String> sourceIPAddress;
    private ArrayList<String> destIPAddress;
    private ArrayList<Integer> packetSize;
    private ArrayList<String> fullSourceIPAddress;
    private ArrayList<String> fullDestIPAddress;
    private ArrayList<Integer> orderedPacketSize;
    private ArrayList<Integer> orderedTimestamp;


    private Font font;
    private int selectedIndex;

    private JPanel radioButtonPanel;
    private JRadioButton radioButtonSource;
    private JRadioButton radioButtonDestination;
    private ButtonGroup radioButtons;
    private JComboBox sourceaddressComboBox;
    private JComboBox destaddressComboBox;

    public FlowVolumeViewer() {
        super("Flow Volume Viewer");
        setLayout(null);
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        font = new Font("Sans-Serif", Font.PLAIN, 20);
        setVisible(true);

        graphPanel p = new graphPanel(sourceIPAddress, destIPAddress, packetSize, fullSourceIPAddress, fullDestIPAddress);
        p.setSize(1000,325);
        p.setBackground(Color.white);
        p.setLocation(0,100);
        add(p);


        setupArrayLists();
        setupMenu();
        setupRadioButtons();
        setupSourceComboBox();
        setupDestComboBox();
        setupGraph();
    }

    private void setupArrayLists() {
        timestamp = new ArrayList<Double>();
        sourceIPAddress = new ArrayList<String>();
        destIPAddress = new ArrayList<String>();
        packetSize = new ArrayList<Integer>();
        fullDestIPAddress = new ArrayList<String>();
        fullSourceIPAddress = new ArrayList<String>();
        orderedPacketSize = new ArrayList<Integer>();
        orderedTimestamp = new ArrayList<Integer>();
    }


    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.setFont(font);
        menuBar.add(fileMenu);

        JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
        fileMenuOpen.setFont(font);
        fileMenuOpen.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser(".");
                        int txtfile = fileChooser.showOpenDialog(null);
                        try {
                            if (txtfile == JFileChooser.APPROVE_OPTION) {

                                timestamp.clear();
                                sourceIPAddress.clear();
                                destIPAddress.clear();
                                packetSize.clear();
                                fullDestIPAddress.clear();
                                fullSourceIPAddress.clear();
                                orderedPacketSize.clear();
                                orderedTimestamp.clear();
                                sourceaddressComboBox.removeAllItems();
                                destaddressComboBox.removeAllItems();

                                File f = fileChooser.getSelectedFile();
                                Scanner fulltext = new Scanner(f);
                                while (fulltext.hasNextLine()){
                                    currentLine = fulltext.nextLine();
                                    String array1[]= currentLine.split("\t");

                                    timestamp.add(Double.parseDouble((array1[1])));
                                    fullSourceIPAddress.add(array1[2]);
                                    fullDestIPAddress.add(array1[4]);
                                    try{
                                        packetSize.add(Integer.parseInt(array1[7]));
                                    }
                                    catch(Exception i){
                                        packetSize.add(0);
                                    }

                                    if (!sourceIPAddress.contains(array1[2])){
                                        sourceIPAddress.add(array1[2]);
                                    }
                                    if(!destIPAddress.contains(array1[4])){
                                        destIPAddress.add(array1[4]);
                                    }
                                }

                                if (sourceIPAddress.contains("")){
                                    sourceIPAddress.remove("");
                                }
                                if (destIPAddress.contains("")){
                                    destIPAddress.remove("");
                                }

                                Collections.sort(sourceIPAddress);
                                Collections.sort(destIPAddress);

                                for(int j = 0; j < sourceIPAddress.size(); j++){
                                    sourceaddressComboBox.addItem(sourceIPAddress.toArray()[j]);
                                }
                                for(int j = 0; j < destIPAddress.size(); j++){
                                    destaddressComboBox.addItem(destIPAddress.toArray()[j]);
                                }
                                sourceaddressComboBox.setVisible(true);
                            }
                        }
                        catch (FileNotFoundException u){
                            JOptionPane.showMessageDialog(null, "Invalid file", "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
        );
        fileMenu.add(fileMenuOpen);

        JMenuItem fileMenuQuit = new JMenuItem("Quit");
        fileMenuQuit.setFont(font);
        fileMenuQuit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );
        fileMenu.add(fileMenuQuit);

    }


    private void setupRadioButtons() {
        radioButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        radioButtonPanel.setLocation(0,0);
        radioButtonPanel.setSize(200,100);
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;


        radioButtons = new ButtonGroup();

        radioButtonSource = new JRadioButton("Source hosts");
        radioButtonSource.setFont(font);
        radioButtonSource.setSelected(true);
        radioButtons.add(radioButtonSource);
        radioButtonPanel.add(radioButtonSource, c);

        radioButtonDestination = new JRadioButton("Destination hosts");
        radioButtonDestination.setFont(font);
        radioButtons.add(radioButtonDestination);
        radioButtonPanel.add(radioButtonDestination, c);

        add(radioButtonPanel);
        radioButtonSource.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (radioButtonSource.isSelected()){
                            sourceaddressComboBox.setVisible(true);
                            destaddressComboBox.setVisible(false);
                        }
                    }
                }
        );

        radioButtonDestination.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (radioButtonDestination.isSelected()){
                            destaddressComboBox.setVisible(true);
                            sourceaddressComboBox.setVisible(false);
                        }
                    }
                }
        );
    }


    public void setupSourceComboBox() {
        sourceaddressComboBox = new JComboBox<String>();
        sourceaddressComboBox.setModel((MutableComboBoxModel<String>) sourceaddressComboBox.getModel());
        sourceaddressComboBox.setMaximumRowCount(20);
        sourceaddressComboBox.setFont(font);
        sourceaddressComboBox.setEnabled(true);
        sourceaddressComboBox.setVisible(false);
        sourceaddressComboBox.setBounds(200,30,300,25);
        add(sourceaddressComboBox);

        sourceaddressComboBox.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent i1){
                        if (radioButtonSource.isSelected()){
                            selectedIndex = sourceaddressComboBox.getSelectedIndex();
                        }
                    }
                }
        );
    }

    public void setupDestComboBox(){
        destaddressComboBox = new JComboBox<String>();
        destaddressComboBox.setModel((MutableComboBoxModel<String>) destaddressComboBox.getModel());
        destaddressComboBox.setMaximumRowCount(20);
        destaddressComboBox.setFont(font);
        destaddressComboBox.setEnabled(true);
        destaddressComboBox.setVisible(false);
        destaddressComboBox.setBounds(200,30,300,25);
        add(destaddressComboBox);

        destaddressComboBox.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent i2){
                        if (radioButtonDestination.isSelected()){
                            selectedIndex = destaddressComboBox.getSelectedIndex();
                        }
                    }
                }
        );
    }

    public void setupGraph(){
        int currentPacketsize = 0;
        int minrange = 0;
        int maxrange = 2;
        for (int i = 0; i < packetSize.size(); i++){
            if (minrange <= timestamp.get(i).intValue() && timestamp.get(i).intValue() <= maxrange){
                currentPacketsize += orderedPacketSize.get(i);
            }
            else{
                orderedTimestamp.add(minrange);
                minrange += 2;
                maxrange += 2;
                orderedPacketSize.add(currentPacketsize);
                currentPacketsize = 0;
            }
        }
    }


    public void actionPerformed(ActionEvent e){
    }
    public void itemStateChanged(ItemEvent i){
    }
}