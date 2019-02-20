import java.awt.*;
import javax.swing.*;
@SuppressWarnings("serial")
public class ProgressFrame extends JFrame
{
	public ProgressFrame()
	{
		Container cp = getContentPane();
		JPanel topPane = new JPanel();
		topPane.setLayout(new GridLayout(2,1));
		cp.add(topPane);
		JLabel progLabel1 = new JLabel("Image Processing in Progress...",SwingConstants.CENTER);
        topPane.add(progLabel1);
        JLabel progLabel2 = new JLabel("Please Wait...",SwingConstants.CENTER);
        topPane.add(progLabel2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program if close-window button clicked
        setTitle("Image Duplicate Finder"); // "super" JFrame sets title
        setSize(300, 100);        // "super" JFrame sets initial size
        setVisible(true);          // "super" JFrame shows
	}
	public void endProgress()
	{
		setVisible(false);
		dispose();
	}
	public static void main()
	{
	    // Run the GUI construction in the Event-Dispatching thread for thread-safety
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            new ProgressFrame(); // Let the constructor do the job
	        }
	    });
	}
}
