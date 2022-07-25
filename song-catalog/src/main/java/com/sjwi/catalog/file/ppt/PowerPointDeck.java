/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.file.ppt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PowerPointDeck {

  private static final int MAX_NUMBER_OF_LINES_PER_PAGE = 8;
  private static final String NEW_LINE_DELIMITER = "\n";
  private static List<Character> BREAK_CHARS =
      new ArrayList<Character>(Arrays.asList(',', '!', ';', '.', ':'));

  private final String[] songBodyInChunks;
  private final int lineSizeMax;
  private final int defaultLineSize;

  public PowerPointDeck(String[] songBodyInChunks, int lineSizeMax, int defaultLineSize) {
    this.songBodyInChunks = songBodyInChunks;
    this.lineSizeMax = lineSizeMax;
    this.defaultLineSize = defaultLineSize;
  }

  public List<List<String>> getDeck() {
    return Arrays.stream(songBodyInChunks)
        .map(this::getSlidesFromChunk)
        .flatMap(l -> l.stream())
        .collect(Collectors.toList());
  }

  private List<List<String>> getSlidesFromChunk(String chunk) {
    List<String> chunkSplitIntoLines = Arrays.asList(chunk.split(NEW_LINE_DELIMITER));
    return combineSlidesIntoFinalSubDeck(
        reduceChunksToLinesLessThanLineSizeMax(
            chunkSplitIntoLines, getDefaultLinesPerSlide(chunkSplitIntoLines.size())));
  }

  private List<List<String>> combineSlidesIntoFinalSubDeck(
      List<List<String>> chunksInDefaultLinesPerSlide) {
    List<List<String>> slidesFromChunk = new ArrayList<List<String>>();
    for (int i = 0; i < chunksInDefaultLinesPerSlide.size(); i = i + 2) {
      if (chunksInDefaultLinesPerSlide.size() < i + 2) {
        slidesFromChunk.add(chunksInDefaultLinesPerSlide.get(i));
      } else {
        if (canTheNextTwoSlidesBeCombined(
            chunksInDefaultLinesPerSlide.get(i).size(),
            chunksInDefaultLinesPerSlide.get(i + 1).size())) {
          slidesFromChunk.add(
              combineTwoSlides(
                  chunksInDefaultLinesPerSlide.get(i), chunksInDefaultLinesPerSlide.get(i + 1)));
        } else {
          slidesFromChunk.add(chunksInDefaultLinesPerSlide.get(i));
          slidesFromChunk.add(chunksInDefaultLinesPerSlide.get(i + 1));
        }
      }
    }
    return slidesFromChunk;
  }

  private List<String> combineTwoSlides(List<String> firstSlide, List<String> secondSlide) {
    List<String> combinedSlide = firstSlide;
    combinedSlide.addAll(secondSlide);
    return combinedSlide;
  }

  private boolean canTheNextTwoSlidesBeCombined(int firstSlideSize, int secondSlideSize) {
    return firstSlideSize + secondSlideSize <= MAX_NUMBER_OF_LINES_PER_PAGE;
  }

  private List<List<String>> reduceChunksToLinesLessThanLineSizeMax(
      List<String> chunkSplitIntoLines, int defaultLinesPerSlide) {
    List<List<String>> chunksInDefaultLinesPerSlide =
        partitionListIntoSubLists(chunkSplitIntoLines, defaultLinesPerSlide);
    return chunksInDefaultLinesPerSlide.stream()
        .map(this::reduceLinesToLessThanLineSizeMax)
        .flatMap(
            s -> {
              List<List<String>> subLists = new ArrayList<List<String>>();
              if (s.size() > MAX_NUMBER_OF_LINES_PER_PAGE) {
                subLists = partitionListIntoSubLists(s, defaultLinesPerSlide);
              } else {
                subLists.add(s);
              }
              return subLists.stream();
            })
        .collect(Collectors.toList());
  }

  private List<String> reduceLinesToLessThanLineSizeMax(List<String> lines) {
    return lines.stream()
        .map(s -> reduceLineToLessThanLineSizeMax(s))
        .flatMap(s -> s.stream())
        .collect(Collectors.toList());
  }

  private List<String> reduceLineToLessThanLineSizeMax(String line) {
    List<String> lines = new ArrayList<String>();
    while (line.length() > lineSizeMax) {
      int index = splitStringAtEndOfWord(line);
      for (char c : BREAK_CHARS) {
        if (doesLineHaveSplittableDelimiter(line, String.valueOf(c))) index = line.lastIndexOf(c);
      }
      lines.add(line.substring(0, index));
      line = removeAddedSubstring(line, index);
    }
    lines.add(line.trim());
    return lines;
  }

  private int splitStringAtEndOfWord(String line) {
    int index = 0;
    while (line.charAt(defaultLineSize + index) != ' '
        && line.charAt(defaultLineSize + index) != NEW_LINE_DELIMITER.charAt(0)) {
      index = index + 1;
    }
    if (index > 10) {
      index = 0;
      while (line.charAt(defaultLineSize + index) != ' ') {
        index = index - 1;
      }
    }
    return index + defaultLineSize;
  }

  private String removeAddedSubstring(String line, int index) {
    return line.substring(index + 1).trim();
  }

  private List<List<String>> partitionListIntoSubLists(List<String> list, int partitionSize) {
    int size = list.size();

    int numberOfLists = size / partitionSize;
    if (size % partitionSize != 0) numberOfLists++;

    List<List<String>> partition = new ArrayList<List<String>>();
    for (int i = 0; i < numberOfLists; i++) {
      int fromIndex = i * partitionSize;
      int toIndex =
          (i * partitionSize + partitionSize < size) ? (i * partitionSize + partitionSize) : size;

      partition.add(new ArrayList<String>(list.subList(fromIndex, toIndex)));
    }
    if (partition.get(partition.size() - 1).size() == 1
        && partition.size() > 1
        && partition.get(partition.size() - 2).size() < MAX_NUMBER_OF_LINES_PER_PAGE) {
      partition.get(partition.size() - 2).add(partition.get(partition.size() - 1).get(0));
      partition.remove(partition.size() - 1);
    }

    return partition;
  }

  private boolean doesLineHaveSplittableDelimiter(String line, String delimiter) {
    return line.contains(delimiter)
        && (line.lastIndexOf(delimiter.charAt(0)) != line.length() - 2)
        && (line.lastIndexOf(delimiter.charAt(0)) < lineSizeMax)
        && ((line.length() - line.lastIndexOf(delimiter.charAt(0))) > 15)
        && (line.lastIndexOf(delimiter.charAt(0)) > 15);
  }

  private int getDefaultLinesPerSlide(int size) {
    while (size >= MAX_NUMBER_OF_LINES_PER_PAGE) size = size / 2;
    return size > 6 ? 4 : size;
  }
}
