package com.redhat.wizard;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Page {
  private LinkedList<Control> controls;
  private Integer number;
  private String name;
  private boolean isLast;
  
  public Page(Integer number, String name){
    this.number=number;
    this.name=name;
    this.isLast=false;
    this.controls=new LinkedList<Control>();
  }
  
  public String display(HttpServletRequest request){
    StringBuffer sb=new StringBuffer();
    if (null!=getControls()){
      for (Control ctl:getControls()){
          sb.append(ctl.toControl(request, getNumber()));
      }
    }
    return sb.toString();
  }
  
  public Integer getNumber(){
    return number;
  }
  public String getName(){
    return name;
  }
  public LinkedList<Control> getControls(){
    return controls;
  }
  public boolean isLast(){
    return isLast;
  }
  public void setLast(boolean b) {
    this.isLast=b;
  }

  public Control getControl(String string) {
    for(Control c:controls){
      if (string.equalsIgnoreCase(c.getQuestion())){
        return (Control)c;
      }
    }
    return null;
  }

}
