package com.redhat.wizard;

import javax.servlet.http.HttpServletRequest;

public interface IControl {
  public String toControl(HttpServletRequest request, Integer pageNumber);
  public String getForm();
  public Integer getPageNumber();
  public void setAnswer(String value);
  public Integer getId();
  public String getQuestion();
}
