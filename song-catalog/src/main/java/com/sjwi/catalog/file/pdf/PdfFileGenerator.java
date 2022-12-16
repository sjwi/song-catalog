/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.file.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.Song;

public class PdfFileGenerator implements FileGenerator {

  private static final String SUFFIX = ".pdf";
  private static final String PREFIX = "pdfdoc";
  public static final String PDF_SUB_DIRECTORY = "pdf_dir";
  private static final int DEF_SONG_FONT = 12;
  private static final int DEF_TITLE_FONT = 14;
  private static final int DEF_LEADING = 18;
  private static final int DEF_TITLE_LEADING = 30;
  private static final int DEF_HEADER_FOOTER_FONT = 10;
  private static final String FONT_KEY = FontFactory.HELVETICA;
  private static final String FONT_BOLD_KEY = FontFactory.HELVETICA_BOLD;
  private final Font songFont;
  private final Font titleFont;
  private final Font headerFooterFont;
  private final Font headerFooterFontBold;
  private final int leading;
  private final int titleLeading;
  private final String file;
  private final boolean includeQrCode;
  private BufferedImage qrCode;
  private PdfWriter writer;
  private Document document;
  private String currentSongTitle;
  private int pageNumber = 1;

  public PdfFileGenerator(int fontSize, String qrCode) throws FileUtilityException {
    try {
      String root =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
              .getRequest()
              .getServletContext()
              .getRealPath("/");
      FontFactory.register("fonts/bold.ttf");
      FontFactory.register("fonts/normal.ttf");
      if (fontSize == 0) {
        songFont = generateFont(DEF_SONG_FONT, false);
        titleFont = generateFont(DEF_TITLE_FONT, true);
        headerFooterFont = generateFont(DEF_HEADER_FOOTER_FONT, false);
        headerFooterFontBold = generateFont(DEF_HEADER_FOOTER_FONT, true);
        titleLeading = DEF_TITLE_LEADING;
        leading = DEF_LEADING;
      } else {
        fontSize = fontSize > 25 ? 25 : fontSize < 6 ? 6 : fontSize;
        songFont = generateFont(fontSize, false);
        titleFont = generateFont(fontSize + 2, true);
        if (fontSize > 16 && fontSize <= 20) {
          leading = 24;
          titleLeading = 40;
          headerFooterFont = generateFont(11, false);
          headerFooterFontBold = generateFont(11, true);
        } else if (fontSize > 20) {
          leading = 28;
          titleLeading = 50;
          headerFooterFont = generateFont(11, false);
          headerFooterFontBold = generateFont(11, true);
        } else {
          leading = DEF_LEADING;
          titleLeading = DEF_TITLE_LEADING;
          headerFooterFont = generateFont(DEF_HEADER_FOOTER_FONT, false);
          headerFooterFontBold = generateFont(DEF_HEADER_FOOTER_FONT, true);
        }
      }
      file =
          root + "/" + PDF_SUB_DIRECTORY + "/" + PREFIX + "_" + System.currentTimeMillis() + SUFFIX;
      new File(root + "/" + PDF_SUB_DIRECTORY).mkdir();
      document = new Document();
      writer = PdfWriter.getInstance(document, new FileOutputStream(file));
      if (qrCode != null) {
        includeQrCode = true;
        this.qrCode = generateQRCodeImage(qrCode);
      } else {
        includeQrCode = false;
      }
    } catch (Exception e) {
      throw new FileUtilityException("Unable to initialize PDF file generator", e);
    }
  }

  private Font generateFont(int size, boolean bold) {
    return FontFactory.getFont(bold ? FONT_BOLD_KEY : FONT_KEY, size, BaseColor.BLACK);
  }

  @Override
  public String buildFile(SetList setList) throws FileUtilityException {

    try {
      writer.setPageEvent(new DrawHeaderAndFooter(setList));
      for (Song song : setList.getSongs()) {
        if (!document.isOpen()) {
          document.open();
          document.addTitle(setList.getSetListName());
          document.addSubject(setList.getSetListName());
        } else {
          pageNumber = writer.getPageNumber() + 1;
          document.newPage();
        }
        drawSong(song.getName(), song.getBodyAsChunks());
      }
    } catch (Exception e) {
      throw new FileUtilityException(
          "Unable to build pdf file for set list " + setList.getSetListName(), e);
    } finally {
      cleanup();
    }

    return file;
  }

  @Override
  public String buildFile(Song song) throws FileUtilityException {

    try {
      writer.setPageEvent(new DrawHeaderAndFooter(song));
      document.open();
      document.addTitle(song.getName());
      document.addSubject(song.getName());
      drawSong(song.getName(), song.getBodyAsChunks());
    } catch (Exception e) {
      throw new FileUtilityException("Unable to build pdf for song " + song.getName(), e);
    } finally {
      cleanup();
    }
    return file;
  }

