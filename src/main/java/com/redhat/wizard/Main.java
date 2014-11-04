package com.redhat.wizard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.decisiontable.InputType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

public class Main {
  public static final String configsBase="src/main/resources/";
  
  
  @SuppressWarnings("unused")
  public static void main(String[] args){
    Main bean=new Main();
    bean.run("config");
    Page page1=bean.getPage(1);
    System.out.println(page1);
  }
  
  public Map<Integer, Page> pages=new LinkedHashMap<Integer,Page>();
  public String title;
  public String getTitle(){return this.title;};
  
  
  public List<String> configs=new ArrayList<String>();
  public List<String> listConfigs(){
    File[] files=new File(configsBase).listFiles();
    configs.clear();
    for(File f:files)
      configs.add(f.getName());
    return configs;
  }
  
  public Page getPage(Integer pageNumber){
    return pages.get(pageNumber);
  }
  
  
  public void run(String config){
    try{
      int startingRow=2;
      int startingCol=1;
      
      InputStream data=ResourceFactory.newFileResource(new File(configsBase+config+".xls")).getInputStream();
      InputStream template=ResourceFactory.newFileResource(new File(configsBase+"/template.drl")).getInputStream();
      
      String drl = new ExternalSpreadsheetCompiler().compile(data, template, InputType.XLS, startingRow, startingCol);
      System.out.println(drl);
      KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
      builder.add(ResourceFactory.newByteArrayResource(drl.getBytes()), ResourceType.DRL);
      KnowledgeBase kBase = builder.newKnowledgeBase();
      
      StatefulKnowledgeSession session=kBase.newStatefulKnowledgeSession();
      List<Control> controlsReversed=new LinkedList<Control>();
      session.insert(controlsReversed);
      session.fireAllRules();
      
//      Map<String,List<IControl>> results=new LinkedHashMap<String,List<IControl>>();
      
      pages.clear();
      
//      Map<String, Page> myPages=new HashMap<String, Page>();
      
//      List<Control> controls=new LinkedList<Control>();
//      for(FactHandle fh:session.getFactHandles()){
//        controls.add((Control)session.getObject(fh));
//      }
//      Collections.sort(controls, new Comparator<Control>() {
//        public int compare(Control o1, Control o2) {
//          return o2.getId()-o1.getId();
//        }
//      });
      for(int i=controlsReversed.size()-1;i>=0;i--){
        if ("title".equalsIgnoreCase(controlsReversed.get(i).getType()))
          title=controlsReversed.remove(i).getForm();
      }
      List<Control> controls=new LinkedList<Control>();
      for(int i=controlsReversed.size()-1;i>=0;i--){
        controls.add(controlsReversed.get(i)); // this is because salience is the reverse order we need
      }
      
      for(Control c:controls){
        Page page=pages.get(c.getPageNumber());
        if (page==null) page=new Page(c.getPageNumber(), c.getForm());
        page.getControls().add(c);
        pages.put(page.getNumber(), page);
      }
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  public Page getPageByName(String pageToIncludeInGraph) {
    for(Entry<Integer, Page> e:pages.entrySet()){
      if (pageToIncludeInGraph.equalsIgnoreCase(e.getValue().getName())){
        return e.getValue();
      }
    }
    return null;
  }
}
