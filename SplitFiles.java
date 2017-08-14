import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/*
 * 名称:文件切割机
 * 功能:将指定目录下的指定文件切割成指定大小的.part文件并存储到指定目录中
 * eg:
 * 文件对象:
 * 	snap.apk
 * 	大小:7.32mb
 * 切割后:
 * 	1.part(3072kb = 3m);
 * 	2.part(3072kb = 3m);
 * 	3.part(1360kb = 1.32m);
 * 	4.ini(切之后的配置信息,请勿删除!如果删除,将无法合并QAQ);
 * */
public class SplitFiles
{
	private static final int SIZE = 3145728;//按照3M每个切;ps:1024*1024*3

	public static void main(String[] args) throws IOException
	{
		File file = new File("D:\\splitfiles\\snap.apk");
		
		splitpart(file);
	}

	public static void splitpart(File file) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		byte[] buff = new byte[SIZE];
		FileOutputStream fos = null;
		int len = 0;
		int count = 1;
		Properties prop  = new Properties();
		
		File dir = new File("D://splitparts");
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		while((len = fis.read(buff))!=-1)
		{
			fos = new FileOutputStream(new File(dir,count++ +".part"));
			fos.write(buff, 0, len);
		}
		//将被切割文件的配置信息保存到集合中。
		prop.setProperty("partcount", count+"");
		prop.setProperty("filename", file.getName());
		
		fos = new FileOutputStream(new File(dir,count+".ini"));
		
		//将集合中的数据存储到文件中。 
		prop.store(fos, "File Split Info");
				
		fis.close();
		fos.close();
		
	}

}
