using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using serverapi.Models;

namespace serverapi.Controllers
{
    public class ReviewsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Reviews
        public IQueryable<HotelReview> GetReviews()
        {
            return db.Reviews.Select(r => new HotelReview {
                UserId = r.user_id,
                HotelId = r.hotel_id,
                Rating = r.rating,
                ReviewText = r.review_text,
                ReviewDate = r.review_date,
                HotelResponse = r.hotel_response
            });
        }

        // GET: api/Reviews/5
        [ResponseType(typeof(Reviews))]
        public IHttpActionResult GetReviews(int id)
        {
            Reviews r = db.Reviews.Find(id);
            if (r == null)
            {
                return NotFound();
            }

            HotelReview review = new HotelReview();
            review.UserId = r.user_id;
            review.HotelId = r.hotel_id;
            review.Rating = r.rating;
            review.ReviewText = r.review_text;
            review.ReviewDate = r.review_date;
            review.HotelResponse = r.hotel_response;

            return Ok(review);
        }

        // PUT: api/Reviews/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutReviews(int id, Reviews reviews)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Reviews.Find(id) is null)
            {
                return BadRequest();
            }

            Reviews db_reviews = db.Reviews.Find(id);
            db_reviews.rating = reviews.rating;
            db_reviews.review_text = reviews.review_text;
            db_reviews.review_date = reviews.review_date;
            db_reviews.hotel_response = reviews.hotel_response;

            db.Entry(db_reviews).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ReviewsExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/Reviews
        [ResponseType(typeof(Reviews))]
        public IHttpActionResult PostReviews(Reviews reviews)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Reviews.Add(reviews);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = reviews.review_id }, reviews);
        }

        // DELETE: api/Reviews/5
        [ResponseType(typeof(Reviews))]
        public IHttpActionResult DeleteReviews(int id)
        {
            Reviews reviews = db.Reviews.Find(id);
            if (reviews == null)
            {
                return NotFound();
            }

            db.Reviews.Remove(reviews);
            db.SaveChanges();

            return Ok(reviews);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ReviewsExists(int id)
        {
            return db.Reviews.Count(e => e.review_id == id) > 0;
        }
    }
    public class HotelReview
    {
        public int UserId { get; set; }
        public int HotelId { get; set; }
        public int? Rating { get; set; }
        public string ReviewText { get; set; }
        public DateTime? ReviewDate { get; set; }
        public string HotelResponse { get; set; }
    }
}