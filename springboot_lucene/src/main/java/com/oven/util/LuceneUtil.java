package com.oven.util;

import com.oven.vo.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneUtil {

    private static final String PATH = "/Users/oven/Desktop/index/";
    private static Directory dir;

    static {
        try {
            dir = FSDirectory.open(Paths.get(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建索引
     */
    public static void index(Article article) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(article.getId()), Field.Store.YES));
        doc.add(new StringField("author", article.getAuthor(), Field.Store.YES));
        doc.add(new TextField("content", article.getContent(), Field.Store.YES));
        writer.addDocument(doc);
        writer.commit();
        writer.close();
    }

    /**
     * 删除索引
     */
    public static void delete(Integer id) throws Exception {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", String.valueOf(id)));
        writer.commit();
        writer.close();
    }

    /**
     * 更新
     */
    public static void update(Article article) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("author", article.getAuthor(), Field.Store.YES));
        doc.add(new TextField("content", article.getContent(), Field.Store.YES));
        doc.add(new StringField("id", String.valueOf(article.getId()), Field.Store.YES));
        writer.updateDocument(new Term("id", String.valueOf(article.getId())), doc);
        writer.close();
    }

    /**
     * 查询
     */
    public static List<Article> search(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        List<Article> list = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher is = new IndexSearcher(reader);
            Analyzer analyzer = new IKAnalyzer(); // IK分词器
            String[] fields = {"content", "id", "author"}; // 要查询的字段
            BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
            Query query = MultiFieldQueryParser.parse(keyword, fields, clauses, analyzer);
            TopDocs hits = is.search(query, 10);

            // 高亮显示
            QueryScorer scorer = new QueryScorer(query);
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'><strong>", "</strong></font>");
            Highlighter highlighter = new Highlighter(formatter, scorer);
            highlighter.setTextFragmenter(fragmenter);

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);
                String id = doc.get("id");
                String author = doc.get("author");
                String content = doc.get("content");
                if (content != null) {
                    TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                    String result = highlighter.getBestFragment(tokenStream, content);
                    Article article = new Article();
                    article.setId(Integer.parseInt(id));
                    article.setAuthor(author);
                    article.setContent(result);
                    list.add(article);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取IndexWriter实例
     */
    private static IndexWriter getWriter() throws Exception {
        Analyzer analyzer = new IKAnalyzer(); // IK分词器
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir, iwc);
    }

}
