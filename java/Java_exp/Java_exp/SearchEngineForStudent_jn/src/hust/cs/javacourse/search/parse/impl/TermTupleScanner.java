package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Jia Nan
 * @version 1.0
 * @date 2022-03-20
 */
public class TermTupleScanner extends AbstractTermTupleScanner {
    private int pos=0;//position of word
    private List<AbstractTermTuple> buf=new ArrayList<>();
    private StringSplitter spliter;
    public TermTupleScanner(){
        pos=0;
        buf=new LinkedList<AbstractTermTuple>();
        spliter=new StringSplitter();
        spliter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
    };
    public TermTupleScanner(BufferedReader input){
        super(input);
        spliter=new StringSplitter();
        spliter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
        try {
            String str = input.readLine();
            while (str != null) {
                List<String> parts = spliter.splitByRegex(str);
                int i;
                for (i = 0; i < parts.size(); i++) {
                    if (!parts.get(i).equals("")){
                        this.buf.add(new TermTuple(new Term(parts.get(i).toLowerCase()), this.pos++));
                    }
                }
                str = input.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获得下一个三元组
     *
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        if (this.buf.size()!=0){//获取一个就从buf中删除
            return buf.remove(0);
        } else {
            return null;
        }
    }
}
