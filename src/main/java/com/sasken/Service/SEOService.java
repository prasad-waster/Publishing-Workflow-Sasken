package com.sasken.Service;

import com.sasken.Model.BlogPost;
import com.sasken.Model.SEOAnalysis;
import com.sasken.Repository.BlogPostRepository;
import com.sasken.Repository.SEOAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SEOService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private SEOAnalysisRepository seoAnalysisRepository;

    @Transactional
    public SEOAnalysis analyzePost(Long postId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", postId);
                    return new IllegalArgumentException("Post not found with ID: " + postId);
                });

        String content = post.getContent() != null ? post.getContent() : "";
        String title = post.getTitle() != null ? post.getTitle() : "";

        SEOAnalysis analysis = SEOAnalysis.builder()
                .postId(postId)
                .wordCount(countWords(content))
                .keywordDensity(calculateKeywordDensity(content, title))
                .readabilityScore(calculateReadabilityScore(content))
                .hasMetaDescription(checkMetaDescription(content))
                .hasImages(hasImages(content))
                .hasExternalLinks(hasExternalLinks(content))
                .hasInternalLinks(hasInternalLinks(content))
                .headingCount(countHeadings(content))
                .headingStructureScore(calculateHeadingStructureScore(content))
                .analysis(generateAnalysisReport(title, content))
                .build();

        log.info("Generated SEO analysis for post {}: {}", postId, analysis);
        return seoAnalysisRepository.save(analysis);
    }

    private boolean checkMetaDescription(String content) {
        return content.length() > 50 && content.length() < 160;
    }

    private boolean hasImages(String content) {
        if (content == null) return false;
        String lowerContent = content.toLowerCase();
        return lowerContent.contains("<img") || lowerContent.contains("![");
    }

    private boolean hasExternalLinks(String content) {
        if (content == null) return false;
        return content.matches("(?s).*https?://\\S+.*");
    }

    private boolean hasInternalLinks(String content) {
        if (content == null) return false;
        return content.matches("(?s).*/\\S+.*") && !content.matches("(?s).*https?://\\S+.*");
    }

    private int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return text.split("\\s+").length;
    }

    private int calculateKeywordDensity(String content, String title) {
        if (title == null || title.isEmpty() || content == null) {
            return 0;
        }

        String[] titleWords = title.split("\\s+");
        String mainKeyword = titleWords.length > 0 ? titleWords[0] : "";

        if (mainKeyword.isEmpty()) {
            return 0;
        }

        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(mainKeyword.toLowerCase()) + "\\b");
        java.util.regex.Matcher matcher = pattern.matcher(content.toLowerCase());
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        int wordCount = countWords(content);
        return wordCount > 0 ? (count * 100) / wordCount : 0;
    }

    private int calculateReadabilityScore(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        String[] sentences = content.split("[.!?]+");
        String[] words = content.split("\\s+");
        String[] paragraphs = content.split("\n\n");

        if (sentences.length == 0 || words.length == 0) {
            return 0;
        }

        double avgSentenceLength = (double) words.length / sentences.length;
        double avgWordLength = content.replaceAll("\\s+", "").length() / (double) words.length;

        int score = 100;
        if (avgSentenceLength > 20) score -= 10;
        if (avgSentenceLength > 30) score -= 15;
        if (avgWordLength > 5) score -= 5;
        if (paragraphs.length < 3) score -= 5;

        return Math.max(0, score);
    }

    private int countHeadings(String content) {
        if (content == null) {
            return 0;
        }
        int count = 0;
        count += countOccurrences(content, "<h1");
        count += countOccurrences(content, "<h2");
        count += countOccurrences(content, "<h3");
        count += countOccurrences(content, "# ");
        count += countOccurrences(content, "## ");
        count += countOccurrences(content, "### ");
        return count;
    }

    private int calculateHeadingStructureScore(String content) {
        if (content == null) {
            return 0;
        }

        int h1Count = countOccurrences(content, "<h1") + countOccurrences(content, "# ");
        int h2Count = countOccurrences(content, "<h2") + countOccurrences(content, "## ");
        int h3Count = countOccurrences(content, "<h3") + countOccurrences(content, "### ");

        int score = 0;
        if (h1Count == 1) score += 30;
        if (h2Count >= 2) score += 30;
        if (h3Count >= 2) score += 20;
        if (h1Count + h2Count + h3Count >= 5) score += 20;

        return Math.min(100, score);
    }

    private int countOccurrences(String haystack, String needle) {
        return haystack.toLowerCase().split(Pattern.quote(needle.toLowerCase()), -1).length - 1;
    }

    private String generateAnalysisReport(String title, String content) {
        StringBuilder report = new StringBuilder();
        report.append("SEO Analysis Report for: ").append(title).append("\n\n");
        report.append("Word Count: ").append(countWords(content)).append("\n");
        report.append("Keyword Density: ").append(calculateKeywordDensity(content, title)).append("%\n");
        report.append("Readability Score: ").append(calculateReadabilityScore(content)).append("/100\n");
        report.append("Heading Structure Score: ").append(calculateHeadingStructureScore(content)).append("/100\n");
        report.append("Has Meta Description: ").append(checkMetaDescription(content) ? "Yes" : "No").append("\n");
        report.append("Has Images: ").append(hasImages(content) ? "Yes" : "No").append("\n");
        report.append("Has External Links: ").append(hasExternalLinks(content) ? "Yes" : "No").append("\n");
        report.append("Has Internal Links: ").append(hasInternalLinks(content) ? "Yes" : "No").append("\n");
        report.append("Heading Count: ").append(countHeadings(content)).append("\n\n");

        report.append("Recommendations:\n");
        if (countWords(content) < 300) {
            report.append("- Consider expanding your content to at least 300 words for better SEO\n");
        }
        int keywordDensity = calculateKeywordDensity(content, title);
        if (keywordDensity < 1 || keywordDensity > 3) {
            report.append("- Adjust keyword density to be between 1-3%\n");
        }
        if (calculateReadabilityScore(content) < 70) {
            report.append("- Improve readability by shortening sentences and using simpler words\n");
        }
        if (!checkMetaDescription(content)) {
            report.append("- Add a meta description (50-160 characters)\n");
        }
        if (!hasImages(content)) {
            report.append("- Add relevant images to improve engagement\n");
        }
        if (countHeadings(content) < 2) {
            report.append("- Use more headings to structure your content\n");
        }
        if (calculateHeadingStructureScore(content) < 70) {
            report.append("- Improve heading structure (1 H1, multiple H2/H3 headings)\n");
        }
        if (!hasExternalLinks(content)) {
            report.append("- Consider adding authoritative external links\n");
        }
        if (!hasInternalLinks(content)) {
            report.append("- Add internal links to other relevant posts\n");
        }

        return report.toString();
    }

    @Transactional(readOnly = true)
    public List<SEOAnalysis> getAnalysisHistory(Long postId) {
        List<SEOAnalysis> history = seoAnalysisRepository.findByPostId(postId);
        log.info("Retrieved {} historical analyses for post {}", history.size(), postId);
        return history;
    }
}