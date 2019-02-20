import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
public class FileOutput
{
	private PrintWriter out;
	public void setFile(File file, String customLoc, String name, String ext)
    {
    	if(customLoc.length() != 0)
    	{
    		try 
    		{
                out = new PrintWriter(new FileWriter(customLoc+"\\"+name+ext));
            } catch (IOException e) {}
    	}
    	else
    	{
    		try {
                out = new PrintWriter(new FileWriter(file.getAbsolutePath()+"\\"+name+ext));
            } catch (IOException e) {}
    	}
    }
	public void newPrint(String print)
    {
        out.print(print);
    }
    public void newPrintln(String print)
    {
    	out.println(print);
    }
	public void endPrint()
	{
		out.close();
	}
}
