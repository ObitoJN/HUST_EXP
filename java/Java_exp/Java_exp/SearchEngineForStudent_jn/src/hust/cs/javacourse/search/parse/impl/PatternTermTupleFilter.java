package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * @author Jia Nan
 * @version 1.0
 * @date 2022-03-20
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    public PatternTermTupleFilter(AbstractTermTupleStream input){
        super(input);

    }
    /**
     * 获得下一个三元组
     *
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple patternFilter= input.next();
        if(patternFilter==null){
            return null;
        }
        while(!patternFilter.term.getContent().matches(Config.TERM_FILTER_PATTERN)){
            patternFilter=input.next();
            if(patternFilter==null){
                return null;
            }
        }
        return patternFilter;
    }
}
