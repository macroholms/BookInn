using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using serverapi.Models;

namespace serverapi.Controllers
{
    public class RoomsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Rooms
        public IQueryable<RoomDto> GetRooms()
        {
            return db.Rooms.Select(r => new RoomDto
            {
                RoomId = r.room_id,
                HotelId = r.hotel_id,
                TypeId = r.type_id,
                Description = r.description,
                Capacity = r.capacity,
                QuantityAvailable = r.quantity_available,
                PricePerNight = r.price_per_night,
                Size = r.size,
                RoomPreview = r.room_preview
            });
        }

        // GET: api/Rooms/5
        [ResponseType(typeof(Rooms))]
        public IHttpActionResult GetRooms(int id)
        {
            Rooms r = db.Rooms.Find(id);
            if (r == null)
            {
                return NotFound();
            }

            RoomDto room = new RoomDto();
            room.RoomId = r.room_id;
            room.HotelId = r.hotel_id;
            room.TypeId = r.type_id;
            room.Description = r.description;
            room.Capacity = r.capacity;
            room.QuantityAvailable = r.quantity_available;
            room.PricePerNight = r.price_per_night;
            room.Size = r.size;
            room.RoomPreview = r.room_preview;
            return Ok(room);
        }

        // PUT: api/Rooms/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutRooms(int id, Rooms rooms)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Rooms.Find(id) is null)
            {
                return BadRequest();
            }

            Rooms db_rooms = db.Rooms.Find(id);
            db_rooms.description = rooms.description;
            db_rooms.capacity = rooms.capacity;
            db_rooms.quantity_available = rooms.quantity_available;
            db_rooms.price_per_night = rooms.price_per_night;
            db_rooms.size = rooms.size;
            db_rooms.room_preview = rooms.room_preview;

            db.Entry(db_rooms).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!RoomsExists(id))
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

        // POST: api/Rooms
        [ResponseType(typeof(Rooms))]
        public IHttpActionResult PostRooms(Rooms rooms)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Rooms.Add(rooms);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = rooms.room_id }, rooms);
        }

        // DELETE: api/Rooms/5
        [ResponseType(typeof(Rooms))]
        public IHttpActionResult DeleteRooms(int id)
        {
            Rooms rooms = db.Rooms.Find(id);
            if (rooms == null)
            {
                return NotFound();
            }

            db.Rooms.Remove(rooms);
            db.SaveChanges();

            return Ok(rooms);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool RoomsExists(int id)
        {
            return db.Rooms.Count(e => e.room_id == id) > 0;
        }
    }

    public class RoomDto
    {
        public int RoomId { get; set; }
        public int HotelId { get; set; }
        public int TypeId { get; set; }
        public string Description { get; set; }
        public int Capacity { get; set; }
        public int QuantityAvailable { get; set; }
        public decimal PricePerNight { get; set; }
        public int? Size { get; set; }
        public int? RoomPreview { get; set; }
    }
}