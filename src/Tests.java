import java.util.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
public class Tests
{
    public static void filePrintTest(File[] files)
    {
        for(File f: files)
        {
            System.out.println(f.getName());
        }
    }
    public static void dimsOrderTest(File[][] f)
    {
        for(File[] files: f)
        {
            for(File file: files)
            {
                System.out.println(file.getName());
            }
        }
    }
    public static void readerFileSuffixes()
    {
        for(String str: ImageIO.getReaderFileSuffixes())
        {
            System.out.println(str);
        }
    }
    public static void imageReaderTest()
    {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
        while (readers.hasNext())
        {
            System.out.println("reader: " + readers.next());
        }
    }
    public static void testCompareFrame()
    {
        File f1 = new File("C:\\Users\\DeathWing72\\Pictures\\Saved Pictures\\0dmpja7igyi11.jpeg");
        File f2 = new File("C:\\Users\\DeathWing72\\Pictures\\Saved Pictures\\mcafee virus.PNG");
        @SuppressWarnings("unused")
		CompareFrame cf = new CompareFrame();
        cf.updateFrame(f1, f2, 1.0, 1, 1);
    }
}