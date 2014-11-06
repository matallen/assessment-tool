package com.redhat.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

public class FileUploadServlet extends HttpServlet {
  private static final long serialVersionUID=8131747609577498479L;
  private final String UPLOAD_DIRECTORY=Main.configsBase;//"/tmp/questionnairre";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (null!=request.getParameter("delete")){
      String configName=request.getParameter("delete");
      new File(UPLOAD_DIRECTORY, configName).delete();
      request.getRequestDispatcher("/configuration.jsp").forward(request, response);
      
    }else{
      File file=new File(UPLOAD_DIRECTORY, request.getParameter("download"));
      response.setContentType("application/vnd.ms-excel");
      response.setContentLength((int) file.length());
      response.setHeader("Content-Disposition", "attachment; filename=\""+file.getName()+"\"");
      IOUtils.copy(new FileInputStream(file), response.getOutputStream());
//      request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // process only if its multipart content
    if (ServletFileUpload.isMultipartContent(request)) {
      try {
        List<FileItem> multiparts=new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

        for (FileItem item : multiparts)
          if (!item.isFormField()) 
            item.write(new File(UPLOAD_DIRECTORY+File.separator+new File(item.getName()).getName()));

//        request.setAttribute("message", "File Uploaded Successfully");
      } catch (Exception ex) {
//        request.setAttribute("message", "File Upload Failed due to "+ex);
      }
//    } else {
//      request.setAttribute("message", "Sorry this Servlet only handles file upload request");
    }

    request.getRequestDispatcher("/configuration.jsp").forward(request, response);

  }

}