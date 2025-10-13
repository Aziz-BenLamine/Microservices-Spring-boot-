package com.azizdev.jobApp.review.impl;

import com.azizdev.jobApp.company.Company;
import com.azizdev.jobApp.company.CompanyService;
import com.azizdev.jobApp.review.Review;
import com.azizdev.jobApp.review.ReviewRepository;
import com.azizdev.jobApp.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final CompanyService companyService;
    private ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findBycompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        Company company = companyService.getCompanyById(companyId);
        if (company != null) {
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReview(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findBycompanyId(companyId);
        reviews.stream().filter(review ->review.getId().equals(reviewId)).findFirst().orElse(null);
        return null;
    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review review) {
        if(companyService.getCompanyById(companyId) != null) {
            review.setCompany(companyService.getCompanyById(companyId));
            review.setId(reviewId);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        if(companyService.getCompanyById(companyId) != null && reviewRepository.existsById(reviewId)) {
            Review review = reviewRepository.findById(reviewId).orElse(null);
            Company company = review.getCompany();
            company.getReviews().remove(review);
            review.setCompany(null);
            companyService.updateCompany(companyId, company);
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }


}
