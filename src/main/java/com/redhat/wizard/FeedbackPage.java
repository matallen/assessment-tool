package com.redhat.wizard;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.drools.runtime.StatefulKnowledgeSession;

public class FeedbackPage extends Page {
  private Assessment assessment;
  public FeedbackPage(Integer number, String name) {
    super(number, name);
  }
  public FeedbackPage(Integer number, String name, Assessment assessment) {
    super(number, name);
    this.assessment=assessment;
  }
  
  public String display(HttpServletRequest request){
    StringBuffer sb=new StringBuffer();
    LinkedList<Feedback> feedbackList=new LinkedList<Feedback>();
    for(Page page:assessment.getPages().values()){
      
      StatefulKnowledgeSession session=assessment.feedbackKBase.newStatefulKnowledgeSession();
      Integer score=0;
      for(Control q:page.getControls()){
        if (q.getAnswer()==null) continue;
        if (!q.getType().equalsIgnoreCase("radio") && !q.getType().equalsIgnoreCase("select")) continue;
        // only include radio and select boxes in metrics
        System.out.println("Adding control ["+q.getId()+"] to score metrics with a value of ["+q.getAnswer()+"]");
        Integer qScore=Integer.parseInt(q.getAnswer());
        Integer weighting=q.getWeighting();
        score+=(qScore*weighting);
        
      }
      Feedback feedback=new Feedback(page.getName(), score);
      feedbackList.add(feedback);
      session.insert(feedback);
      session.fireAllRules();
    }
    sb.append("<table border=1>");
    for(Feedback fb:feedbackList){
      if (fb.getText()==null) continue;
      System.out.println("Feedback for [page="+fb.getPage()+", score="+fb.getScore()+"] is "+fb.getText());
      sb.append("<tr><td>"+fb.getPage()+"</td><td>"+fb.getText()+"</td></tr>");
    }
    sb.append("</table>");
    
//    
//    for(Feedback feedback:assessment.getFeedback().values()){
//      String pageName=feedback.getPage();
//      Page page=assessment.getPageByName(pageName);
//      Integer score=0;
//      for(Control q:page.getControls()){
//        Integer qScore=Integer.parseInt(q.getAnswer());
//        Integer weighting=q.getWeighting();
//        score+=score + (qScore*weighting);
//      }
//      System.out.println("Feedback score for ["+pageName+"] is "+score);
//    }
    
    return sb.toString();  
  }
}
