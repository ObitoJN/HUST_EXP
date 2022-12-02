package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import javax.swing.*;
import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args) throws IOException {
        IndexSearcher searcher=new IndexSearcher();
        searcher.open(Config.INDEX_DIR+"index.dat");
        SimpleSorter sorter=new SimpleSorter();
        Scanner scan =new Scanner(System.in);
        while(true) {
            System.out.println("请选择查询方式：");
            System.out.println("1.一个单词查询");
            System.out.println("2.两个单词查询");
            int option=scan.nextInt();
            BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
            String words;
            //一个单词查询
            if(option==1){
                System.out.println("请输入一个单词：");
                words=reader.readLine();
                AbstractHit[] hits=searcher.search(new Term(words),sorter);
                if(hits==null||hits.length<1){
                    System.out.println("无结果！");
                }
                else{
                    for(AbstractHit hit:hits){
                        System.out.println(hit.toString());
                    }
                }
            }
            //两个单词查询
            else if(option==2){
                System.out.println("请输入两个单词，以空格隔开：");
                words=reader.readLine();
                while(true) {
                    System.out.println("请选择两个单词结合方式：");
                    System.out.println("1.AND模式");
                    System.out.println("2.OR模式");
                    option = scan.nextInt();
                    String[] twoWord = words.split(" ");
                    if(twoWord.length!=2){
                        System.out.println("输入单词个数错误！");
                    }
                    if (option == 1) {
                        AbstractHit[] hits=searcher.search(new Term(twoWord[0]),new Term(twoWord[1]),sorter, AbstractIndexSearcher.LogicalCombination.AND);
                        if(hits==null||hits.length<1){
                            System.out.println("无结果！");
                        }
                        else{
                            for(AbstractHit hit:hits){
                                System.out.println(hit.toString());
                            }
                        }
                        break;
                    } else if (option == 2) {
                        AbstractHit[] hits=searcher.search(new Term(twoWord[0]),new Term(twoWord[1]),sorter, AbstractIndexSearcher.LogicalCombination.OR);
                        if(hits==null||hits.length<1){
                            System.out.println("无结果！");
                        }
                        else{
                            for(AbstractHit hit:hits){
                                System.out.println(hit.toString());
                            }
                        }
                        break;
                    } else {
                        System.out.println("输入错误！");
                    }
                }

            }
            else{
                System.out.println("输入错误！请再次输入");
                continue;
            }
            System.out.println("是否退出搜索？（Y/N）");
           char isCon=scan.next().charAt(0);
            if(isCon=='Y'){
                break;
            }
        }
    }
}
