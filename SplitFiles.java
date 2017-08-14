import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/*
 * ����:�ļ��и��
 * ����:��ָ��Ŀ¼�µ�ָ���ļ��и��ָ����С��.part�ļ����洢��ָ��Ŀ¼��
 * eg:
 * �ļ�����:
 * 	snap.apk
 * 	��С:7.32mb
 * �и��:
 * 	1.part(3072kb = 3m);
 * 	2.part(3072kb = 3m);
 * 	3.part(1360kb = 1.32m);
 * 	4.ini(��֮���������Ϣ,����ɾ��!���ɾ��,���޷��ϲ�QAQ);
 * */
public class SplitFiles
{
	private static final int SIZE = 3145728;//����3Mÿ����;ps:1024*1024*3

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
		//�����и��ļ���������Ϣ���浽�����С�
		prop.setProperty("partcount", count+"");
		prop.setProperty("filename", file.getName());
		
		fos = new FileOutputStream(new File(dir,count+".ini"));
		
		//�������е����ݴ洢���ļ��С� 
		prop.store(fos, "File Split Info");
				
		fis.close();
		fos.close();
		
	}

}
