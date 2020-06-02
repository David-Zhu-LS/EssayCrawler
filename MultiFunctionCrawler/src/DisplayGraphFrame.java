import org.pushingpixels.substance.api.SubstanceSkin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * DisplayGraphFrame: Display the relation graph based on the result.
 */
public class DisplayGraphFrame extends JFrame {

    // Constructor
    public DisplayGraphFrame(SubstanceSkin skin) {
    }

    // display
    public static void display(Graph G) {
        if(G == null) return;
        if(G.nodeCnt == 0) return;
        JFrame displayFrame = new JFrame();
        //create components.
        JButton btnSaveGraph = new JButton();
        JButton btnQuit = new JButton();
        JPanel pnlGraph = new JPanel();
        JLabel l = new JLabel();
        JLabel lblInfo = new JLabel("<html>Red means more cited.<br/>Blue means less cited.</html>");
        try {
            MakeGraph.generate(G);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(new File("./saved/graph.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int g_nWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int g_nHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        //int maxWidth = (int) (g_nWidth * 0.7);
        //int maxHeight =  (int) (maxWidth *(icon.getIconHeight() * 1.0 /icon.getIconWidth()));
        //965*750
        l.setIcon(icon);
        l.setVisible(true);
        //l.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        //layout design.
        int finalWidth = Math.min(icon.getIconWidth() + 180, g_nWidth - 50);
        int finalHeight = Math.min(icon.getIconHeight() + 80, g_nHeight - 50);
        displayFrame.setLayout(new BorderLayout(0,0));
        displayFrame.setSize(finalWidth, finalHeight);
        displayFrame.setTitle("Graph");
        pnlGraph.setLayout(new GridLayout(3,1));
        setButton2(displayFrame, btnSaveGraph, "SaveGraph", Color.lightGray, finalWidth - 150, 18, 116, 40);
        setButton2(displayFrame, btnQuit, "Quit", Color.lightGray, finalWidth - 150, 72, 116, 40);
        pnlGraph.add(btnSaveGraph);
        pnlGraph.add(btnQuit);
        setLabel2(lblInfo, 520, 126, 250, 300);
        pnlGraph.add(lblInfo);
        lblInfo.setSize(250, 150);
        //CrawlerFrame.setAndAdd(displayFrame, l, 10, 25, finalWidth - 180, finalHeight - 20);
        //displayFrame.getContentPane().add(l);
        //pnlGraph.setLayout(new BorderLayout(0,0));
        //pnlGraph.setBorder(new EmptyBorder(5, 5, 5, 5));
        //pnlGraph.add(l,BorderLayout.CENTER);
        JScrollPane scroller = new JScrollPane(l);// set the scrollbar
        scroller.setBounds(10, 25, finalWidth - 180, finalHeight - 20);
        //scroller.setViewportView(l);
        scroller.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//horizontal one always appears
        scroller.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//vertical one always appears
        displayFrame.getContentPane().add(scroller,BorderLayout.CENTER);
        displayFrame.getContentPane().add(pnlGraph,BorderLayout.EAST);
        btnSaveGraph.addActionListener((e) -> {
            JOptionPane.showMessageDialog(null, "Saved successfully!");
            displayFrame.setVisible(false);
        });
        btnQuit.addActionListener((e) -> {
            File file = new File("./saved/graph.png");
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Not save.");
                } else {
                    System.out.println("Save it.");
                }
            }
            displayFrame.setVisible(false);
        });
        //set the close button
        displayFrame.setVisible(true);
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setResizable(false);
    }
    // Set and add the button to the frame.
    public static void setButton2(JFrame dst, JButton button, String name, Color color, int posx, int posy, int width, int height) {
        button.setText(name);
        button.setBackground(color);
        button.setBounds(posx, posy, width, height);
        // Text displayed at the center
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
    }
    // Set and add the component to the frame.
    public static void setLabel2(JComponent component, int posx, int posy, int width, int height) {
        component.setFont(new Font("Dialog", Font.PLAIN, 15));
        component.setBounds(posx, posy, width, height);
    }
}
