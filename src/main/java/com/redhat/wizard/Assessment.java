package com.redhat.wizard;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.KnowledgeBase;

public class Assessment {
  private String title;
  private Map<String, Feedback> feedback;
  public Map<Integer, Page> pages;
  public KnowledgeBase feedbackKBase;
  
  public void addControl(Integer pageNum, Control c){
    Page page=getPages().get(pageNum);
    
    if (c.getType().equalsIgnoreCase("Feedback")){
      page=new FeedbackPage(pageNum, c.getForm(), this);
      System.out.println("Adding Feedback Page "+page.getNumber()+" ["+page.getName()+"]");
    }else{
      if (page==null) page=new Page(pageNum, c.getForm());
      page.getControls(). addFirst(c);
      System.out.println("Adding Page "+page.getNumber()+" ["+page.getName()+"]");
    }
    getPages().put(page.getNumber(), page);
  }
  
  public Map<Integer, Page> getPages(){
    if (pages==null) pages=new LinkedHashMap<Integer,Page>();
    return pages;
  }
  
  public String getTitle(){
    return title;
  }
  public void setTitle(String title){
    this.title=title;
  }
  public Map<String, Feedback> getFeedback(){
    if (feedback==null) feedback=new HashMap<String, Feedback>();
    return feedback;
  }
  public Page getPageByName(String pageName) {
    for(Entry<Integer, Page> e:getPages().entrySet()){
      if (pageName.equalsIgnoreCase(e.getValue().getName())){
        return e.getValue();
      }
    }
    return null;
  }
  
}
