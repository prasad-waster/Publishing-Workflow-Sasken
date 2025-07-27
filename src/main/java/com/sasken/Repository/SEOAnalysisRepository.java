package com.sasken.Repository;

import com.sasken.Model.SEOAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SEOAnalysisRepository extends JpaRepository<SEOAnalysis, Long> {
    List<SEOAnalysis> findByPostId(Long postId);
}
