import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.GraphiteAquaSkin;

import javax.swing.*;
import java.awt.*;

/**
 * DisplayGraphFrame: Display the relation graph based on the result.
 */
public class DisplayGraphFrame extends JFrame {
    public static JFrame displayFrame = new JFrame();
    // Constructor
    public DisplayGraphFrame(SubstanceSkin skin) {
    }
    // display
    public static void display(){
        //create components.
        JButton btnSaveGraph = new JButton();
        JButton btnQuit = new JButton();
        JPanel pnlGraph = new JPanel();
        JLabel l = new JLabel();
        ImageIcon icon=new ImageIcon("./saved/10.jpg");
        int g_nWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int g_nHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        //int maxWidth = (int) (g_nWidth * 0.7);
        //int maxHeight =  (int) (maxWidth *(icon.getIconHeight() * 1.0 /icon.getIconWidth()));
        //965*750
        l.setIcon(icon);
        l.setBounds(0, 0, icon.getIconWidth(),icon.getIconHeight());
        pnlGraph.setLayout(new BorderLayout());
        pnlGraph.add(l,BorderLayout.CENTER);
        //layout design.
        int finalWidth = icon.getIconWidth()+200;
        int finalHeight = Math.min(icon.getIconHeight()+50, g_nHeight-50);
        displayFrame.setLayout(null);
        displayFrame.setSize(finalWidth,finalHeight);
        displayFrame.setTitle("Graph");
        CrawlerFrame.setAndAddButton(displayFrame,btnSaveGraph,"SaveGraph", Color.lightGray,finalWidth-150,18,116,40);
        CrawlerFrame.setAndAddButton(displayFrame,btnQuit,"Quit",Color.lightGray,finalWidth-150,72,116,40);
        CrawlerFrame.setAndAdd(displayFrame,pnlGraph,10,25,finalWidth-180,finalHeight-20);
        btnSaveGraph.addActionListener((e)->{

        });
        btnQuit.addActionListener((e)->{
            displayFrame.setVisible(false);
        });
        //set the close button
        displayFrame.setVisible(true);
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setResizable(false);
    }
}
