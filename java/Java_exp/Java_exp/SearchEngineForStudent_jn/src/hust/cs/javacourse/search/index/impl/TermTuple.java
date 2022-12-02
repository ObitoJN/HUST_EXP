package hust.cs.javacourse.search.index.impl;
/**
 * @author Jia Nan
 * @Date 2022-3-20
 */

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.FileSerializable;

public class TermTuple extends AbstractTermTuple {
    /**
     *无参构造
     */
    public TermTuple(){}
    /**
     * 用term和curPos构造
     * @param term :单词
     * @param curPos ：出现位置
     */
    public TermTuple(AbstractTerm term,int curPos){
        this.term=term;
        this.curPos=curPos;
    }
    /**
     * 直接用string构造
     * @param content :string单词
     * @param curPos :出现位置
     */
    public TermTuple(String content,int curPos){
        this.term=new Term(content);
        this.curPos=curPos;
    }
    /**
     * 判断二个三元组内容是否相同
     *
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TermTuple){
            return this.term.equals(((TermTuple) obj).term)&&this.freq== ((TermTuple) obj).freq&&this.curPos==((TermTuple) obj).curPos;
        }
        return false;
    }

    /**
     * 获得三元组的字符串表示
     *
     * @return ： 三元组的字符串表示
     */
    @Override
    public String toString() {
        return "{term="+term.toString()+",freq:"+freq+",curPos:"+curPos+"}\n";
    }
}
