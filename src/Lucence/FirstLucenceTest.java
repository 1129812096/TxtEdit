package Lucence;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
}
