/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.file.ppt;

import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.song.Song;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PptFileGenerator implements FileGenerator {

  private static final String SUFFIX = ".pptx";
  private static final String PREFIX = "slideshow";
  public static final String PPT_SUB_DIRECTORY = "ppt_dir";
  private static final int DEF_LINE_SIZE = 20;
  private static final int DEF_LINE_SIZE_MAX = 41;
  private static final int DEF_TITLE_SIZE_MAX = 38;
  private static final int DEF_FONT_SIZE = 39;
  private static final int MARGIN = 20;
  private static final int DEF_TITLE_FONT_SIZE = 41;
  private static final int LINE_SPACING = 100;

  private final int fontSize;
  private final int titleFontSize;
  private final int lineSize;
  private final int lineSizeMax;
  private final int titleSizeMax;
  private final int lineSpacing;
  private final boolean prependBlankSlide;
  private final boolean blankSlideDelimiter;
  private final TextAlign textAlign;
  public final String file;
  private XMLSlideShow ppt;
  private XSLFSlideLayout layout;
  private XSLFSlide slide;

  public PptFileGenerator(
      boolean prependBlankSlide, int fontSize, boolean alignCenter, boolean blankSlideDelimiter) {
    String root =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest()
            .getServletContext()
            .getRealPath("/");
    file =
        root + "/" + PPT_SUB_DIRECTORY + "/" + PREFIX + "_" + System.currentTimeMillis() + SUFFIX;
    new File(root + "/" + PPT_SUB_DIRECTORY).mkdir();
    this.prependBlankSlide = prependBlankSlide;
    this.blankSlideDelimiter = blankSlideDelimiter;

    if (fontSize == 0) {
      this.fontSize = DEF_FONT_SIZE;
      lineSize = DEF_LINE_SIZE;
      lineSizeMax = DEF_LINE_SIZE_MAX;
      titleSizeMax = DEF_TITLE_SIZE_MAX;
      lineSpacing = LINE_SPACING;
      titleFontSize = DEF_TITLE_FONT_SIZE;
    } else {
      fontSize = fontSize > 50 ? 50 : fontSize < 30 ? 30 : fontSize;
      this.fontSize = fontSize - 1;
      lineSize = DEF_LINE_SIZE + (DEF_FONT_SIZE - this.fontSize);
      lineSizeMax = DEF_LINE_SIZE_MAX + (DEF_FONT_SIZE - this.fontSize);
      titleSizeMax = DEF_TITLE_SIZE_MAX + (DEF_FONT_SIZE - this.fontSize);
      lineSpacing = LINE_SPACING + (2 * (DEF_FONT_SIZE - this.fontSize));
      titleFontSize = this.fontSize + 3;
    }
    textAlign = alignCenter ? TextAlign.CENTER : TextAlign.LEFT;
    ppt = new XMLSlideShow();
    XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
    layout = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
  }

  @Override
  public String buildFile(Song song) throws FileUtilityException {

    try {
      if (prependBlankSlide) drawBlankSlide();
      drawSong(new PowerPointDeck(song.getBodyAsChunks(), lineSizeMax, lineSize), song.getName());
      writeFileToSystem();
    } catch (Exception e) {
      throw new FileUtilityException("Unable to write to Ppt file for song " + song.getName(), e);
    }

    return file;
  }

  @Override
  public String buildFile(SetList setList) throws FileUtilityException {

    try {
      if (prependBlankSlide) drawBlankSlide();
      for (Song song : setList.getSongs()) {
        drawSong(new PowerPointDeck(song.getBodyAsChunks(), lineSizeMax, lineSize), song.getName());
        if (blankSlideDelimiter) drawBlankSlide();
      }
      writeFileToSystem();
    } catch (Exception e) {
      throw new FileUtilityException(
          "Unable to write to Ppt file for set list " + setList.getSetListName(), e);
    }

    return file;
  }

  private void writeFileToSystem() throws IOException {
    FileOutputStream out = new FileOutputStream(file);
    ppt.write(out);
    out.close();
    ppt.close();
  }

  private void drawSong(PowerPointDeck powerPointDeck, String title) {
    for (List<String> singleSlide : powerPointDeck.getDeck()) {
      createNewSlide();
      drawTitle(
          title.length() > titleSizeMax
              ? title = title.substring(0, titleSizeMax - 4) + "..."
              : title);
      drawContent(singleSlide);
      drawFooter();
    }
  }

  private void createNewSlide() {
    slide = ppt.createSlide(layout);
    if (slide.getXmlObject().getCSld().getBg() == null) slide.getXmlObject().getCSld().addNewBg();
    slide.getBackground().setFillColor(Color.BLACK);
  }

  private void drawTitle(String title) {

    XSLFTextShape titleShape = slide.getPlaceholder(0);
    XSLFTextParagraph p = titleShape.getTextParagraphs().get(0);
    XSLFTextRun titleRun = p.getTextRuns().get(0);
    titleRun.setText(title);
    titleRun.setFontColor(Color.WHITE);
    titleRun.setBold(true);
    titleRun.setFontSize((double) titleFontSize);
  }

  private void drawContent(List<String> lines) {

    XSLFTextShape contentShape = slide.getPlaceholder(1);
    contentShape.clearText();
    Rectangle2D rect = contentShape.getAnchor();
    rect.setRect(0, 110, 720, 420);
    contentShape.setAnchor(rect);
    contentShape.setLeftInset(0.);
    contentShape.setRightInset(0.);
    XSLFTextParagraph cp = contentShape.addNewTextParagraph();
    cp.setBullet(false);
    cp.setIndent(0.);
    cp.setLeftMargin(Double.valueOf(MARGIN));
    cp.setRightMargin(0.);
    cp.setLineSpacing((double) lineSpacing);
    cp.setBulletStyle();
    cp.setTextAlign(textAlign);
    for (String line : lines) {

      XSLFTextRun contentRun = cp.addNewTextRun();
      contentRun.setText(line);
      contentRun.setFontColor(Color.WHITE);
      contentRun.setFontSize((double) fontSize);
      cp.addLineBreak();
    }
  }

  private void drawFooter() {

    XSLFTextBox footer = slide.createTextBox();
    footer.clearText();
    footer.setLeftInset(0.);
    footer.setWordWrap(false);
    footer.setRightInset(0.);
    XSLFTextParagraph footerParagraph = footer.addNewTextParagraph();
    footerParagraph.setRightMargin(0.);
    XSLFTextRun r = footerParagraph.addNewTextRun();
    r.setText(LICENSE_TEXT);
    r.setFontSize(20.);
    r.setFontColor(Color.WHITE);
    Rectangle2D footerRect = footer.getAnchor();
    footerRect.setRect(430, 500, 300, 30);
    footer.setAnchor(footerRect);
  }

  private void drawBlankSlide() {

    XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
    XSLFSlideLayout blankSlideLayout = defaultMaster.getLayout(SlideLayout.BLANK);
    slide = ppt.createSlide(blankSlideLayout);
    if (slide.getXmlObject().getCSld().getBg() == null) slide.getXmlObject().getCSld().addNewBg();
    slide.getBackground().setFillColor(Color.BLACK);
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

  @Override
  public void close() {
    try {
      ppt.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
