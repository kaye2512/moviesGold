package dev.rkaye.movies.Service;

import dev.rkaye.movies.Model.Movie;
import dev.rkaye.movies.Model.Review;
import dev.rkaye.movies.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    // mongotemplate we are using to update movie with the new review we put in the repository
    @Autowired
    private MongoTemplate mongoTemplate;
    public Review createReview(String reviewBody, String imdbId){
        Review review = reviewRepository.insert(new Review(reviewBody));

    // perform update o moving class
        // because reviewid are empty
        // perform matching on with moving we are updating imdbId matching the id
        // we are receive from the user
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();
        return review;
    }
}
