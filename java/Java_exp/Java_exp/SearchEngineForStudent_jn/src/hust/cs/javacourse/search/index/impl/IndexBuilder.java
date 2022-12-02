package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Jia Nan
 * @version 1.0
 * @date 2022-03-22
 */
public class IndexBuilder extends AbstractIndexBuilder {
    public IndexBuilder(AbstractDocumentBuilder documentBuilder){
        super(documentBuilder);
    }
    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootDirectory ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index=new Index();
        //建立Document对象，再将该对象加入到index倒排索引表中
        for(String docPath: FileUtil.list(rootDirectory)){
            AbstractDocument doc=docBuilder.build(this.docId++,docPath,new File(docPath));
            index.addDocument(doc);
        }
        //读入文件对象
        index.save(new File(Config.INDEX_DIR+"index.dat"));
        return index;
    }
}
