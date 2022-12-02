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
public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    public LengthTermTupleFilter(AbstractTermTupleStream termTupleStream){
        super(termTupleStream);
    }
    /**
     * 获得下一个三元组,过滤长度不符合要求的
     *
     * @return 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple lenFilter=input.next();
        if(lenFilter==null){
            return null;
        }
        while(lenFilter.term.getContent().length()< Config.TERM_FILTER_MINLENGTH||lenFilter.term.getContent().length()>Config.TERM_FILTER_MAXLENGTH){
            lenFilter=input.next();
            if(lenFilter==null){
                return null;
            }
        }
        return lenFilter;
    }
}
