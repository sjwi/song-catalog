package com.sjwi.cfsongs.model;

import static com.sjwi.cfsongs.model.KeySet.LYRICS_ONLY_KEY_CODE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.sjwi.cfsongs.config.KeySetConfiguration;

public class TransposableString {

	private static final String SPLIT_LINE_DELIMITER = "\\n";
	private static final String APPEND_LINE_DELIMITER = "\n";
	private static final List<String> CHORD_CHART_KEYWORDS = new ArrayList<String>(Arrays.asList("min","maj","dim","minor","major","sus","dom","aug"));
	private static final List<String> STRIPPABLE_KEYWORDS = new ArrayList<String>(Arrays.asList("verse","chorus","bridge","prechorus","refrain","interlude"));
	private final List<String> transposableStringLines;
	private final KeySet storedKey;
	
	public TransposableString(String transposableString, String storedKey) {
		this.transposableStringLines = formatRawString(transposableString);
		this.storedKey = getKeyFromMaster(storedKey);
	}
	
	public boolean isStringIsTransposable() {
		return (transposableStringLines.stream().filter(l -> !isLineOnlyChords(l)).count() == 0);
	}

	public String getTransposedString(String transposeTo) {
		if (LYRICS_ONLY_KEY_CODE.equalsIgnoreCase(transposeTo)) {
			return getLyricsFromTransposableString();
		} else {
			return transposeStringLines(getKeyFromMaster(transposeTo));
		}
	}
	
	public String getSourceString() {
		return String.join(APPEND_LINE_DELIMITER,transposableStringLines);
	}

	private String transposeStringLines(KeySet transpositionKey) {
		String transposedString = "";
		for (String line: transposableStringLines) {
			transposedString = transposedString +
					(isLineOnlyChords(line) && !storedKey.getKeyId().equals(LYRICS_ONLY_KEY_CODE)? transposeChordLine(line, storedKey, transpositionKey): line) + 
					APPEND_LINE_DELIMITER;
		}
		return transposedString.trim();
	}

	private String getLyricsFromTransposableString() {
		String lyrics = "";
		for (String line: transposableStringLines) {
			if (!isLineOnlyChords(line)) {
				lyrics = lyrics + line + APPEND_LINE_DELIMITER;
			}
		}
		return lyrics.trim();
	}

	public static boolean isLineOnlyChords(String line) {

		line = line.toLowerCase();
		
		if (line.contains("h")){return false; }
		if (line.contains("j")){if (!doesLineContainChordKeyword(line,"j")){ return false; }}
		if (line.contains("k")){return false; }
		if (line.contains("l")){return false; }
		if (line.contains("n")){if (!doesLineContainChordKeyword(line,"n")){ return false; }}
		if (line.contains("o")){return false; }
		if (line.contains("p")){return false; }
		if (line.contains("q")){return false; }
		if (line.contains("r")){return false; }
		if (line.contains("s")){if (!doesLineContainChordKeyword(line,"s")){ return false; }}
		if (line.contains("t")){return false; }
		if (line.contains("u")){if (!doesLineContainChordKeyword(line,"u")){ return false; }}
		if (line.contains("w")){return false; }
		if (line.contains("x")){return false; }
		if (line.contains("y")){return false; }
		if (line.contains("z")){return false; }
		if (line.trim().isEmpty()){return false; }
		return true;
	}
	
	private static boolean doesLineContainChordKeyword(String line, String character) {

		return (CHORD_CHART_KEYWORDS.stream()
				.filter(c -> c.contains(character))
				.filter(c -> line.contains(c))
				.collect(Collectors.toList()).size() > 0);
	}

