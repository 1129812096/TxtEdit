import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
/**
 * 
 * 对文本内容进行替换
 * 没有高级功能
 * @author Administrator
 *
 */
public class CodeEditReplace {

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			
			File replacefile = new File("C:\\Users\\Administrator\\Desktop\\replacefile.txt");
			// 可能多个文件，怎么搞？
			// File file = new File("C:\\Users\\Administrator\\Desktop\\customer.sql");
			Collection<File> listFiles = FileUtils.listFiles(new File("F:\\upload"),
					FileFilterUtils.suffixFileFilter("sql"), DirectoryFileFilter.INSTANCE);
			//读取要屏蔽的字符串到数组 start
			LineIterator iterator = FileUtils.lineIterator(replacefile);
			
			while (iterator.hasNext()) {
				String next = iterator.next();
				String replaceword = next.replaceAll("[\\.\\**]", "\\\\$0");
				System.out.println(replaceword);
				list.add(replaceword);
				
			}
			if (list.size() == 0) {
				System.out.println("file was empty");
				return;
			}
			//读取要屏蔽的字符串到数组 end
			
			for (File file : listFiles) {
				LineIterator in = FileUtils.lineIterator(file, "utf-8");
				CharArrayWriter charArrayWriter = new CharArrayWriter();
				for (String str : list) {
					
					while (in.hasNext()) {
						String next = in.next();
						String replaceAll = next.replaceAll("(.*')(" + str + ")",
								"--sccba09218781\r\n" + "$1" + "@-" + "$2");
						charArrayWriter.write(replaceAll);
						charArrayWriter.append(System.getProperty("line.separator"));
						System.out.println(replaceAll);
					}
					FileWriter out = new FileWriter(file);
					charArrayWriter.writeTo(out);
					out.close();
				}
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
