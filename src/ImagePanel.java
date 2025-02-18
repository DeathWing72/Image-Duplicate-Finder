import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{
    private BufferedImage image;
    public ImagePanel(File f) {
        try {                
           image = ImageIO.read(f);
        } catch (IOException ex) {}
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }
}