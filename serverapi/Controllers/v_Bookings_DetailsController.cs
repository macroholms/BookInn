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
    public class v_Bookings_DetailsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/v_Bookings_Details
        public IQueryable<v_Bookings_Details> Getv_Bookings_Details()
        {
            return db.v_Bookings_Details;
        }

        public IQueryable<v_Bookings_Details> Getv_Bookings_With_User(int UserId)
        {
            return db.v_Bookings_Details
                .Where(r => r.user_id == UserId);
        }

        // GET: api/v_Bookings_Details/5
        [ResponseType(typeof(v_Bookings_Details))]
        public IHttpActionResult Getv_Bookings_Details(int id)
        {
            v_Bookings_Details v_Bookings_Details = db.v_Bookings_Details.Find(id);
            if (v_Bookings_Details == null)
            {
                return NotFound();
            }

            return Ok(v_Bookings_Details);
        }

        // PUT: api/v_Bookings_Details/5
        [ResponseType(typeof(void))]
        public IHttpActionResult Putv_Bookings_Details(int id, v_Bookings_Details v_Bookings_Details)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.v_Bookings_Details.Find(id) is null)
            {
                return BadRequest();
            }

            v_Bookings_Details Bookings_Details = db.v_Bookings_Details.Find(id);
            Bookings_Details.booking_id = v_Bookings_Details.booking_id;
            Bookings_Details.user_fio = v_Bookings_Details.user_fio;
            Bookings_Details.user_id = v_Bookings_Details.user_id;
            Bookings_Details.room_id = v_Bookings_Details.room_id;
            Bookings_Details.room_description = v_Bookings_Details.room_description;
            Bookings_Details.check_in_date = v_Bookings_Details.check_in_date;
            Bookings_Details.check_out_date = v_Bookings_Details.check_out_date;
            Bookings_Details.guests_number = v_Bookings_Details.guests_number;
            Bookings_Details.total_price = v_Bookings_Details.total_price;
            Bookings_Details.booking_status = Bookings_Details.booking_status;
            Bookings_Details.payment_id = v_Bookings_Details.payment_id;
            Bookings_Details.payment_amount = v_Bookings_Details.payment_amount;
            Bookings_Details.payment_status = Bookings_Details.payment_status;

            db.Entry(Bookings_Details).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!v_Bookings_DetailsExists(id))
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

        // POST: api/v_Bookings_Details
        [ResponseType(typeof(v_Bookings_Details))]
        public IHttpActionResult Postv_Bookings_Details(v_Bookings_Details v_Bookings_Details)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.v_Bookings_Details.Add(v_Bookings_Details);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (v_Bookings_DetailsExists(v_Bookings_Details.booking_id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = v_Bookings_Details.booking_id }, v_Bookings_Details);
        }

        // DELETE: api/v_Bookings_Details/5
        [ResponseType(typeof(v_Bookings_Details))]
        public IHttpActionResult Deletev_Bookings_Details(int id)
        {
            v_Bookings_Details v_Bookings_Details = db.v_Bookings_Details.Find(id);
            if (v_Bookings_Details == null)
            {
                return NotFound();
            }

            db.v_Bookings_Details.Remove(v_Bookings_Details);
            db.SaveChanges();

            return Ok(v_Bookings_Details);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool v_Bookings_DetailsExists(int id)
        {
            return db.v_Bookings_Details.Count(e => e.booking_id == id) > 0;
        }
    }
}