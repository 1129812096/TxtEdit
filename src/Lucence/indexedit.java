package Lucence;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class indexedit {
	public IndexWriter indexWriter() throws Exception {
		Directory d=FSDirectory.open(new File("E:\\用户文件\\run\\index"));
		Analyzer analyzer=new IKAnalyzer();
		IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		// TODO Auto-generated method stub
		return new IndexWriter(d, conf);
	}
	@Test
	public void testindex() throws Exception {
		// TODO Auto-generated method stub
		IndexWriter indexWriter = indexWriter();
		indexWriter.deleteAll();
		indexWriter.close();
	}
	@Test
	public void testparse() throws IOException, ParseException {
		// TODO Auto-generated method stub
		Directory directory = FSDirectory.open(new File("E:\\用户文件\\run\\index"));
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		Query query=new QueryParser("filename",new IKAnalyzer()).parse("+filename : apache filename : lucence");
		TopDocs topDocs = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int doc = scoreDoc.doc;
			Document document = indexSearcher.doc(doc);
			String filename = document.get("filename");
			//String fileDetial = document.get("fileDetial");
			String filepath = document.get("path");
			String filesize = document.get("size");
			System.out.println(filename+"\n"+" "+"\n"+filepath+"\n"+filesize+"\n");
			
		}
		indexReader.close();
	}
}
