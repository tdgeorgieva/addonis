package com.addonis.controllers.mvc;


import com.addonis.controllers.AuthenticationHelper;
import com.addonis.modelMappers.ReviewModelMapper;
import com.addonis.services.review.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reviews")
public class ReviewMvcController {

    private final ReviewService reviewService;
    private final ReviewModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;


    public ReviewMvcController(ReviewService reviewService, ReviewModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }



}
