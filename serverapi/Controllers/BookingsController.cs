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
    public class BookingsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Bookings
        public IQueryable<BookingDto> GetBookings()
        {
            return db.Bookings.Select(b => new BookingDto
                {
                    BookingId = b.booking_id,
                    UserId = b.user_id,
                    RoomId = b.room_id,
                    CheckInDate = b.check_in_date,
                    CheckOutDate = b.check_out_date,
                    GuestsNumber = b.guests_number,
                    TotalPrice = b.total_price,
                    BookingStatus = b.booking_status,
                    CreatedAt = b.created_at,
                    SpecialRequests = b.special_requests
                });
        }

        public IQueryable<BookingDto> GetBookingsU(int userId)
        {
            return db.Bookings.Select(b => new BookingDto
            {
                BookingId = b.booking_id,
                UserId = b.user_id,
                RoomId = b.room_id,
                CheckInDate = b.check_in_date,
                CheckOutDate = b.check_out_date,
                GuestsNumber = b.guests_number,
                TotalPrice = b.total_price,
                BookingStatus = b.booking_status,
                CreatedAt = b.created_at,
                SpecialRequests = b.special_requests
            }).Where(r => r.UserId == userId);
        }

        // GET: api/Bookings/5
        [ResponseType(typeof(Bookings))]
        public IHttpActionResult GetBookings(int id)
        {
            Bookings b = db.Bookings.Find(id);
            if (b == null)
            {
                return NotFound();
            }

            BookingDto book = new BookingDto();

            book.BookingId = b.booking_id;
            book.UserId = b.user_id;
            book.RoomId = b.room_id;
            book.CheckInDate = b.check_in_date;
            book.CheckOutDate = b.check_out_date;
            book.GuestsNumber = b.guests_number;
            book.TotalPrice = b.total_price;
            book.BookingStatus = b.booking_status;
            book.CreatedAt = b.created_at;
            book.SpecialRequests = b.special_requests;
            

            return Ok(book);
        }

        // PUT: api/Bookings/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutBookings(int id, Bookings bookings)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Bookings.Find(id) is null)
            {
                return BadRequest();
            }

            Bookings db_bookings = db.Bookings.Find(id);

            db_bookings.total_price = bookings.total_price;
            db_bookings.check_in_date = bookings.check_in_date;
            db_bookings.check_out_date = bookings.check_out_date;
            db_bookings.created_at = bookings.created_at;
            db_bookings.guests_number = bookings.guests_number;
            db_bookings.special_requests = bookings.special_requests;

            db.Entry(db_bookings).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!BookingsExists(id))
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

        // POST: api/Bookings
        [ResponseType(typeof(Bookings))]
        public IHttpActionResult PostBookings(Bookings bookings)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Bookings.Add(bookings);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = bookings.booking_id }, bookings);
        }

        // DELETE: api/Bookings/5
        [ResponseType(typeof(Bookings))]
        public IHttpActionResult DeleteBookings(int id)
        {
            Bookings bookings = db.Bookings.Find(id);
            if (bookings == null)
            {
                return NotFound();
            }

            db.Bookings.Remove(bookings);
            db.SaveChanges();

            return Ok(bookings);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool BookingsExists(int id)
        {
            return db.Bookings.Count(e => e.booking_id == id) > 0;
        }
    }

    public class BookingDto
    {
        public int BookingId { get; set; }
        public int UserId { get; set; }
        public int RoomId { get; set; }
        public DateTime CheckInDate { get; set; }
        public DateTime CheckOutDate { get; set; }
        public int GuestsNumber { get; set; }
        public decimal TotalPrice { get; set; }
        public int? BookingStatus { get; set; }
        public DateTime? CreatedAt { get; set; }
        public string SpecialRequests { get; set; }
    }
}