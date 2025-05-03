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
    public class v_Active_BookingsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/v_Active_Bookings
        public IQueryable<v_Active_Bookings> Getv_Active_Bookings()
        {
            return db.v_Active_Bookings;
        }

        // GET: api/v_Active_Bookings/5
        [ResponseType(typeof(v_Active_Bookings))]
        public IHttpActionResult Getv_Active_Bookings(int id)
        {
            v_Active_Bookings v_Active_Bookings = db.v_Active_Bookings.Find(id);
            if (v_Active_Bookings == null)
            {
                return NotFound();
            }

            return Ok(v_Active_Bookings);
        }

        // PUT: api/v_Active_Bookings/5
        [ResponseType(typeof(void))]
        public IHttpActionResult Putv_Active_Bookings(int id, v_Active_Bookings v_Active_Bookings)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.v_Active_Bookings.Find(id) is null)
            {
                return BadRequest();
            }
            
            v_Active_Bookings Active_Bookings = db.v_Active_Bookings.Find(id);
            Active_Bookings.booking_id = v_Active_Bookings.booking_id;
            Active_Bookings.user_id = v_Active_Bookings.user_id;
            Active_Bookings.user_fio = v_Active_Bookings.user_fio;
            Active_Bookings.room_id = v_Active_Bookings.room_id;
            Active_Bookings.room_description = v_Active_Bookings.room_description;
            Active_Bookings.check_in_date = v_Active_Bookings.check_in_date;
            Active_Bookings.check_out_date = v_Active_Bookings.check_out_date;
            Active_Bookings.guests_number = v_Active_Bookings.guests_number;
            Active_Bookings.total_price = v_Active_Bookings.total_price;

            db.Entry(Active_Bookings).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!v_Active_BookingsExists(id))
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

        // POST: api/v_Active_Bookings
        [ResponseType(typeof(v_Active_Bookings))]
        public IHttpActionResult Postv_Active_Bookings(v_Active_Bookings v_Active_Bookings)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.v_Active_Bookings.Add(v_Active_Bookings);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (v_Active_BookingsExists(v_Active_Bookings.booking_id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = v_Active_Bookings.booking_id }, v_Active_Bookings);
        }

        // DELETE: api/v_Active_Bookings/5
        [ResponseType(typeof(v_Active_Bookings))]
        public IHttpActionResult Deletev_Active_Bookings(int id)
        {
            v_Active_Bookings v_Active_Bookings = db.v_Active_Bookings.Find(id);
            if (v_Active_Bookings == null)
            {
                return NotFound();
            }

            db.v_Active_Bookings.Remove(v_Active_Bookings);
            db.SaveChanges();

            return Ok(v_Active_Bookings);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool v_Active_BookingsExists(int id)
        {
            return db.v_Active_Bookings.Count(e => e.booking_id == id) > 0;
        }
    }
}