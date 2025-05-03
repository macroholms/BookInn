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
using System.Xml.Linq;
using serverapi.Models;

namespace serverapi.Controllers
{
    public class RoomTypesController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/RoomTypes
        public IQueryable<RoomTypesDto> GetRoomTypes()
        {
            return db.RoomTypes.Select(r => new RoomTypesDto
            {
                type_id = r.type_id,
                name = r.name,
                description = r.description
            });
        }

        // GET: api/RoomTypes/5
        [ResponseType(typeof(RoomTypes))]
        public IHttpActionResult GetRoomTypes(int id)
        {
            RoomTypes r = db.RoomTypes.Find(id);
            if (r == null)
            {
                return NotFound();
            }

            RoomTypesDto room = new RoomTypesDto();

            room.type_id = r.type_id;
            room.name = r.name;
            room.description = r.description;

            return Ok(room);
        }

        // PUT: api/RoomTypes/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutRoomTypes(int id, RoomTypes roomTypes)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.RoomTypes.Find(id) is null)
            {
                return BadRequest();
            }

            RoomTypes db_roomTypes = db.RoomTypes.Find(id);
            db_roomTypes.name = roomTypes.name;
            db_roomTypes.description = roomTypes.description;

            db.Entry(db_roomTypes).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!RoomTypesExists(id))
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

        // POST: api/RoomTypes
        [ResponseType(typeof(RoomTypes))]
        public IHttpActionResult PostRoomTypes(RoomTypes roomTypes)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.RoomTypes.Add(roomTypes);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = roomTypes.type_id }, roomTypes);
        }

        // DELETE: api/RoomTypes/5
        [ResponseType(typeof(RoomTypes))]
        public IHttpActionResult DeleteRoomTypes(int id)
        {
            RoomTypes roomTypes = db.RoomTypes.Find(id);
            if (roomTypes == null)
            {
                return NotFound();
            }

            db.RoomTypes.Remove(roomTypes);
            db.SaveChanges();

            return Ok(roomTypes);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool RoomTypesExists(int id)
        {
            return db.RoomTypes.Count(e => e.type_id == id) > 0;
        }
    }

    public class RoomTypesDto
    {
        public int type_id {  get; set; }
        public string name { get; set; }
        public string description {  get; set; }
    }
}