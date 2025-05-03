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
    public class v_Hotels_with_PhotosController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/v_Hotels_with_Photos
        public IQueryable<v_Hotels_with_Photos> Getv_Hotels_with_Photos()
        {
            return db.v_Hotels_with_Photos;
        }

        // GET: api/v_Hotels_with_Photos/5
        [ResponseType(typeof(v_Hotels_with_Photos))]
        public IHttpActionResult Getv_Hotels_with_Photos(int id)
        {
            v_Hotels_with_Photos v_Hotels_with_Photos = db.v_Hotels_with_Photos.Find(id);
            if (v_Hotels_with_Photos == null)
            {
                return NotFound();
            }

            return Ok(v_Hotels_with_Photos);
        }

        // PUT: api/v_Hotels_with_Photos/5
        [ResponseType(typeof(void))]
        public IHttpActionResult Putv_Hotels_with_Photos(int id, v_Hotels_with_Photos v_Hotels_with_Photos)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.v_Hotels_with_Photos.Find(id) is null)
            {
                return BadRequest();
            }

            v_Hotels_with_Photos Hotels_with_Photos = db.v_Hotels_with_Photos.Find(id);
            Hotels_with_Photos.hotel_id = v_Hotels_with_Photos.hotel_id;
            Hotels_with_Photos.name = v_Hotels_with_Photos.name;
            Hotels_with_Photos.description = v_Hotels_with_Photos.description;
            Hotels_with_Photos.address = v_Hotels_with_Photos.address;
            Hotels_with_Photos.city = v_Hotels_with_Photos.city;
            Hotels_with_Photos.country = v_Hotels_with_Photos.country;
            Hotels_with_Photos.star_rating = v_Hotels_with_Photos.star_rating;
            Hotels_with_Photos.email = v_Hotels_with_Photos.email;
            Hotels_with_Photos.phone = v_Hotels_with_Photos.phone;
            Hotels_with_Photos.hotel_photo = v_Hotels_with_Photos.hotel_photo;

            db.Entry(Hotels_with_Photos).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!v_Hotels_with_PhotosExists(id))
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

        // POST: api/v_Hotels_with_Photos
        [ResponseType(typeof(v_Hotels_with_Photos))]
        public IHttpActionResult Postv_Hotels_with_Photos(v_Hotels_with_Photos v_Hotels_with_Photos)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.v_Hotels_with_Photos.Add(v_Hotels_with_Photos);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (v_Hotels_with_PhotosExists(v_Hotels_with_Photos.hotel_id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = v_Hotels_with_Photos.hotel_id }, v_Hotels_with_Photos);
        }

        // DELETE: api/v_Hotels_with_Photos/5
        [ResponseType(typeof(v_Hotels_with_Photos))]
        public IHttpActionResult Deletev_Hotels_with_Photos(int id)
        {
            v_Hotels_with_Photos v_Hotels_with_Photos = db.v_Hotels_with_Photos.Find(id);
            if (v_Hotels_with_Photos == null)
            {
                return NotFound();
            }

            db.v_Hotels_with_Photos.Remove(v_Hotels_with_Photos);
            db.SaveChanges();

            return Ok(v_Hotels_with_Photos);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool v_Hotels_with_PhotosExists(int id)
        {
            return db.v_Hotels_with_Photos.Count(e => e.hotel_id == id) > 0;
        }
    }
}