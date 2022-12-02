package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.*;

/**
 * @author Jia Nan
 * @version 1.0
 * @date 2022-03-24
 */
public class IndexSearcher extends AbstractIndexSearcher {
    public IndexSearcher(){}
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        try {
            index.load(new File(indexFile));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList=index.search(queryTerm);
        if(postingList==null){
            return null;
        }

        int len=postingList.size();
        AbstractHit[] hits=new AbstractHit[len];
        //将每一个
        for(int i=0;i<len;i++){
            AbstractPosting posting=postingList.get(i);
            Map<AbstractTerm,AbstractPosting> termToPosting=new HashMap<>();
            termToPosting.put(queryTerm,posting);
            hits[i] = new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termToPosting);
            hits[i].setScore(sorter.score(hits[i]));
        }
        sorter.sort(Arrays.asList(hits));
        return hits;
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractPostingList postinglist1 = this.index.search(queryTerm1);
        AbstractPostingList postinglist2 = this.index.search(queryTerm2);
        Map<AbstractTerm, AbstractPosting> termToPosting = new HashMap<>();
        ArrayList<AbstractHit> hits = new ArrayList<>();
        switch (combine) {
            case OR:
                if (postinglist1 != null) {
                    for (int i = 0; i < postinglist1.size(); i++) {
                        AbstractPosting posting = postinglist1.get(i);
                        termToPosting.put(queryTerm1, posting);
                        hits.add(new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termToPosting));
                        hits.get(i).setScore(sorter.score(hits.get(i)));
                        termToPosting.clear();
                    }
                }
                if (postinglist2 != null) {
                    for (int i = 0; i < postinglist2.size(); i++) {
                        int flag = 0;
                        AbstractPosting posting = postinglist2.get(i);
                        for (int j = 0; j < hits.size(); j++) {
                            AbstractHit item = hits.get(j);
                            if (item.getDocId() == posting.getDocId()) {
                                item.getTermPostingMapping().put(queryTerm2, posting);
                                item.setScore(sorter.score(item));
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            termToPosting.put(queryTerm2, posting);
                            hits.add(new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termToPosting));
                            hits.get(hits.size()-1).setScore(sorter.score(hits.get(hits.size()-1)));
                            termToPosting.clear();
                        }
                    }
                }
                break;
            case AND:
                if (postinglist1 != null && postinglist2 != null) {
                    for (int i = 0; i < postinglist1.size(); i++) {
                        AbstractPosting posting = postinglist1.get(i);
                        for (int j = 0; j < postinglist2.size(); j++) {
                            AbstractPosting posting1 = postinglist2.get(j);
                            if (posting.getDocId() == posting1.getDocId()) {
                                termToPosting.put(queryTerm1, posting);
                                termToPosting.put(queryTerm2, posting1);
                                AbstractHit hit=new Hit(posting.getDocId(), this.index.getDocName(posting.getDocId()), termToPosting);
                                hit.setScore(sorter.score(hit));
                                hits.add(hit);
                                termToPosting.clear();
                            }
                        }
                    }
                }
                break;
        }

        sorter.sort(hits);
        return hits.toArray(new AbstractHit[hits.size()]);
    }
}
