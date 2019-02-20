import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
public class ImageDuplicateFinder
{
	private static final double THRESHOLD = .88;
	static FileOutput fOut = new FileOutput(), fLog = new FileOutput();
    public static void main(String[] args)
    {
        FileFrame frame = new FileFrame();
        File subDir = new File(frame.file.getAbsoluteFile(), "Analysis Results");
        subDir.mkdir();
        fOut.setFile(frame.file,subDir.getAbsolutePath(),frame.file.getName(),".csv");
        fLog.setFile(frame.file, subDir.getAbsolutePath(), frame.file.getName()+" Analysis Data Log", ".txt");
        File[] files = getFile(frame.file);
        File[][] sortFiles = imageSort(files);
        PotMatches[] pm = findMatches(sortFiles);
        confirmMatches(pm);
        fOut.endPrint();
        fLog.endPrint();
        System.exit(0);
    }
    public static File[] getFile(File dir)
    {
    	System.err.println(timeStamp() + dir.getAbsoluteFile()); //Print folder path to err dialog
        fLog.newPrintln(timeStamp() + dir.getAbsolutePath());
        File[] dirContents = dir.listFiles(); //Initialize File array
        ArrayList<File> files = new ArrayList<File>();
        for(int fn = 0; fn < dirContents.length; fn++)
        {
            String fileName = dirContents[fn].getName(); //Save file name as a String
            if(!dirContents[fn].isDirectory() && (fileName.contains(".jpg") || fileName.contains(".JPG") || fileName.contains(".jpeg") || fileName.contains(".png") || fileName.contains(".PNG")))
            {
                files.add(dirContents[fn]); //Add File object to files ArrayList
            }
        }
        return files.toArray(new File[files.size()]);
    }
    public static File[][] imageSort(File[] files) //Sorts images by aspect ratio
    {
        /**
    	*Sort images into sub-arrays by dimensions (resolution and aspect ratio)
        *Match images of identical aspect ratio and send them for rescaling
        */
        ArrayList<Double> resIndex = new ArrayList<Double>();
        for(File file: files)
        {
            if(!resIndex.contains(getAR(file)))
            {
                resIndex.add(getAR(file));
            }
        }
        double[] ratIndex = arSort(resIndex); //Sort aspect ratio array from small to large
        ArrayList<ArrayList<File>> ratios = new ArrayList<ArrayList<File>>(ratIndex.length); //Initializes new ArrayList of File ArrayLists
        for(@SuppressWarnings("unused") double rat: ratIndex)
        {
            ratios.add(new ArrayList<File>());
        }
        for(File file: files)
        {
            for(int x = 0; x < ratIndex.length; x++)
            {
                if(ratIndex[x] == getAR(file))
                {
                    ratios.get(x).add(file);
                    break;
                }
            }
        }
        File[][] sortedImages = new File[ratios.size()][];
        for(int x = 0; x < sortedImages.length; x++)
        {
            sortedImages[x] = ratios.get(x).toArray(new File[ratios.get(x).size()]);
        }
        return sortedImages;
    }
    public static double[] arSort(ArrayList<Double> r)
    {
        double[] ratios = new double[r.size()];
        for(int x = 0; x < ratios.length; x++)
        {
            ratios[x] = r.get(x);
        }
        int len = ratios.length;
        for(int x = 0; x < len-1; x++)
        {
            int minDex = x;
            for(int i = x+1; i < len; i++)
            {
                if(ratios[i] < ratios[minDex])
                {
                    minDex = i;
                }
            }
            double tmp = ratios[minDex];
            ratios[minDex] = ratios[x];
            ratios[x] = tmp;
        }
        return ratios;
    }
    public static PotMatches[] findMatches(File[][] f)
    {
        ArrayList<PotMatches> matches = new ArrayList<PotMatches>();
        for(File[] files: f)
        {
            System.err.println(timeStamp() + "New Aspect Ratio - "+getAR(files[0]));
            fLog.newPrintln(timeStamp() + "New Aspect Ratio - "+getAR(files[0]));
            if(f.length > 1)
            {
                System.err.println(timeStamp() + "Beginning Comparisons in Aspect Ratio: "+getAR(files[0]));
                fLog.newPrintln(timeStamp() + "Beginning Comparisons in Aspect Ratio: "+getAR(files[0]));
                for(int x=0;x<files.length-1;x++)
                {
                    System.err.println(timeStamp() + "x: "+files[x].getName());
                    fLog.newPrintln(timeStamp() + "x: "+files[x].getName());
                    for(int y=x+1;y<files.length;y++)
                    {
                        System.err.println(timeStamp() + "x: "+files[x].getName()+"  y: "+files[y].getName());
                        fLog.newPrintln(timeStamp() + "x: "+files[x].getName()+"  y: "+files[y].getName());
                        double simIndex = 0;
                        try
                        {
                            SsimCalculator ssim = new SsimCalculator(files[x]);
                            simIndex = ssim.compareTo(files[y]);
                        } catch(SsimException | IOException e) {}
                        if(simIndex >= THRESHOLD)
                        {
                            matches.add(new PotMatches(files[x],files[y],simIndex));
                            System.err.println(timeStamp() + "New Potential Match - " + simIndex);
                            fLog.newPrintln(timeStamp() + "New Potential Match - " + String.valueOf(simIndex));
                        }
                    }
                }
            }
        }
        return matches.toArray(new PotMatches[matches.size()]);
    }
    public static void confirmMatches(PotMatches[] files)
    {
    	System.out.println("File Name a,File Dimensions a,File Name b,File Dimensions b,Similarity Index,Match? (No-0/Yes-1)");
        fOut.newPrintln("File Name a,File Dimensions a,File Name b,File Dimensions b,Similarity Index,Match? (No-0/Yes-1)");
        for(int i=0;i < files.length;i++)
    	{
        	CompareFrame frame = new CompareFrame(files[i].f1,files[i].f2,files[i].simIndex);
            while(!frame.buttonPressed)
            {
                try
                {
                    Thread.sleep(1);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
            printMatch(files[i].f1,files[i].f2,files[i].simIndex);
            if(frame.isMatch)
            {
            	System.out.println(",1");
                fOut.newPrintln(",1");
            }
            else
            {
            	System.out.println(",0");
                fOut.newPrintln(",0");
            }
    	}
        System.out.println(",,,,Match Percentage:,=SUM(F2:F"+(files.length+1)+")/"+files.length);
    	fOut.newPrintln(",,,,Match Percentage:,=SUM(F2:F"+(files.length+1)+")/"+files.length);
    	System.err.println(timeStamp() + "Analysis Finished");
    	fLog.newPrintln(timeStamp() + "Analysis Finished");
    }
    public static void printMatch(File a, File b, double c)
    {
        System.out.println(a.getName()+","+(int)getWidth(a)+"x"+(int)getHeight(a)+","+b.getName()+","+(int)getWidth(b)+"x"+(int)getHeight(b)+","+c);
        fOut.newPrint(a.getName()+","+(int)getWidth(a)+"x"+(int)getHeight(a)+","+b.getName()+","+(int)getWidth(b)+"x"+(int)getHeight(b)+","+c);
        //System.out.println("=HYPERLINK("+a.getPath()+"),  ,=HYPERLINK("+b.getPath()+")");
    }
    public static Dimension getDims(File f)
    {
        try(ImageInputStream in = ImageIO.createImageInputStream(f)){
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch(IOException e) {}
        return new Dimension(0,0);
    }
    public static double getHeight(File f)
    {
        return getDims(f).getHeight();
    }
    public static double getWidth(File f)
    {
        return getDims(f).getWidth();
    }
    public static double getAR(File f)
    {
        return getWidth(f)/getHeight(f);
    }
    public static String timeStamp()
    {
    	Calendar c = Calendar.getInstance();
    	java.text.SimpleDateFormat timeStamp = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	return timeStamp.format(c.getTime()) + " --> ";
    }
    public static class PotMatches
    {
    	public File f1,f2;
    	public double simIndex;
    	public PotMatches(File f1, File f2, double simIndex)
    	{
    		this.f1 = f1;
    		this.f2 = f2;
    		this.simIndex = simIndex;
    	}
    }
}