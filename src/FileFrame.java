import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.SwingUtilities;
import java.io.File;
@SuppressWarnings({ "unused", "serial" })
public class FileFrame extends JFrame
{
    static private final String newline = "\n";
    JFileChooser fc;
    File file;
    public FileFrame()
    {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(FileFrame.this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            file = fc.getSelectedFile();
        }
        else if(returnVal == JFileChooser.CANCEL_OPTION)
        {
        	System.exit(0);
        }
    }
    public static void main() {
        // Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileFrame(); // Let the constructor do the job
            }
        });
    }
}