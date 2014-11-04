package com.redhat.wizard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;

import com.keypoint.PngEncoder;

public class ChartServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // get the chart from storage
    Main bean=(Main) request.getSession().getAttribute("bean");
    String pageNumber=request.getParameter("pageNumber");
    
    // set the content type so the browser can see this as it is
    response.setContentType("image/png");
    
    String clientCompany=bean.getPage(1).getControl("Client name").getAnswer();
    
    Page chartPage=bean.getPage(Integer.parseInt(pageNumber));
    JFreeChart chart=new SpiderWebGraph().createChart(clientCompany, bean, chartPage);
    
    // send the picture
    BufferedImage buf=chart.createBufferedImage(800, 425, null);
    PngEncoder encoder=new PngEncoder(buf, false, 0, 9);
    response.getOutputStream().write(encoder.pngEncode());
    
//    JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(response.getOutputStream());
//    JPEGEncodeParam param=encoder.getDefaultJPEGEncodeParam(buf);
//    param.setQuality(0.75f, true);
//    encoder.encode(buf, param);
  }
}
