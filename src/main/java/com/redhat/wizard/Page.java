package com.redhat.wizard;

import java.util.LinkedList;
import java.util.List;

public class Page {
  private List<Control> controls;
  private Integer number;
  private String name;
  
  public Page(Integer number, String name){
    this.number=number;
    this.name=name;
    this.controls=new LinkedList<Control>();
  }
  
  public Integer getNumber(){
    return number;
  }
  public String getName(){
    return name;
  }
  public List<Control> getControls(){
    return controls;
  }

  public Control getControl(String string) {
    for(IControl c:controls){
      if (string.equalsIgnoreCase(c.getQuestion())){
        return (Control)c;
      }
    }
    return null;
  }
}
