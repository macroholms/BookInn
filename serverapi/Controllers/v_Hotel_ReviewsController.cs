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
    public class v_Hotel_ReviewsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/v_Hotel_Reviews
        public IQueryable<v_Hotel_Reviews> Getv_Hotel_Reviews()
        {
            return db.v_Hotel_Reviews;
        }

        public IQueryable<v_Hotel_Reviews> Getv_All_Reviews(int hotel_id)
        {
            return db.v_Hotel_Reviews.
                Where(r => r.hotel_id == hotel_id);
        }

        // GET: api/v_Hotel_Reviews/5
        [ResponseType(typeof(v_Hotel_Reviews))]
        public IHttpActionResult Getv_Hotel_Reviews(int id)
        {
            v_Hotel_Reviews v_Hotel_Reviews = db.v_Hotel_Reviews.Find(id);
            if (v_Hotel_Reviews == null)
            {
                return NotFound();
            }

            return Ok(v_Hotel_Reviews);
        }

        // PUT: api/v_Hotel_Reviews/5
        [ResponseType(typeof(void))]
        public IHttpActionResult Putv_Hotel_Reviews(int id, v_Hotel_Reviews v_Hotel_Reviews)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.v_Hotel_Reviews.Find(id) is null)
            {
                return BadRequest();
            }

            v_Hotel_Reviews Hotel_Reviews = db.v_Hotel_Reviews.Find(id);
            Hotel_Reviews.hotel_id = v_Hotel_Reviews.hotel_id;
            Hotel_Reviews.hotel_name = v_Hotel_Reviews.hotel_name;
            Hotel_Reviews.user_id = v_Hotel_Reviews.user_id;
            Hotel_Reviews.user_fio = v_Hotel_Reviews.user_fio;
            Hotel_Reviews.review_id = v_Hotel_Reviews.review_id;
            Hotel_Reviews.rating = v_Hotel_Reviews.rating;
            Hotel_Reviews.review_text = v_Hotel_Reviews.review_text;
            Hotel_Reviews.hotel_response = v_Hotel_Reviews.hotel_response;
            Hotel_Reviews.review_date = v_Hotel_Reviews.review_date;

            db.Entry(Hotel_Reviews).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!v_Hotel_ReviewsExists(id))
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

        // POST: api/v_Hotel_Reviews
        [ResponseType(typeof(v_Hotel_Reviews))]
        public IHttpActionResult Postv_Hotel_Reviews(v_Hotel_Reviews v_Hotel_Reviews)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.v_Hotel_Reviews.Add(v_Hotel_Reviews);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (v_Hotel_ReviewsExists(v_Hotel_Reviews.hotel_id))
                {
                    return Conflict();
                }
                else
                {
                        throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = v_Hotel_Reviews.hotel_id }, v_Hotel_Reviews);
        }

        // DELETE: api/v_Hotel_Reviews/5
        [ResponseType(typeof(v_Hotel_Reviews))]
        public IHttpActionResult Deletev_Hotel_Reviews(int id)
        {
            v_Hotel_Reviews v_Hotel_Reviews = db.v_Hotel_Reviews.Find(id);
            if (v_Hotel_Reviews == null)
            {
                return NotFound();
            }

            db.v_Hotel_Reviews.Remove(v_Hotel_Reviews);
            db.SaveChanges();

            return Ok(v_Hotel_Reviews);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool v_Hotel_ReviewsExists(int id)
        {
            return db.v_Hotel_Reviews.Count(e => e.hotel_id == id) > 0;
        }
    }
}