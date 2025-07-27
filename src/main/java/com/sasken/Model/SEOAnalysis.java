package com.sasken.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SEOAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    @Column(length = 5000)
    private String analysis;

    @Column(name = "heading_structure_score", nullable = false, columnDefinition = "int default 0")
    private int headingStructureScore = 0;

    private int keywordDensity;
    private int readabilityScore;
    private boolean hasMetaDescription;
    private boolean hasImages;
    private boolean hasExternalLinks;
    private boolean hasInternalLinks;
    private int wordCount;
    private int headingCount;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @PrePersist
    protected void onCreate() {
        if (this.analyzedAt == null) {
            this.analyzedAt = LocalDateTime.now();
        }
    }
}
