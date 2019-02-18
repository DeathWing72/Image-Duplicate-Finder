import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
public class ImageDuplicateFinder
{
	static FileOutput fOut = new FileOutput();
    public static void main(String[] args)
    {
        FileFrame frame = new FileFrame();
        fOut.setFile(frame.file);
        File[] files = getFile(frame.file);
        File[][] sortFiles = imageSort(files);
        findMatches(sortFiles);
        fOut.endPrint();
    }
    public static File[] getFile(File dir)
    {
        System.err.println(dir.getAbsoluteFile()); //Print folder path to err dialog
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
    public static void findMatches(File[][] f)
    {
        System.out.println("File Name a,File Dimensions a,File Name b,File Dimensions b,Similarity Index");
        fOut.newPrint("File Name a,File Dimensions a,File Name b,File Dimensions b,Similarity Index");
        ArrayList<ArrayList<File>> matches = new ArrayList<ArrayList<File>>();
        for(File[] files: f)
        {
            //System.out.println("New Aspect Ratio - "+getAR(files[0]));
            if(f.length > 1)
            {
                //System.out.println("Beginning Comparisons in Aspect Ratio: "+getAR(files[0]));
                for(int x=0;x<files.length-1;x++)
                {
                    //System.out.println("x: "+files[x].getName());
                    for(int y=x+1;y<files.length;y++)
                    {
                        //System.out.println("x: "+files[x].getName()+"  y: "+files[y].getName());
                        double simIndex = 0;
                        try
                        {
                            SsimCalculator ssim = new SsimCalculator(files[x]);
                            simIndex = ssim.compareTo(files[y]);
                            ssim = null;
                        } catch(SsimException | IOException e) {}
                        if(true/*simIndex > .95*/)
                        {
                            // matches.add(new ArrayList<File>());
                            // matches.get(matches.size()-1).add(files[x]);
                            // matches.get(matches.size()-1).add(files[y]);
                            CompareFrame frame = new CompareFrame(files[x],files[y],simIndex);
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
                            if(frame.isMatch)
                            {
                                printMatch(files[x],files[y],simIndex);
                            }
                        }
                    }
                }
            }
        }
        File[][] matchImg = new File[matches.size()][];
        for(int x = 0; x < matchImg.length; x++)
        {
            matchImg[x] = matches.get(x).toArray(new File[matches.get(x).size()]);
        }
        System.err.println("Analysis Finished");
        // return matchImg;
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
}