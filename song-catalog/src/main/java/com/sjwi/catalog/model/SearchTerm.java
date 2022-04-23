package com.sjwi.catalog.model;

public enum SearchTerm {
  KEY("key", "DEFAULT_KEY"),
  USER("username", "CREATED_BY")
  ;

  public static final String QUERY_ATTRIBUTE_STR = "<QUERY_ATTRIBUTE>";
  public final String searchTerm;
  public final String queryAttr;

  private SearchTerm(String searchTerm, String queryAttr) {
    this.searchTerm = searchTerm;
    this.queryAttr = queryAttr;
  }

  public static SearchTerm fromString(String s) {
    for (SearchTerm term: SearchTerm.values())
      if (term.searchTerm.equalsIgnoreCase(s))
        return term;
    return null;
  }
  
}
