package hust.cs.javacourse.search.index.impl;
/**
 * @author Jia Nan
 * @Date 2022-3-20
 */
import hust.cs.javacourse.search.index.*;

import java.io.*;


import hust.cs.javacourse.search.index.AbstractIndex;

import java.io.File;
import java.util.*;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex implements FileSerializable{
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        return termToPostingListMapping.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        Map<AbstractTerm, List<Integer> > termPos=new HashMap<AbstractTerm,List<Integer>>();
        for(AbstractTermTuple termTuple:document.getTuples()){//将文件中的termtuple按照term来分组整理
            if(termPos.get(termTuple.term)==null) {
                termPos.put(termTuple.term, new ArrayList<Integer>());
            }
                termPos.get(termTuple.term).add(termTuple.curPos);

        }
        //将这个文件的每个term索引加入到总的map中
        for(AbstractTerm term:termPos.keySet()){
            if(termToPostingListMapping.get(term)==null){
                termToPostingListMapping.put(term,new PostingList());
            }
            termToPostingListMapping.get(term).add(new Posting(document.getDocId(),termPos.get(term).size(),termPos.get(term)));
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try{
            readObject(new ObjectInputStream(new FileInputStream(file)));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try{
            ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file));
            writeObject(outputStream);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return new HashSet<AbstractTerm>(termToPostingListMapping.keySet());
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for(AbstractPostingList list:termToPostingListMapping.values()){
            list.sort();
            for(int i=0;i< list.size();i++){
                list.get(i).sort();
            }
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try{
            out.writeObject(termToPostingListMapping);
            out.writeObject(docIdToDocPathMapping);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try{
            termToPostingListMapping=(Map<AbstractTerm, AbstractPostingList>) in.readObject();
            docIdToDocPathMapping=(Map<Integer, String>) in.readObject();
        }
        catch(IOException|ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