  private void drawSong(String name, String[] body) throws DocumentException {
    currentSongTitle = name;
    document.add(buildSongTitle(name));
    for (int i = 0; i < body.length; i++) {
      Paragraph p = new Paragraph(15);
      p.setIndentationLeft(20);
      p.setLeading(leading);
      p.add(buildSongBody(body[i]));
      p.setKeepTogether(true);
      document.add(p);
    }
  }

  private Phrase buildSongBody(String songBody) throws DocumentException {

    songBody = songBody.replace("\t", "      ");
    String[] body = songBody.split("\n");
    songBody = "";
    for (int i = 0; i < body.length; i++) {
      if (TransposableString.isLineOnlyChords(body[i])) {
        body[i] = body[i].replace("    ", "   ");
      }
      songBody = songBody + body[i] + "\n";
    }
    return new Phrase(new Chunk(songBody + "\n", songFont));
  }

  private Paragraph buildSongTitle(String songName) {

    Paragraph titleParagraph = new Paragraph(new Chunk(songName, titleFont));
    titleParagraph.setAlignment(Element.ALIGN_CENTER);
    titleParagraph.setLeading(titleLeading);
    titleParagraph.setSpacingAfter(25);
    return titleParagraph;
  }

  private void cleanup() throws FileUtilityException {
    if (document.isOpen()) {
      document.close();
    } else {
      try {
        document.open();
        document.add(buildSongTitle("Empty set list"));
        document.close();
      } catch (Exception e) {
        throw new FileUtilityException(e);
      }
    }
  }

  private BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
    return MatrixToImageWriter.toBufferedImage(
        new QRCodeWriter().encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200));
  }

  @Override
  public String getFilePath() {
    return file;
  }

  @Override
  public void removeFile() throws FileUtilityException {
    try {
      Files.deleteIfExists(Paths.get(file));
    } catch (Exception e) {
      throw new FileUtilityException(e);
    }
  }

  public class DrawHeaderAndFooter extends PdfPageEventHelper {
    private final String headerText;
    private int additionalPageCounter = 1;
    private final boolean includePageNumberInHeader;

    DrawHeaderAndFooter(Song song) {
      this.headerText = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
      includePageNumberInHeader = false;
    }

    DrawHeaderAndFooter(SetList setList) {
      this.headerText = setList.getSetListName();
      includePageNumberInHeader = true;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
      if (document.getPageNumber() == 1 && includeQrCode) {
        try {
          addHeader("[ share  pdf ]", 579, 753, Element.ALIGN_RIGHT);
          addHeader(headerText, 55, 815, Element.ALIGN_LEFT);
          document.add(getQrCodeImage());
        } catch (DocumentException | IOException e) {
          e.printStackTrace();
        }
      } else {
        addHeader(headerText, 545, 815, Element.ALIGN_RIGHT);
      }
      addFooter(writer, document.getPageNumber());
      if (pageNumber != document.getPageNumber()) {
        additionalPageCounter++;
        addCurrentSongTitle();
        pageNumber = document.getPageNumber();
      } else {
        additionalPageCounter = 1;
      }
    }

    private void addHeader(String headerText, int x, int y, int alignment) {
      ColumnText.showTextAligned(
          writer.getDirectContent(), alignment, new Phrase(headerText, headerFooterFont), x, y, 0);
    }

    private void addCurrentSongTitle() {
      Phrase headerPhrase = new Phrase(currentSongTitle, headerFooterFontBold);
      if (includePageNumberInHeader) {
        headerPhrase.add("  |  Page " + additionalPageCounter);
      }
      ColumnText.showTextAligned(
          writer.getDirectContent(), Element.ALIGN_LEFT, headerPhrase, 55, 815, 0);
      addAdditionalPageSpacer();
    }

    private void addFooter(PdfWriter writer, int count) {
      Phrase pageNumber = new Phrase(Integer.toString(count), headerFooterFont);
      Phrase licenseText = new Phrase(LICENSE_TEXT, headerFooterFont);
      ColumnText.showTextAligned(
          writer.getDirectContent(), Element.ALIGN_LEFT, licenseText, 55, 20, 0);
      ColumnText.showTextAligned(
          writer.getDirectContent(), Element.ALIGN_RIGHT, pageNumber, 545, 20, 0);
    }

    private void addAdditionalPageSpacer() {
      Paragraph paragraph = new Paragraph();
      paragraph.setPaddingTop(20);
      paragraph.add(new Chunk(" "));
      try {
        document.add(paragraph);
      } catch (DocumentException e) {
      }
    }

    private Image getQrCodeImage() throws IOException, BadElementException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(qrCode, "png", baos);
      Image img = Image.getInstance(baos.toByteArray());
      img.scaleToFit(80, 80);
      img.setAbsolutePosition(508, 755);
      return img;
    }
  }

  @Override
  public void close() {
    try {
      if (document.isOpen()) document.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
