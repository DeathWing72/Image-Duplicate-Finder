import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
@SuppressWarnings("serial")
public class CompareFrame extends JFrame
{
    public boolean isMatch, buttonPressed;
    public CompareFrame(File f1,File f2,double i)
    {
    	this.setExtendedState(Frame.MAXIMIZED_BOTH);
    	Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        
        JPanel topPane = new JPanel();
        topPane.setPreferredSize(new Dimension(1600,30));
        topPane.setLayout(new GridLayout(1,2));
        cp.add(topPane,BorderLayout.PAGE_START);
        
        JLabel nameLabel1 = new JLabel(f1.getName(),SwingConstants.CENTER);
        topPane.add(nameLabel1);
        JLabel nameLabel2 = new JLabel(f2.getName(),SwingConstants.CENTER);
        topPane.add(nameLabel2);
        
        JPanel cenPane = new JPanel();
        cenPane.setPreferredSize(new Dimension(1600,900));
        cenPane.setLayout(new GridLayout(1,2));
        cp.add(cenPane,BorderLayout.CENTER);
        
        ImagePanel img1 = new ImagePanel(f1);
        img1.setPreferredSize(ImageDuplicateFinder.getDims(f1));
        JScrollPane scrollFrame1 = new JScrollPane(img1);
        img1.setAutoscrolls(true);
        scrollFrame1.setPreferredSize(new Dimension(800,450));
        cenPane.add(scrollFrame1);
        ImagePanel img2 = new ImagePanel(f2);
        img2.setPreferredSize(ImageDuplicateFinder.getDims(f2));
        JScrollPane scrollFrame2 = new JScrollPane(img2);
        img2.setAutoscrolls(true);
        scrollFrame2.setPreferredSize(new Dimension(800,450));
        cenPane.add(scrollFrame2);
        
        JPanel botPane = new JPanel();
        botPane.setPreferredSize(new Dimension(1600,70));
        botPane.setLayout(new GridLayout(1,3));
        cp.add(botPane,BorderLayout.PAGE_END);
        
        JButton matchButton = new JButton("Match");
        botPane.add(matchButton);
        JLabel indexLabel = new JLabel(Double.toString(i),SwingConstants.CENTER);
        botPane.add(indexLabel);
        JButton noMatchButton = new JButton("No Match");
        botPane.add(noMatchButton);
        
        matchButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            buttonPressed = true;
            isMatch=true;
            setVisible(false);
            dispose();
         }
        });
        noMatchButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            buttonPressed = true;
            setVisible(false);
            dispose();
         }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Image Comparison"); // "super" JFrame sets title
        setSize(1600, 1000);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
    }
    public static void main(File f1,File f2,double i)
    {
        // Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompareFrame(f1,f2,i); // Let the constructor do the job
            }
        });
    }
}