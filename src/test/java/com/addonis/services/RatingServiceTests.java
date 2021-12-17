package com.addonis.services;

import com.addonis.repositories.addon.RatingRepository;
import com.addonis.services.rating.RatingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.addonis.Helpers.*;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTests {

    @Mock
    RatingRepository mockRepository;

    @InjectMocks
    RatingServiceImpl service;

    @Test
    public void create_ShouldCallRepository_WhenNull() {
        var rating = createMockRating();

        doThrow(NullPointerException.class)
                .when(mockRepository)
                .create(rating);

        Assertions.assertThrows(NullPointerException.class, () -> service.create(rating));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(rating);
    }

    @Test
    public void create_ShouldCallRepository() {
        var rating = createMockRating();
        var addon = createMockAddon();
        var user = createMockUser();

        Mockito.when(mockRepository.ratingExistsForAddonEntity(user.getId(),addon.getId()))
                .thenReturn(rating);

        Assertions.assertDoesNotThrow(() -> service.create(rating));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(rating);
    }

    @Test
    public void update_Should_Update_Rating() {
        var rating = createMockRating();
        rating.setRating(2);

        Assertions.assertDoesNotThrow(() -> service.update(rating));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(rating);
    }

    @Test
    public void calculateRating_Should_CalculateRating() {
        var rating = createMockRating();

        Mockito.when(mockRepository.calculateRating(rating.getAddon().getId()))
                .thenReturn(1.0);

        Assertions.assertDoesNotThrow(() -> service.calculateRating(rating.getAddon().getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .calculateRating(rating.getAddon().getId());
    }
}

