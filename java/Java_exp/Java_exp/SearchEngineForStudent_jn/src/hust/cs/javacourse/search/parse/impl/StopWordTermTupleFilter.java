package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jia Nan
 * @version 1.0
 * @date 2022-03-20
 */
public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    private List<String> stopwordList;
    public StopWordTermTupleFilter(AbstractTermTupleStream input){
        super(input);
        stopwordList=new ArrayList<String>(Arrays.asList(StopWords.STOP_WORDS));
    }
    /**
     * 获得下一个三元组
     *
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple stopwordFilter=input.next();
        if(stopwordFilter==null){
            return null;
        }
        while(stopwordList.contains(stopwordFilter.term.getContent())){
            stopwordFilter=input.next();
            if(stopwordFilter==null){
                return null;
            }
        }
        return stopwordFilter;
    }
}
