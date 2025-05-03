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
    public class Booking_statusController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Booking_status
        public IQueryable<BStatusDto> GetBooking_status()
        {
            return db.Booking_status.Select(b => new BStatusDto
            {
                booking_status_id = b.booking_status_id,
                status_text = b.status_text
            });
        }

        // GET: api/Booking_status/5
        [ResponseType(typeof(Booking_status))]
        public IHttpActionResult GetBooking_status(int id)
        {
            Booking_status b = db.Booking_status.Find(id);
            if (b == null)
            {
                return NotFound();
            }

            BStatusDto booking_status = new BStatusDto();
            booking_status.booking_status_id = b.booking_status_id;
            booking_status.status_text = b.status_text;

            return Ok(booking_status);
        }

        // PUT: api/Booking_status/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutBooking_status(int id, Booking_status booking_status)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Booking_status.Find(id) is null)
            {
                return BadRequest();
            }

            Booking_status bd_booking_status = db.Booking_status.Find(id);

            bd_booking_status.status_text = booking_status.status_text;

            db.Entry(bd_booking_status).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }

            catch (DbUpdateConcurrencyException)
            {
                if (!Booking_statusExists(id))
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

        // POST: api/Booking_status
        [ResponseType(typeof(Booking_status))]
        public IHttpActionResult PostBooking_status(Booking_status booking_status)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Booking_status.Add(booking_status);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = booking_status.booking_status_id }, booking_status);
        }

        // DELETE: api/Booking_status/5
        [ResponseType(typeof(Booking_status))]
        public IHttpActionResult DeleteBooking_status(int id)
        {
            Booking_status booking_status = db.Booking_status.Find(id);
            if (booking_status == null)
            {
                return NotFound();
            }

            db.Booking_status.Remove(booking_status);
            db.SaveChanges();

            return Ok(booking_status);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool Booking_statusExists(int id)
        {
            return db.Booking_status.Count(e => e.booking_status_id == id) > 0;
        }
    }

    public class BStatusDto
    {
        public int booking_status_id { get; set; }
        public string status_text { get; set; }
    }
}