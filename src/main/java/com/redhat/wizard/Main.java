package com.redhat.wizard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class Main {
  public static final String configsBase="src/main/resources/";
  public String getTitle(){return assessment.getTitle();};
  private Assessment assessment;
  
  
  public List<String> configs=new ArrayList<String>();
  public List<String> listConfigs(){
    File[] files=new File(configsBase).listFiles();
    configs.clear();
    for(File f:files)
      configs.add(f.getName());
    return configs;
  }
  public Map<Integer,Page> getPages(){
    return assessment.getPages();
  }
  public Page getPage(Integer pageNumber){
    return assessment.getPages().get(pageNumber);
  }
  
  private String config;
  public void reset(){
    assessment.getPages().clear();
    run(config);
  }
  
  public void run(String config){
    try{
      int startingRow=2;
      int startingCol=1;
      if (config==null) throw new RuntimeException("No config was provided. Your session may have timed out.");
      if (config.endsWith(".xls")) config=config.substring(0, config.length()-4);
      
      this.config=config;
      
      InputStream questions=ResourceFactory.newFileResource(new File(configsBase+config+".xls")).getInputStream();
      InputStream questionsTemplate=ResourceFactory.newFileResource(new File(configsBase+"/questions.drl")).getInputStream();
      String questionsDrl = new ExternalSpreadsheetCompiler().compile(questions, "Questions", questionsTemplate, startingRow, startingCol);
      
      InputStream feedback=ResourceFactory.newFileResource(new File(configsBase+config+".xls")).getInputStream();
      InputStream feedbackTemplate=ResourceFactory.newFileResource(new File(configsBase+"/feedback.drl")).getInputStream();
      String feedbackDrl = new ExternalSpreadsheetCompiler().compile(feedback, "Feedback", feedbackTemplate, startingRow, startingCol);
      
//      String drl = new ExternalSpreadsheetCompiler().compile(data, template, InputType.XLS, startingRow, startingCol);
//      System.out.println(drl);
      KnowledgeBuilder qbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
      qbuilder.add(ResourceFactory.newByteArrayResource(questionsDrl.getBytes()), ResourceType.DRL);
      if (qbuilder.hasErrors()) System.out.println(questionsDrl);
      KnowledgeBase kBase = qbuilder.newKnowledgeBase();
      
      KnowledgeBuilder fbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
      fbuilder.add(ResourceFactory.newByteArrayResource(feedbackDrl.getBytes()), ResourceType.DRL);
      if (fbuilder.hasErrors()) System.out.println(feedbackDrl);
      KnowledgeBase fkBase = fbuilder.newKnowledgeBase();
      
      StatefulKnowledgeSession session=kBase.newStatefulKnowledgeSession();
      assessment=new Assessment();
      assessment.feedbackKBase=fkBase;
      session.insert(assessment);
      session.fireAllRules();
      
      // set the last page
      Page lastPage=assessment.getPages().get(assessment.getPages().size());
      if (null!=lastPage) lastPage.setLast(true);
      
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  public Page getPageByName(String pageName) {
    return assessment.getPageByName(pageName);
  }
}
