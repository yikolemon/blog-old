package com.yikolemon.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@Component
@Service
public class SensitiveUtil {

    private static final Logger logger= LoggerFactory.getLogger(SensitiveUtil.class);

    private static final String REPLACEMENT="***";

    private TrieNode root=new TrieNode();

    //初始化前缀树
    @PostConstruct
    public void init(){

        try (InputStream resource = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             BufferedReader reader =new BufferedReader(new InputStreamReader(resource));
        ){
            String keyword;
            while ((keyword=reader.readLine())!=null){
                this.addKeyword(keyword);
            }
        } catch (IOException e) {

            logger.error("加载敏感词文件失败",e);
        }
    }

    //把敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode curNode=root;
        for (int i = 0; i < keyword.length(); i++) {
            char c=keyword.charAt(i);
            TrieNode subNode=curNode.getSubNode(c);
            if (subNode==null){
                //如果子节点为空，那么创建子节点，并添加
                subNode=new TrieNode();
                curNode.addSubNode(c,subNode);
            }/*else{
                //子节点不为空，说明这个位置已经创建了
            }*/
            curNode=subNode;
            if(i==keyword.length()-1){
                curNode.setKeywordEnd(true);
            }
        }
    }

    /**
     *
     * @param text text为检测文本
     * @return true包含，false不包含
     */
    public boolean containsSensitiveWord(String text){
        if (StringUtils.isEmpty(text)){
            return false;
        }
        //指针1
        TrieNode curNode=root;
        //指针2
        int begin=0;
        //指针3
        int position=0;
        while (position<text.length()){
            char c=text.charAt(position);
            //跳过符号
            if (isSymbol(c)){
                //如果指针位于根节点，将此符号计入结果，让指针向下走一步
                if (curNode==root){
                    begin++;
                }
                //无论符号在开头还是中间，指针3都向下走
                position++;
                continue;
            }
            //不是跳过符号
            //检查下级节点
            curNode=curNode.getSubNode(c);
            if (curNode==null){
                //不为敏感词
                position=++begin;
                //重新指向根节点
                curNode=root;
            }else if (curNode.isKeywordEnd()){
                //发现敏感词
                return true;
            }else {
                //检查下一个字符
                position++;
            }
        }
        return false;
    }


    /**
     *
     * @param text 待过滤文本1
     * @return 过滤后的文本
     */
    public String filter(String text){
        if (StringUtils.isEmpty(text)){
            return null;
        }
        //指针1
        TrieNode curNode=root;
        //指针2
        int begin=0;
        //指针3
        int position=0;
        StringBuilder sb=new StringBuilder();
        while (position<text.length()){
            char c=text.charAt(position);
            //跳过符号
            if (isSymbol(c)){
                //如果指针位于根节点，将此符号计入结果，让指针向下走一步
                if (curNode==root){
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头还是中间，指针3都向下走
                position++;
                continue;
            }
            //不是跳过符号
            //检查下级节点
            curNode=curNode.getSubNode(c);
            if (curNode==null){
                //不为敏感词
                sb.append(text.charAt(begin));
                position=++begin;
                //重新指向根节点
                curNode=root;
            }else if (curNode.isKeywordEnd()){
                //发现敏感词，替换敏感词
                sb.append(REPLACEMENT);
                begin=++position;
                curNode=root;
            }else {
                //检查下一个字符
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c){
        //判读是否为普通字符，(c<0x2E80||c>0x9FFF)为东亚文字范围之外
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }

    private class TrieNode{

        //关键词结束标识
        private boolean isKeywordEnd=false;

        //子节点(key为下级字符，value为下级节点)
        private Map<Character,TrieNode> subNodes=new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点方法
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }


    }

}
