package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.util.Config;

import java.io.File;
import java.util.Scanner;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args) {
        System.out.println("请选择倒排索引创建方式：");
        System.out.println("1. 通过文本文档创建");
        System.out.println("2. 通过序列化索引文件反序列化进行创建");
        System.out.print("请输入选择：");
        Scanner scan = new Scanner(System.in);
        int option = scan.nextInt();
        AbstractIndex index;
        while (true) {
            if(option==1) {
                AbstractIndexBuilder indexBuilder = new IndexBuilder(new DocumentBuilder());
                index = indexBuilder.buildIndex(Config.DOC_DIR);
                if (index.getDictionary().isEmpty()) {
                    System.out.println("索引表为空！");
                }
                System.out.println("文档目录：");
                System.out.println(Config.DOC_DIR);
                System.out.println("倒排索引内容：");
                System.out.println(index.toString());
                break;
            }
            else if(option==2) {
                index = new Index();
                index.load(new File(Config.INDEX_DIR + "index.dat"));
                System.out.println("倒排索引内容：");
                System.out.println(index.toString());
                break;
            }
            else{
                System.out.println("输入错误!请再次输入：");
                option= scan.nextInt();
            }
        }
    }
}
