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
    public class PhotosController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Photos
        public IQueryable<photoDto> GetPhotos()
        {
            return db.Photos.Select(h => new photoDto
            {
                photo_id = h.photo_id,
                photo_data = h.photo_data
            });
        }

        // GET: api/Photos/5
        [ResponseType(typeof(Photos))]
        public IHttpActionResult GetPhotos(int id)
        {
            Photos p = db.Photos.Find(id);
            if (p == null)
            {
                return NotFound();
            }

            photoDto photos = new photoDto();
            photos.photo_id = p.photo_id;
            photos.photo_data = p.photo_data;

            return Ok(photos);
        }

        // PUT: api/Photos/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutPhotos(int id, Photos photos)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Photos.Find(id) is null)
            {
                return BadRequest();
            }
            
            Photos db_photos = db.Photos.Find(id);
            db_photos.photo_data = photos.photo_data;

            db.Entry(db_photos).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PhotosExists(id))
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

        // POST: api/Photos
        [ResponseType(typeof(Photos))]
        public IHttpActionResult PostPhotos(Photos photos)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Photos.Add(photos);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = photos.photo_id }, photos);
        }

        // DELETE: api/Photos/5
        [ResponseType(typeof(Photos))]
        public IHttpActionResult DeletePhotos(int id)
        {
            Photos photos = db.Photos.Find(id);
            if (photos == null)
            {
                return NotFound();
            }

            db.Photos.Remove(photos);
            db.SaveChanges();

            return Ok(photos);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool PhotosExists(int id)
        {
            return db.Photos.Count(e => e.photo_id == id) > 0;
        }
    }

    public class photoDto
    {
        public int photo_id {  get; set; }
        public byte[] photo_data {  get; set; }
    }
}