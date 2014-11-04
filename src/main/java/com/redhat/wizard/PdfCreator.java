package com.redhat.wizard;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfCreator {

  public void create() throws DocumentException, IOException {
      // step 1
      Document document = new Document();
      // step 2
      ByteArrayOutputStream baos=new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);
      // step 3
      document.open();
      // step 4
      document.add(new Paragraph("Hello World!"));
      // step 5
      document.close();
  }
}
