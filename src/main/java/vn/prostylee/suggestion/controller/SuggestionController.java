package vn.prostylee.suggestion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.suggestion.dto.filter.SuggestionKeywordFilter;
import vn.prostylee.suggestion.service.SuggestionService;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiVersion.API_V1 + "/suggestions")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @GetMapping("/keywords/hint")
    public SimpleResponse getKeywordsOfProducts(SuggestionKeywordFilter suggestionKeywordFilter) {
        return SimpleResponse.builder()
                .data(suggestionService.getHintKeywords(suggestionKeywordFilter))
                .build();
    }

    @GetMapping("/keywords/top")
    public SimpleResponse getTopKeywordsOfProducts(SuggestionKeywordFilter suggestionKeywordFilter) {
        return SimpleResponse.builder()
                .data(suggestionService.getTopKeywords(suggestionKeywordFilter))
                .build();
    }

    @GetMapping("/keywords/me/recent")
    public SimpleResponse getRecentProductKeywordsByMe(SuggestionKeywordFilter suggestionKeywordFilter) {
        return SimpleResponse.builder()
                .data(suggestionService.getRecentKeywordsByMe(suggestionKeywordFilter))
                .build();
    }
}
