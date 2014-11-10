package com.redhat.wizard;

import javax.servlet.http.HttpServletRequest;

public class Control implements IControl{
  private int pageNumber;
  private String form;
  private Integer id;
  private String question;
  private String type;
  private String[] options;
  private String answer;
  private Integer weighting;
  public Control(String form, String type) {
    super();
    this.form=form;
    this.type=type;
  }
  public Control(int pageNumber, String form, Integer id, String question, String type) {
    super();
    this.pageNumber=pageNumber;
    this.form=form;
    this.id=id;
    this.question=question;
    this.type=type;
  }
  public Control(int pageNumber, String form, Integer id, String question, String type, String options, Integer weighting) {
    super();
    this.pageNumber=pageNumber;
    this.form=form;
    this.id=id;
    this.question=question;
    this.type=type;
    this.options=options.split(",");
    this.weighting=weighting;
  }
  public Control(int pageNumber, String form, Integer id, String question, String type, String options) {
    super();
    this.pageNumber=pageNumber;
    this.form=form;
    this.id=id;
    this.question=question;
    this.type=type;
    this.options=options.split(",");
    this.weighting=1;
  }
  public Integer getPageNumber(){
    return pageNumber;
  }
  public String getForm() {
    return form;
  }
  public Integer getId() {
    return id;
  }
  public String getQuestion() {
    return question;
  }
  public String getType() {
    return type;
  }
  public String[] getOptions() {
    return options;
  }
  public Integer getWeighting(){
    return weighting;
  }
  public String toString(){
    return "Control("+form+"->"+question+"; answer="+answer+")";
  }
  public String toControl(HttpServletRequest request, Integer pageNumber){
    String label="<label class=\"label\" for=\""+id+"\">"+getQuestion()+"</label>";
    String control="";
    if ("textbox".equalsIgnoreCase(type)){
      control="<input class=\"control\" type=\"text\" name=\""+id+"\" value=\""+(answer!=null?answer:"")+"\"/>";
    }else if ("radio".equalsIgnoreCase(type)){
      for(String option:getOptions()){
        String checked=answer!=null && answer.equals(option)?" checked":"";
        control+="<input type=\"radio\" name=\""+id+"\" value=\""+option+"\""+checked+">"+option+"<br>";
      }
    }else if ("select".equalsIgnoreCase(type)){
      control="<select name=\""+id+"\">";
      int i=0;
      for(String option:getOptions()){
        i+=1;
//        String selected=answer!=null && answer.equals(option)?" selected":"";
//        control+="<option value=\""+option+"\""+selected+">"+option+"</option>";
        String selected=answer!=null && answer.equals(String.valueOf(i))?" selected":"";
//        System.out.println("XXX="+answer+" == "+ String.valueOf(i) +" === "+ String.valueOf(i).equals(answer));
        control+="<option value=\""+i+"\""+selected+">"+option+"</option>";
      }
      control+="</select>";
    }else if ("graph".equalsIgnoreCase(type)){
      control="<img src=\""+request.getContextPath()+"/graph?pageNumber="+pageNumber+"\"/>";
    }
//    return "<p class=\"text\" >"+label+"&nbsp;"+control+"</p>";
    return "<tr><td valign=\"top\" class=\"td-label-wrapper\">"+label+"</td><td class=\"td-control-wrapper\">"+control+"</td></tr>";
  }
  public void setAnswer(String value) {
    this.answer=value;
  }
  public String getAnswer(){
    return this.answer;
  }
}
