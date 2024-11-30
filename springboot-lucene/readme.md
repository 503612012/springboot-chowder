# springboot炖lucene
### 1. 先睹为快
### 2. 实现原理
#### 2.1 新建项目
#### 2.2 创建maven目录结构，以及pom.xml文件
#### 2.3 pom.xml文件中加入依赖
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 2.4 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.lucene</groupId>
        <artifactId>lucene-core</artifactId>
        <version>7.1.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.lucene</groupId>
        <artifactId>lucene-queryparser</artifactId>
        <version>7.1.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.lucene</groupId>
        <artifactId>lucene-highlighter</artifactId>
        <version>7.1.0</version>
    </dependency>

    <dependency>
        <groupId>cn.bestwu</groupId>
        <artifactId>ik-analyzers</artifactId>
        <version>5.1.0</version>
    </dependency>
</dependencies>
```
#### 2.5 pom.xml文件中加入maven-springboot打包插件
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
#### 2.6 开发启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
#### 2.7 开发文章实体类
```java
import lombok.Data;

@Data
public class Article {

    private Integer id;
    private String author;
    private String content;

}
```
#### 2.8 开发lucene工具类
```java
import com.oven.entity.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
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
```
#### 2.9 开发测试控制层
```java
import com.oven.entity.Article;
import com.oven.util.LuceneUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @PostMapping("/add")
    public String add(Article article) {
        try {
            LuceneUtil.index(article);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/delete")
    public String delete(Integer id) {
        try {
            LuceneUtil.delete(id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/update")
    public String update(Article article) {
        try {
            LuceneUtil.update(article);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/search")
    public List<Article> search(String keyword) {
        try {
            return LuceneUtil.search(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
```
#### 2.10 编译打包运行
### 3. 应用场景