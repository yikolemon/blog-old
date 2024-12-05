package com.yikolemon.service.search;

import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.PageInfo;
import com.yikolemon.mapper.BlogMapper;
import com.yikolemon.pojo.Blog;
import com.yikolemon.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author duanfuqiang
 * @date 2024/12/4
 **/
@Component
@Slf4j
public class BlogFullTextCompoment {

    @Resource
    private BlogMapper blogMapper;

    private static Path getPath(){
        String absolutePath = FileUtil.getAbsolutePath("./full-text-index");
        return FileSystems.getDefault().getPath(absolutePath);
    }


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
        try (Directory directory = FSDirectory.open(getPath());
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
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(new IKAnalyzer());
        // 创建目录对象
        // 创建索引写出工具
        try (Directory directory = FSDirectory.open(getPath());
             IndexWriter writer = new IndexWriter(directory, conf)) {
            // 获取更新的数据，这里只是演示
            Blog blog = blogMapper.getBlog(blogId);
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
            writer.updateDocument(new Term("id", String.valueOf(blog.getId())), document);
            // 强制合并索引，确保更新生效
            writer.forceMerge(1);
            // 提交
            writer.commit();
        } catch (Exception e) {
            log.error("更新索引失败", e);
        }
    }

    public void delete(Long blogId){
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(new IKAnalyzer());
        // 创建目录对象
        // 创建索引写出工具
        try (Directory directory = FSDirectory.open(getPath());
             IndexWriter writer = new IndexWriter(directory, conf)) {
            // 根据词条进行删除
            writer.deleteDocuments(new Term("id", String.valueOf(blogId)));
            // 提交
            writer.commit();
        } catch (Exception e) {
            log.error("删除索引失败", e);
        }
    }

    public List<Blog> searchAll(String text){
        String[] str = {"title", "description", "content"};
        try (Directory directory = FSDirectory.open(getPath());
             // 索引读取工具
             IndexReader reader = DirectoryReader.open(directory)){
            // 索引搜索工具
            IndexSearcher searcher = new IndexSearcher(reader);
            // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
            MultiFieldQueryParser parser = new MultiFieldQueryParser(str, new IKAnalyzer());
            // 创建查询对象
            Query query = parser.parse(text);
            // 获取前十条记录
            TopDocs topDocs = searcher.search(query, 100);
            // 获取总条数
            log.info("本次搜索共找到" + topDocs.totalHits + "条数据");
            // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<Blog> list = new ArrayList<>();
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 取出文档编号
                int docId = scoreDoc.doc;
                // 根据编号去找文档
                Document doc = reader.document(docId);
                Blog blog = blogMapper.getBlog(Long.parseLong(doc.get("id")));
                list.add(blog);
            }
            return list;
        } catch (Exception e) {
            log.error("删除索引失败", e);
        }
        return Collections.emptyList();
    }

    public PageInfo<Blog> searchPage(int pageNum, String text){
        String[] fields = {"title", "description", "content"};
        try (Directory directory = FSDirectory.open(getPath());
             // 索引读取工具
             IndexReader reader = DirectoryReader.open(directory)){
            // 索引搜索工具
            IndexSearcher searcher = new IndexSearcher(reader);
            // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new IKAnalyzer());
            // 创建查询对象
            Query query = parser.parse(text);
            // 获取前十条记录
            TopDocs topDocs = searchByPage(pageNum, searcher, query);
            assert topDocs != null;
            // 获取总条数
            log.info("本次搜索共找到" + topDocs.totalHits + "条数据");
            // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<Blog> blogList = new ArrayList<>();
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 取出文档编号
                int docId = scoreDoc.doc;
                // 根据编号去找文档
                Document doc = reader.document(docId);
                Blog blog = blogMapper.getBlog(Long.parseLong(doc.get("id")));
                blogList.add(blog);
            }
            PageInfo<Blog> pageInfo = new PageInfo<>();
            pageInfo.setTotal(topDocs.totalHits);
            pageInfo.setPageNum(pageNum);
            int pageSize = PageUtils.getPageSize();
            pageInfo.setPages(pageInfo.getTotal() == 0L ? 1 : (int)((pageInfo.getTotal() - 1) / pageSize + 1));
            pageInfo.setIsFirstPage(pageNum == 1L);
            pageInfo.setIsLastPage(pageNum == pageInfo.getPages());
            pageInfo.setList(blogList);
            return pageInfo;
        } catch (Exception e) {
            log.error("删除索引失败", e);
        }
        return new PageInfo<>();
    }

    private TopDocs searchByPage(int page, IndexSearcher searcher, Query query) throws IOException {
        int pageSize = PageUtils.getPageSize();
        TopDocs result;
        if (query == null) {
            log.info(" Query is null return null ");
            return null;
        }
        ScoreDoc before = null;
        if (page != 1) {
            TopDocs docsBefore = searcher.search(query, page * pageSize);
            ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
            if (scoreDocs.length > 0) {
                before = scoreDocs[scoreDocs.length - 1];
            }
        }
        result = searcher.searchAfter(before, query, pageSize);
        return result;
    }

}
