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
    public class v_Rooms_DetailsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/v_Rooms_Details
        public IQueryable<v_Rooms_Details> Getv_Rooms_Details()
        {
            return db.v_Rooms_Details;
        }

        public IQueryable<v_Rooms_Details> Getv_Rooms_Details_W_Hotels(int hotelId)
        {
            return db.v_Rooms_Details
                .Where(r => r.hotel_id == hotelId);
        }

        public IQueryable<v_Rooms_Details> Getv_Rooms_Details_W_Room(int roomId)
        {
            return db.v_Rooms_Details
                .Where(r => r.room_id == roomId);
        }

        // GET: api/v_Rooms_Details/5
        [ResponseType(typeof(v_Rooms_Details))]
        public IHttpActionResult Getv_Rooms_Details(int id)
        {
            v_Rooms_Details v_Rooms_Details = db.v_Rooms_Details.Find(id);
            if (v_Rooms_Details == null)
            {
                return NotFound();
            }

            return Ok(v_Rooms_Details);
        }

        // PUT: api/v_Rooms_Details/5
        [ResponseType(typeof(void))]
        public IHttpActionResult Putv_Rooms_Details(int id, v_Rooms_Details v_Rooms_Details)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            
            if (db.v_Rooms_Details.Find(id) is null)
            {
                return BadRequest();
            }

            v_Rooms_Details Rooms_Details = db.v_Rooms_Details.Find(id);
            Rooms_Details.description = v_Rooms_Details.description;
            Rooms_Details.hotel_id = v_Rooms_Details.hotel_id;
            Rooms_Details.room_type_name = v_Rooms_Details.room_type_name;
            Rooms_Details.capacity = v_Rooms_Details.capacity;
            Rooms_Details.quantity_available = v_Rooms_Details.quantity_available;
            Rooms_Details.price_per_night = v_Rooms_Details.price_per_night;
            Rooms_Details.size = v_Rooms_Details.size;
            Rooms_Details.room_photo = v_Rooms_Details.room_photo;

            db.Entry(Rooms_Details).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!v_Rooms_DetailsExists(id))
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

        // POST: api/v_Rooms_Details
        [ResponseType(typeof(v_Rooms_Details))]
        public IHttpActionResult Postv_Rooms_Details(v_Rooms_Details v_Rooms_Details)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.v_Rooms_Details.Add(v_Rooms_Details);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (v_Rooms_DetailsExists(v_Rooms_Details.room_id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = v_Rooms_Details.room_id }, v_Rooms_Details);
        }

        // DELETE: api/v_Rooms_Details/5
        [ResponseType(typeof(v_Rooms_Details))]
        public IHttpActionResult Deletev_Rooms_Details(int id)
        {
            v_Rooms_Details v_Rooms_Details = db.v_Rooms_Details.Find(id);
            if (v_Rooms_Details == null)
            {
                return NotFound();
            }

            db.v_Rooms_Details.Remove(v_Rooms_Details);
            db.SaveChanges();

            return Ok(v_Rooms_Details);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool v_Rooms_DetailsExists(int id)
        {
            return db.v_Rooms_Details.Count(e => e.room_id == id) > 0;
        }
    }
}