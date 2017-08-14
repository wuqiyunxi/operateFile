import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
/*
 * 名称:文件合并机
 * 功能:将指定碎片文件目录下.part文件合并成对应"完整文件"
 * eg:
 * 文件目录(D:\\splitparts):
 *  1.part(3072kb = 3m);
 * 	2.part(3072kb = 3m);
 * 	3.part(1360kb = 1.32m);
 * 	4.ini(配置信息);
 * 合并后:
 * 	snap.apk
 * 	大小:7.32mb
 * */
public class MergeFiles 
{
	public static void main(String[] args) throws IOException 
	{
		File dir = new File("D:\\splitparts");//碎片文件所在目录
		
		mergeFile(dir);
	}
	
	public static void mergeFile(File dir) throws IOException 
	{	
		//获取配置文件
		File[] files = dir.listFiles(new SuffixFilter(".ini"));
		
		if(files.length!=1)
			throw new RuntimeException(dir+",该目录下不存在配置文件");
		//记录配置文件
		File confi = files[0];
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(confi);	
		prop.load(fis);
		
		String filename = prop.getProperty("filename");		
		int count = Integer.parseInt(prop.getProperty("partcount"));
		//获取该目录下的碎片文件
		File[] partFiles = dir.listFiles(new SuffixFilter(".part"));
		
		if(partFiles.length!=(count-1))
		{
			throw new RuntimeException(" 碎片文件数目不对,请检查!应该"+count+"个");
		}
		//将碎片文件和流对象关联 并存储到集合中。 
		ArrayList<FileInputStream> arrli = new ArrayList<FileInputStream>();
		for(int x=0; x<partFiles.length; x++)
		{	
			arrli.add(new FileInputStream(partFiles[x]));
		}	
		//合并成一个流。 
		Enumeration<FileInputStream> en = Collections.enumeration(arrli);
		SequenceInputStream sis = new SequenceInputStream(en);
		
		FileOutputStream fos = new FileOutputStream(new File(dir,filename));
		
		byte[] buf = new byte[1024];
		
		int len = 0;
		while((len=sis.read(buf))!=-1)
		{
			fos.write(buf,0,len);
		}
		
		fos.close();
		sis.close();	
		
	}
}
//自定义过滤器
class SuffixFilter implements FilenameFilter
{
	private String suffix;
	public SuffixFilter(String suffix)
	{
		super();
		this.suffix = suffix;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accept(File dir, String name)
	{

		return name.endsWith(suffix);
	}
}

