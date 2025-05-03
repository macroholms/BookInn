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
    public class UsersController : ApiController
    {
        private hotelEntities db = new hotelEntities();

        // GET: api/Users
        public IQueryable<UserDto> GetUsers()
        {
            return db.Users.Select(u => new UserDto
            {
                UserId = u.user_id,
                Fio = u.fio,
                Email = u.email,
                Phone = u.phone,
                RoleId = u.role_id,
                PasswordHash = u.password_hash,
                RegistrationDate = u.registration_date
            });
        }

        // GET: api/Users/5
        [ResponseType(typeof(Users))]
        public IHttpActionResult GetUsers(int id)
        {
            Users users = db.Users.Find(id);
            if (users == null)
            {
                return NotFound();
            }

            var userDto = new UserDto
            {
                UserId = users.user_id,
                Fio = users.fio,
                Email = users.email,
                Phone = users.phone,
                RoleId = users.role_id,
                PasswordHash = users.password_hash,
                RegistrationDate = users.registration_date
            };

            return Ok(userDto);
        }

        [ResponseType(typeof(Users))]
        public IHttpActionResult GetUsers(string Email)
        {
            List<Users> users = db.Users.ToList();
            Users user = new Users();
            for (int i = 0; i < users.Count; i++)
            {
                if (users[i].email == Email) { user =  users[i]; break; }
            }
            if (user == null)
            {
                return NotFound();
            }

            var userDto = new UserDto
            {
                UserId = user.user_id,
                Fio = user.fio,
                Email = user.email,
                Phone = user.phone,
                RoleId = user.role_id,
                PasswordHash = user.password_hash,
                RegistrationDate = user.registration_date
            };

            return Ok(userDto);
        }

        // PUT: api/Users/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutUsers(int id, Users users)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (db.Users.Find(id) is null)
            {
                return BadRequest();
            }

            Users db_users = db.Users.Find(id);
            db_users.fio = users.fio;
            db_users.email = users.email;
            db_users.phone = users.phone;
            db_users.password_hash = users.password_hash;
            db_users.role_id = 4;
            db_users.registration_date = users.registration_date;

            db.Entry(db_users).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UsersExists(id))
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

        // POST: api/Users
        [ResponseType(typeof(Users))]
        public IHttpActionResult PostUsers(Users users)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Users.Add(users);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = users.user_id }, users);
        }

        // DELETE: api/Users/5
        [ResponseType(typeof(Users))]
        public IHttpActionResult DeleteUsers(int id)
        {
            Users users = db.Users.Find(id);
            if (users == null)
            {
                return NotFound();
            }

            db.Users.Remove(users);
            db.SaveChanges();

            return Ok(users);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool UsersExists(int id)
        {
            return db.Users.Count(e => e.user_id == id) > 0;
        }
    }

    public class UserDto
    {
        public int UserId { get; set; }
        public string Fio { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }
        public string PasswordHash { get; set; }
        public int? RoleId { get; set; }
        public DateTime? RegistrationDate { get; set; }
    }
}