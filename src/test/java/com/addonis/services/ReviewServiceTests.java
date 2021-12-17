package com.addonis.services;

import com.addonis.models.Review;
import com.addonis.repositories.review.ReviewRepository;
import com.addonis.services.review.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.addonis.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    ReviewRepository mockRepository;

    @InjectMocks
    ReviewServiceImpl service;

    @Test
    public void getAll_Should_Return_AllReviews() {
        var review = createMockReview();

        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(review));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Review_WhenMatchExists() {
        var review = createMockReview();

        Mockito.when(mockRepository.getById(review.getId()))
                .thenReturn(review);

        Assertions.assertDoesNotThrow(() -> service.getById(review.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(review.getId());
    }

    @Test
    public void create_ShouldCallRepository_When_ReviewDoesNotExists() {
        var review = createMockReview();

        Assertions.assertDoesNotThrow(() -> service.create(review));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Review.class));
    }

    @Test
    public void delete_Should_Delete_Review() {
        var review = createMockReview();

        Assertions.assertDoesNotThrow(() -> service.delete(review.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(review.getId());
    }

    @Test
    public void getByUser_Should_Return_Reviews_WhenMatchExists() {
        var review = createMockReview();
        var user = createMockUser();

        Mockito.when(mockRepository.getByUser(user.getId()))
                .thenReturn(List.of(review));

        Assertions.assertDoesNotThrow(() -> service.getByUser(user.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByUser(user.getId());
    }

    @Test
    public void getByAddon_Should_Return_Reviews_WhenMatchExists() {
        var review = createMockReview();
        var addon = createMockAddon();

        Mockito.when(mockRepository.getByAddon(addon.getId()))
                .thenReturn(List.of(review));

        Assertions.assertDoesNotThrow(() -> service.getByAddon(addon.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByAddon(addon.getId());
    }

}
