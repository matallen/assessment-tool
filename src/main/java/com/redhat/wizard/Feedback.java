package com.redhat.wizard;

public class Feedback {
  private String page;
  private int score;
  private String text;
  
  public Feedback(String page, Integer score){
    this.page=page;
    this.score=score;
  }
  
  public Integer getScore(){
    return score;
  }
  
  public String getPage() {
    return page;
  }

  public String getText() {
    return text;
  }
  
  public void setText(String text){
    this.text=text;
  }
}