	/*
	 * Ugliest method you've ever seen.
	 * First checks for sharps or flats in the chord line
	 * Next Replaces all instances found of the default keyset found
	 * (due to the number system storage format, the order in which chord is replaced first matters)
	 */
	private String transposeChordLine(String line, KeySet defKey, KeySet transKey) {
		
		if (defKey.getI().contains("#")) {
			line = line.replace(defKey.getI(), transKey.getI());
		}
		if (defKey.getiSharp().contains("#")) {
			line = line.replace(defKey.getiSharp(), transKey.getiSharp());
		}
		if (defKey.getIi().contains("#")) {
			line = line.replace(defKey.getIi(), transKey.getIi());
		}
		if (defKey.getIii().contains("#")) {
			line = line.replace(defKey.getIii(), transKey.getIii());
		}
		if (defKey.getIV().contains("#")) {
			line = line.replace(defKey.getIV(), transKey.getIV());
		}
		if (defKey.getIvSharp().contains("#")) {
			line = line.replace(defKey.getIvSharp(), transKey.getIvSharp());
		}
		if (defKey.getV().contains("#")) {
			line = line.replace(defKey.getV(), transKey.getV());
		}
		if (defKey.getVi().contains("#")) {
			line = line.replace(defKey.getVi(), transKey.getVi());
		}
		if (defKey.getViSharp().contains("#")) {
			line = line.replace(defKey.getViSharp(), transKey.getViSharp());
		}
		if (defKey.getVii().contains("#")) {
			line = line.replace(defKey.getVii(), transKey.getVii());
		}
		if (defKey.getI().contains("b")) {
			line = line.replace(defKey.getI(), transKey.getI());
		}
		if (defKey.getiSharp().contains("b")) {
			line = line.replace(defKey.getiSharp(), transKey.getiSharp());
		}
		if (defKey.getIi().contains("b")) {
			line = line.replace(defKey.getIi(), transKey.getIi());
		}
		if (defKey.getIii().contains("b")) {
			line = line.replace(defKey.getIii(), transKey.getIii());
		}
		if (defKey.getIV().contains("b")) {
			line = line.replace(defKey.getIV(), transKey.getIV());
		}
		if (defKey.getIvSharp().contains("b")) {
			line = line.replace(defKey.getIvSharp(), transKey.getIvSharp());
		}
		if (defKey.getV().contains("b")) {
			line = line.replace(defKey.getV(), transKey.getV());
		}
		if (defKey.getVi().contains("b")) {
			line = line.replace(defKey.getVi(), transKey.getVi());
		}
		if (defKey.getViSharp().contains("b")) {
			line = line.replace(defKey.getViSharp(), transKey.getViSharp());
		}
		if (defKey.getVii().contains("b")) {
			line = line.replace(defKey.getVii(), transKey.getVii());
		}

		line = line.replace(defKey.getiSharp() + " ", transKey.getiSharp() + " ")
			.replace(defKey.getIii() + " ", transKey.getIii() + " ")
			.replace(defKey.getIvSharp() + " ", transKey.getIvSharp() + " ")
			.replace(defKey.getViSharp() + " ", transKey.getViSharp() + " ")
			.replace(defKey.getVii() + " ", transKey.getVii() + " ")
			.replace(" " + defKey.getiSharp(), " " + transKey.getiSharp())
			.replace(" " + defKey.getIii(), " " + transKey.getIii())
			.replace(" " + defKey.getIvSharp(), " " + transKey.getIvSharp())
			.replace(" " + defKey.getViSharp(), " " + transKey.getViSharp())
			.replace(" " + defKey.getVii(), " " + transKey.getVii())
			.replace(" " + defKey.getIV(), " " + transKey.getIV())
			.replace(" " + defKey.getVi(), " " + transKey.getVi())
			.replace(defKey.getIi() + " ", transKey.getIi() + " ")
			.replace(defKey.getIV() + " ", transKey.getIV() + " ")
			.replace(defKey.getVi() + " ", transKey.getVi() + " ")
			.replace(" " + defKey.getIi(), " " + transKey.getIi())
			.replace(" " + defKey.getI(), " " + transKey.getI())
			.replace(defKey.getI() + " ", transKey.getI() + " ")
			.replace(" " + defKey.getV(), " " + transKey.getV())
			.replace(defKey.getV() + " ", transKey.getV() + " ")
			.replace(defKey.getiSharp(), transKey.getiSharp())
			.replace(defKey.getIii(), transKey.getIii())
			.replace(defKey.getIvSharp(), transKey.getIvSharp())
			.replace(defKey.getViSharp(), transKey.getViSharp())
			.replace(defKey.getVii(), transKey.getVii())
			.replace(defKey.getIi(), transKey.getIi())
			.replace(defKey.getIV(), transKey.getIV())
			.replace(defKey.getVi(), transKey.getVi())
			.replace(defKey.getI(), transKey.getI())
			.replace(defKey.getV(), transKey.getV());
		
		return line;
	}

	private List<String> formatRawString(String transposableString) {
		return trimExcessiveEmptyLines(
					Arrays.stream(standardizeUnicodeCharacters(transposableString).split(SPLIT_LINE_DELIMITER))
							.map(l -> stripKeywordsFromLine(l))
							.filter(Objects::nonNull)
							.collect(Collectors.toList())
				); 
	}

	private String standardizeUnicodeCharacters(String transposableString) {
		return StringUtils.stripEnd(transposableString
				.replaceAll("\u266D", "b")
				.replaceAll("\u266F", "#")
				,null);
	}

	private String stripKeywordsFromLine(String line) {
		String l = StringUtils.stripEnd(line,null);
		String keywordSubString = l.replaceAll("[^a-zA-Z ]", "").trim();
		return STRIPPABLE_KEYWORDS.stream()
			.map(k -> k.equalsIgnoreCase(keywordSubString))
			.anyMatch(k -> k == true)? null: l;
	}

	private List<String> trimExcessiveEmptyLines(List<String> formattedLines) {
		formattedLines = new ArrayList<String>(Arrays.asList(String.join(APPEND_LINE_DELIMITER,formattedLines).replaceAll("[\n]{3,}", "\n\n").split(APPEND_LINE_DELIMITER)));
		while (formattedLines.get(0).trim().isEmpty()) {
			formattedLines.remove(0);
		}
		return formattedLines;
	}

	private KeySet getKeyFromMaster(String keyId) {
		return KeySetConfiguration.getKeySetMaster().stream().filter(k -> keyId.equalsIgnoreCase(k.getKeyId())).findAny().orElse(null);
	}
}