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
    public class PaymentsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Payments
        public IQueryable<PaymentDto> GetPayments()
        {
            return db.Payments.Select(p => new PaymentDto
            {
                PaymentId = p.payment_id,
                BookingId = p.booking_id,
                Amount = p.amount,
                PaymentMethod = p.payment_method,
                PaymentStatus = p.payment_status,
                PaymentDate = p.payment_date
            });
        }

        // GET: api/Payments/5
        [ResponseType(typeof(Payments))]
        public IHttpActionResult GetPayments(int id)
        {
            Payments p = db.Payments.Find(id);
            if (p == null)
            {
                return NotFound();
            }

            PaymentDto payments = new PaymentDto();

            payments.PaymentId = p.payment_id;
            payments.BookingId = p.booking_id;
            payments.Amount = p.amount;
            payments.PaymentMethod = p.payment_method;
            payments.PaymentStatus = p.payment_status;
            payments.PaymentDate = p.payment_date;

            return Ok(payments);
        }

        // PUT: api/Payments/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutPayments(int id, Payments payments)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Payments.Find(id) is null)
            {
                return BadRequest();
            }

            Payments db_payments = db.Payments.Find(id);
            db_payments.amount = payments.amount;
            db_payments.payment_method = payments.payment_method;
            db_payments.payment_date = payments.payment_date;

            db.Entry(db_payments).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PaymentsExists(id))
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

        // POST: api/Payments
        [ResponseType(typeof(Payments))]
        public IHttpActionResult PostPayments(Payments payments)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Payments.Add(payments);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = payments.payment_id }, payments);
        }

        // DELETE: api/Payments/5
        [ResponseType(typeof(Payments))]
        public IHttpActionResult DeletePayments(int id)
        {
            Payments payments = db.Payments.Find(id);
            if (payments == null)
            {
                return NotFound();
            }

            db.Payments.Remove(payments);
            db.SaveChanges();

            return Ok(payments);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PaymentsExists(int id)
        {
            return db.Payments.Count(e => e.payment_id == id) > 0;
        }
    }

    public class PaymentDto
    {
        public int PaymentId { get; set; }
        public int BookingId { get; set; }
        public decimal Amount { get; set; }
        public string PaymentMethod { get; set; }
        public int? PaymentStatus { get; set; }
        public DateTime? PaymentDate { get; set; }
    }
}