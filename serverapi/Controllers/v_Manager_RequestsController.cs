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
    public class v_Manager_RequestsController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/v_Manager_Requests
        public IQueryable<v_Manager_Requests> Getv_Manager_Requests()
        {
            return db.v_Manager_Requests;
        }

        // GET: api/v_Manager_Requests/5
        [ResponseType(typeof(v_Manager_Requests))]
        public IHttpActionResult Getv_Manager_Requests(int id)
        {
            v_Manager_Requests v_Manager_Requests = db.v_Manager_Requests.Find(id);
            if (v_Manager_Requests == null)
            {
                return NotFound();
            }

            return Ok(v_Manager_Requests);
        }

        // PUT: api/v_Manager_Requests/5
        [ResponseType(typeof(void))]
        public IHttpActionResult Putv_Manager_Requests(int id, v_Manager_Requests v_Manager_Requests)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.v_Manager_Requests.Find(id) is null)
            {
                return BadRequest();
            }

            v_Manager_Requests Manager_Requests = db.v_Manager_Requests.Find(id);
            Manager_Requests.request_text = v_Manager_Requests.request_text;
            Manager_Requests.user_fio = v_Manager_Requests.user_fio;
            Manager_Requests.user_id = v_Manager_Requests.user_id;
            Manager_Requests.doc_photo = v_Manager_Requests.doc_photo;

            db.Entry(Manager_Requests).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!v_Manager_RequestsExists(id))
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

        // POST: api/v_Manager_Requests
        [ResponseType(typeof(v_Manager_Requests))]
        public IHttpActionResult Postv_Manager_Requests(v_Manager_Requests v_Manager_Requests)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.v_Manager_Requests.Add(v_Manager_Requests);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (v_Manager_RequestsExists(v_Manager_Requests.request_id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = v_Manager_Requests.request_id }, v_Manager_Requests);
        }

        // DELETE: api/v_Manager_Requests/5
        [ResponseType(typeof(v_Manager_Requests))]
        public IHttpActionResult Deletev_Manager_Requests(int id)
        {
            v_Manager_Requests v_Manager_Requests = db.v_Manager_Requests.Find(id);
            if (v_Manager_Requests == null)
            {
                return NotFound();
            }

            db.v_Manager_Requests.Remove(v_Manager_Requests);
            db.SaveChanges();

            return Ok(v_Manager_Requests);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool v_Manager_RequestsExists(int id)
        {
            return db.v_Manager_Requests.Count(e => e.request_id == id) > 0;
        }
    }
}