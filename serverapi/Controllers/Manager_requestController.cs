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
    public class Manager_requestController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Manager_request
        public IQueryable<RequestDto> GetManager_request()
        {
            return db.Manager_request.Select(r => new RequestDto
            {
                RequestId = r.request_id,
                PhotoDoc = r.photo_doc,
                RequestText = r.request_text,
                UserId = r.user_id
            });
        }

        // GET: api/Manager_request/5
        [ResponseType(typeof(Manager_request))]
        public IHttpActionResult GetManager_request(int id)
        {
            Manager_request r = db.Manager_request.Find(id);
            if (r == null)
            {
                return NotFound();
            }

            RequestDto dto = new RequestDto();
            dto.RequestId = r.request_id;
            dto.PhotoDoc = r.photo_doc;
            dto.RequestText = r.request_text;
            dto.UserId = r.user_id;

            return Ok(dto);
        }

        // PUT: api/Manager_request/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutManager_request(int id, Manager_request manager_request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Manager_request.Find(id) is null)
            {
                return BadRequest();
            }
            
            Manager_request db_manager_request = db.Manager_request.Find(id);

            db_manager_request.request_text = manager_request.request_text;

            db.Entry(db_manager_request).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!Manager_requestExists(id))
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

        // POST: api/Manager_request
        [ResponseType(typeof(Manager_request))]
        public IHttpActionResult PostManager_request(Manager_request manager_request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Manager_request.Add(manager_request);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = manager_request.request_id }, manager_request);
        }

        // DELETE: api/Manager_request/5
        [ResponseType(typeof(Manager_request))]
        public IHttpActionResult DeleteManager_request(int id)
        {
            Manager_request manager_request = db.Manager_request.Find(id);
            if (manager_request == null)
            {
                return NotFound();
            }

            db.Manager_request.Remove(manager_request);
            db.SaveChanges();

            return Ok(manager_request);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool Manager_requestExists(int id)
        {
            return db.Manager_request.Count(e => e.request_id == id) > 0;
        }
    }

    public class RequestDto
    {
        public int RequestId { get; set; }
        public int? PhotoDoc { get; set; }
        public string RequestText { get; set; }
        public int? UserId { get; set; }
    }
}