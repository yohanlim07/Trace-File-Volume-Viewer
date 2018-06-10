import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.*;

public class graphPanel extends JPanel{

    private ArrayList<Double> timestamp;
    private ArrayList<String> sourceIPAddress;
    private ArrayList<String> destIPAddress;
    private ArrayList<Integer> packetSize;
    private ArrayList<String> fullSourceIPAddress;
    private ArrayList<String> fullDestIPAddress;

    public graphPanel(ArrayList<String> sourceIPAddress, ArrayList<String> destIPAddress, ArrayList<Integer> packetSize,
                      ArrayList<String> fullSourceIPAddress, ArrayList<String> fullDestIPAddress){

        this.sourceIPAddress = sourceIPAddress;
        this.destIPAddress = destIPAddress;
        this.packetSize = packetSize;
        this.fullSourceIPAddress = fullSourceIPAddress;
        this.fullDestIPAddress = fullDestIPAddress;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawLine(55,275,960,275);
        g.drawLine(60,275,60,25);
        for (int i = 60; i <=960; i+=75) {
            g.drawLine(i, 275, i, 280);
        }
        g.drawString("Volume [bytes]", 5,20);
        g.drawString("Time [s]", 450,315);
        g.drawString("0", 56,293);
        g.drawString("50", 129,293);
        g.drawString("100", 200,293);
        g.drawString("150", 275,293);
        g.drawString("200", 350,293);
        g.drawString("250", 425,293);
        g.drawString("300", 500,293);
        g.drawString("350", 575,293);
        g.drawString("400", 650,293);
        g.drawString("450", 725,293);
        g.drawString("500", 800,293);
        g.drawString("550", 875,293);
        g.drawString("600", 950,293);


    }
}
