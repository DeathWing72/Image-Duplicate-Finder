import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
public class FileOutput
{
	private PrintWriter out;
	public void setFile(File file)
	{
		try {
			out = new PrintWriter(new FileWriter(file.getAbsolutePath()+".csv"));
		} catch (IOException e) {}
	}
	public void newPrint(String print)
	{
		out.println(print);
	}
	public void endPrint()
	{
		out.close();
	}
}
