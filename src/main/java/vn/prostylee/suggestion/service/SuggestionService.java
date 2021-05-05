package vn.prostylee.suggestion.service;

import vn.prostylee.suggestion.dto.filter.SuggestionKeywordFilter;

import java.util.List;

public interface SuggestionService {

    List<String> getHintKeywords(SuggestionKeywordFilter suggestionKeywordFilter);

    List<String> getTopKeywords(SuggestionKeywordFilter suggestionKeywordFilter);

    List<String> getRecentKeywordsByMe(SuggestionKeywordFilter suggestionKeywordFilter);
}
