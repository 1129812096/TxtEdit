package Lucence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class FirstLucenceTest {

	@Test
	public void LucenceTest() throws IOException {
		/***
		 * 
		 * 问题的缘由就是Lucene版本不兼容，下面的FSDirectory.open()在Lucene5.0.0版本下，open的参数是Path而不是File。
		 * String path = " ... "; directory = FSDirectory.open(new File(path));
		 * 所以在Lucene5.0.0版本下，正确的打开方式如下,这样得到的就是File，然后能正常赋给directory了。 String path = "
		 * ... "; directory = FSDirectory.open(Paths.get(path));
		 */
		Directory d = FSDirectory.open(new File("E:\\用户文件\\run\\index"));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		IndexWriter indexWriter = new IndexWriter(d, conf);

		File f = new File("E:\\用户文件\\run\\searchsource");
		File[] listFiles = f.listFiles();
		for (File file : listFiles) {
			Document document = new Document();
			String name = file.getName();
			Field FieldFileName = new TextField("filename", name, Store.YES);
			long sizeOf = FileUtils.sizeOf(file);
			Field FileSizeFiled = new LongField("size", sizeOf, Store.YES);
			String path = file.getPath();
			Field FilePathField = new StoredField("path", path);
			String fileDetial = FileUtils.readFileToString(file, "utf-8");
			Field FileDetialField = new TextField("fileDetial", fileDetial, Store.YES);
			document.add(FieldFileName);
			document.add(FileSizeFiled);
			document.add(FilePathField);
			document.add(FileDetialField);
			indexWriter.addDocument(document);

		}
		indexWriter.close();
	}

	@Test
	public void indexSearch() throws Exception {
		Directory directory = FSDirectory.open(new File("E:\\用户文件\\run\\index"));
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Query query = new TermQuery(new Term("filename", "apache"));
		TopDocs topDocs = indexSearcher.search(query, 2);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int doc = scoreDoc.doc;
			Document document = indexSearcher.doc(doc);
			String filename = document.get("filename");
			String fileDetial = document.get("fileDetial");
			String filepath = document.get("path");
			String filesize = document.get("size");
			System.out.println(filename+"\n"+fileDetial+"\n"+filepath+"\n"+filesize+"\n");
			
		}
		indexReader.close();
	}
	
	
	@Test
	public void testTokenStream() throws Exception {
		//创建一个标准分析器对象
		Analyzer analyzer = new IKAnalyzer();
		//获得tokenStream对象
		//第一个参数：域名，可以随便给一个
		//第二个参数：要分析的文本内容
		TokenStream tokenStream = analyzer.tokenStream("test", "我是中国人");
		//添加一个引用，可以获得每个关键词
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		//添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		//将指针调整到列表的头部
		tokenStream.reset();
		//遍历关键词列表，通过incrementToken方法判断列表是否结束
		while(tokenStream.incrementToken()) {
			//关键词的起始位置
			System.out.println("start->" + offsetAttribute.startOffset());
			//取关键词
			System.out.println(charTermAttribute);
			//结束位置
			System.out.println("end->" + offsetAttribute.endOffset());
		}
		tokenStream.close();
	}
	
	
}
