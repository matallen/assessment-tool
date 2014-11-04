package com.redhat.wizard;

import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

public class SpiderWebGraph {

//  public SpiderWebGraph(String title) {
//    JFreeChart jfreechart=createChart(title, createDataset());
//  }

  private CategoryDataset createDataset(String title, Main bean, Page chartPage) {
    DefaultCategoryDataset defaultcategorydataset=new DefaultCategoryDataset();
    
    if (title==null) title="Unknown";
    
    for(String spoke:chartPage.getControls().get(0).getOptions()){
      Page page=bean.getPageByName(spoke.trim());
      int weighting=1;
      for(Control c:page.getControls()){
        Control ctl=(Control)c;
        if (null!=ctl.getAnswer())
          weighting+=Integer.parseInt(ctl.getAnswer());
      }
      System.out.println("dataset.addValue("+weighting+", '"+title +"','"+spoke+"');");
      defaultcategorydataset.addValue(weighting, title, spoke);
    }
    
    return defaultcategorydataset;
  }

  public JFreeChart createChart(String title, Main bean, Page chartPage) {
    return createChart(title, createDataset(title, bean, chartPage));
  }
  
  public JFreeChart createChart(String title, CategoryDataset categorydataset) {
    SpiderWebPlot spiderwebplot=new SpiderWebPlot(categorydataset);
    JFreeChart jfreechart=new JFreeChart(title, TextTitle.DEFAULT_FONT, spiderwebplot, false);
    LegendTitle legendtitle=new LegendTitle(spiderwebplot);
    legendtitle.setPosition(RectangleEdge.BOTTOM);
    jfreechart.addSubtitle(legendtitle);
    return jfreechart;
  }
}
