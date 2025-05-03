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
    public class Payment_statusController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Payment_status
        public IQueryable<PStatusDto> GetPayment_status()
        {
            return db.Payment_status.Select(p => new PStatusDto
            {
                payment_status_id = p.payment_status_id,
                status_text = p.status_text
            });
        }

        // GET: api/Payment_status/5
        [ResponseType(typeof(Payment_status))]
        public IHttpActionResult GetPayment_status(int id)
        {
            Payment_status p = db.Payment_status.Find(id);
            if (p == null)
            {
                return NotFound();
            }
            
            PStatusDto payment_status = new PStatusDto();
            payment_status.payment_status_id = p.payment_status_id;
            payment_status.status_text = p.status_text;

            return Ok(payment_status);
        }

        // PUT: api/Payment_status/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutPayment_status(int id, Payment_status payment_status)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Payment_status.Find(id) is null)
            {
                return BadRequest();
            }

            Payment_status db_payment_status = db.Payment_status.Find(id);
            db_payment_status.status_text = payment_status.status_text;

            db.Entry(db_payment_status).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!Payment_statusExists(id))
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

        // POST: api/Payment_status
        [ResponseType(typeof(Payment_status))]
        public IHttpActionResult PostPayment_status(Payment_status payment_status)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Payment_status.Add(payment_status);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = payment_status.payment_status_id }, payment_status);
        }

        // DELETE: api/Payment_status/5
        [ResponseType(typeof(Payment_status))]
        public IHttpActionResult DeletePayment_status(int id)
        {
            Payment_status payment_status = db.Payment_status.Find(id);
            if (payment_status == null)
            {
                return NotFound();
            }

            db.Payment_status.Remove(payment_status);
            db.SaveChanges();

            return Ok(payment_status);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool Payment_statusExists(int id)
        {
            return db.Payment_status.Count(e => e.payment_status_id == id) > 0;
        }
    }

    public class PStatusDto
    {
        public int payment_status_id { get; set; }
        public string status_text { get; set; }
    }
}