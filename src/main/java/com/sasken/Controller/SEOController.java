package com.sasken.Controller;

import com.sasken.Model.SEOAnalysis;
import com.sasken.Service.SEOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/seo")
@Tag(name = "SEO Assistant", description = "SEO analysis and recommendations")
public class SEOController {

    @Autowired
    private SEOService seoService;

    @PostMapping("/analyze/{postId}")
    @Operation(summary = "Analyze a blog post for SEO")
    @ApiResponse(responseCode = "200", description = "Analysis completed successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<SEOAnalysis> analyzePost(
            @Parameter(description = "ID of the post to analyze")
            @PathVariable Long postId) {
        log.info("Received SEO analysis request for post {}", postId);
        SEOAnalysis analysis = seoService.analyzePost(postId);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/history/{postId}")
    @Operation(summary = "Get SEO analysis history for a post")
    @ApiResponse(responseCode = "200", description = "History retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<List<SEOAnalysis>> getAnalysisHistory(
            @Parameter(description = "ID of the post to get history for")
            @PathVariable Long postId) {
        log.info("Fetching SEO analysis history for post {}", postId);
        List<SEOAnalysis> history = seoService.getAnalysisHistory(postId);
        return ResponseEntity.ok(history);
    }
}