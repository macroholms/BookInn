using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Diagnostics.Metrics;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Helpers;
using System.Web.Http;
using System.Web.Http.Description;
using System.Xml.Linq;
using serverapi.Models;

namespace serverapi.Controllers
{
    public class HotelsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Hotels
        public IQueryable<HotelDto> GetHotels()
        {
            return db.Hotels.Select(h => new HotelDto
            {
                HotelId = h.hotel_id,
                Name = h.name,
                Description = h.description,
                Address = h.address,
                City = h.city,
                Country = h.country,
                StarRating = h.star_rating,
                Email = h.email,
                Phone = h.phone,
                PreviewPhoto = h.preview_photo,
                CreatedAt = h.created_at
            });
        }

        // GET: api/Hotels/5
        [ResponseType(typeof(Hotels))]
        public IHttpActionResult GetHotels(int id)
        {
            Hotels h = db.Hotels.Find(id);
            if (h == null)
            {
                return NotFound();
            }

            HotelDto dto = new HotelDto();
            dto.HotelId = h.hotel_id;
            dto.Name = h.name;
            dto.Description = h.description;
            dto.Address = h.address;
            dto.City = h.city;
            dto.Country = h.country;
            dto.StarRating = h.star_rating;
            dto.Email = h.email;
            dto.Phone = h.phone;
            dto.PreviewPhoto = h.preview_photo;
            dto.CreatedAt = h.created_at;

            return Ok(dto);
        }

        // PUT: api/Hotels/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutHotels(int id, Hotels hotels)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Hotels.Find(id) is null)
            {
                return BadRequest();
            }

            Hotels db_hotels = db.Hotels.Find(id);

            db_hotels.name = hotels.name;
            db_hotels.description = hotels.description;
            db_hotels.address = hotels.address;
            db_hotels.city = hotels.city;
            db_hotels.country = hotels.country;
            db_hotels.star_rating = hotels.star_rating;
            db_hotels.email = hotels.email;
            db_hotels.phone = hotels.phone;
            db_hotels.preview_photo = hotels.preview_photo;
            db_hotels.created_at = hotels.created_at;

            db.Entry(db_hotels).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!HotelsExists(id))
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

        // POST: api/Hotels
        [ResponseType(typeof(Hotels))]
        public IHttpActionResult PostHotels(Hotels hotels)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Hotels.Add(hotels);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = hotels.hotel_id }, hotels);
        }

        // DELETE: api/Hotels/5
        [ResponseType(typeof(Hotels))]
        public IHttpActionResult DeleteHotels(int id)
        {
            Hotels hotels = db.Hotels.Find(id);
            if (hotels == null)
            {
                return NotFound();
            }

            db.Hotels.Remove(hotels);
            db.SaveChanges();

            return Ok(hotels);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool HotelsExists(int id)
        {
            return db.Hotels.Count(e => e.hotel_id == id) > 0;
        }
    }

    public class HotelDto
    {
        public int HotelId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string Address { get; set; }
        public string City { get; set; }
        public string Country { get; set; }
        public double? StarRating { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }
        public int? PreviewPhoto { get; set; }
        public DateTime? CreatedAt { get; set; }
    }
}