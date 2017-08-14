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
 * ����:�ļ��ϲ���
 * ����:��ָ����Ƭ�ļ�Ŀ¼��.part�ļ��ϲ��ɶ�Ӧ"�����ļ�"
 * eg:
 * �ļ�Ŀ¼(D:\\splitparts):
 *  1.part(3072kb = 3m);
 * 	2.part(3072kb = 3m);
 * 	3.part(1360kb = 1.32m);
 * 	4.ini(������Ϣ);
 * �ϲ���:
 * 	snap.apk
 * 	��С:7.32mb
 * */
public class MergeFiles 
{
	public static void main(String[] args) throws IOException 
	{
		File dir = new File("D:\\splitparts");//��Ƭ�ļ�����Ŀ¼
		
		mergeFile(dir);
	}
	
	public static void mergeFile(File dir) throws IOException 
	{	
		//��ȡ�����ļ�
		File[] files = dir.listFiles(new SuffixFilter(".ini"));
		
		if(files.length!=1)
			throw new RuntimeException(dir+",��Ŀ¼�²����������ļ�");
		//��¼�����ļ�
		File confi = files[0];
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(confi);	
		prop.load(fis);
		
		String filename = prop.getProperty("filename");		
		int count = Integer.parseInt(prop.getProperty("partcount"));
		//��ȡ��Ŀ¼�µ���Ƭ�ļ�
		File[] partFiles = dir.listFiles(new SuffixFilter(".part"));
		
		if(partFiles.length!=(count-1))
		{
			throw new RuntimeException(" ��Ƭ�ļ���Ŀ����,����!Ӧ��"+count+"��");
		}
		//����Ƭ�ļ������������ ���洢�������С� 
		ArrayList<FileInputStream> arrli = new ArrayList<FileInputStream>();
		for(int x=0; x<partFiles.length; x++)
		{	
			arrli.add(new FileInputStream(partFiles[x]));
		}	
		//�ϲ���һ������ 
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
//�Զ��������
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

