package com.yikolemon.service.search;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import com.yikolemon.mapper.BlogMapper;
import com.yikolemon.pojo.Blog;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * @author duanfuqiang
 * @date 2024/12/4
 **/
@Component
@Slf4j
public class FullTextIndexInitComponent {

    @Resource
    private BlogMapper blogMapper;

    @PostConstruct
    public void fulltextInit(){
        List<Blog> blogs = blogMapper.listAll();
        ArrayList<Document> docList = new ArrayList<>();
        for (Blog blog : blogs) {
            // 创建文档对象
            Document document = new Document();
            // StringField: 这个 Field 用来构建一个字符串Field，不分析，会索引，Field.Store控制存储
            // LongPoint、IntPoint 等类型存储数值类型的数据。会分析，会索引，不存储，如果想存储数据还需要使用 StoredField
            // StoredField: 这个 Field 用来构建不同类型，不分析，不索引，会存储
            // TextField: 如果是一个Reader, 会分析，会索引，，Field.Store控制存储
            document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
            // Field.Store.YES, 将原始字段值存储在索引中。这对于短文本很有用，比如文档的标题，它应该与结果一起显示。
            // 值以其原始形式存储，即在存储之前没有使用任何分析器。
            document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
            // Field.Store.NO，可以索引，分词，不将字段值存储在索引中。
            // 个人理解：说白了就是为了省空间，如果回表查询，其实无所谓，如果不回表查询，需要展示就要保存，设为YES，无需展示，设为NO即可。
            document.add(new TextField("description", blog.getDescription(), Field.Store.YES));
            document.add(new TextField("content", blog.getContent(), Field.Store.YES));
            docList.add(document);
        }
        // 引入IK分词器，如果需要解决上面版本冲突报错的问，使用`new MyIKAnalyzer()`即可
        Analyzer analyzer = new IKAnalyzer();
        // 索引写出工具的配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        // 设置打开方式：OpenMode.APPEND 会在索引库的基础上追加新索引。OpenMode.CREATE会先清空原来数据，再提交新的索引
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // 索引目录类,指定索引在硬盘中的位置，我的设置为D盘的indexDir文件夹
        // 创建索引的写出工具类。参数：索引的目录和配置信息
        String absolutePath = FileUtil.getAbsolutePath("./full-text-index");

        try (Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(absolutePath));
             IndexWriter indexWriter = new IndexWriter(directory, conf)) {
            // 把文档集合交给IndexWriter
            indexWriter.addDocuments(docList);
            // 提交
            indexWriter.commit();
        } catch (Exception e) {
            log.error("创建索引失败", e);
        }
    }

    public void update(Long blogId){

    }

}
