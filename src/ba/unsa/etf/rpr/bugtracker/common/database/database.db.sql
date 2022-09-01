BEGIN TRANSACTION;
PRAGMA foreign_keys = ON;


CREATE TABLE IF NOT EXISTS "User" (
    "id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
    "firstname"	TEXT NOT NULL,
    "lastname"	TEXT NOT NULL,
    "username"	TEXT NOT NULL,
    "password"	TEXT NOT NULL,
    "email"	TEXT NOT NULL,
    "department"	INTEGER NOT NULL
);


CREATE TABLE IF NOT EXISTS "Bug" (
    "id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
    "title"	TEXT NOT NULL,
    "description"	TEXT,
    "language"	INTEGER,
    "urgency"	INTEGER,
    "keywords"	TEXT,
    "code"	TEXT,
    "date"	TEXT NOT NULL,
    "imageUrl"	TEXT,
    "asker_id"	TEXT NOT NULL,
    FOREIGN KEY("asker_id") REFERENCES "User"("id")
);


CREATE TABLE IF NOT EXISTS "Solution" (
	"id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	"description"	TEXT NOT NULL,
	"date"	TEXT NOT NULL,
	"code"	TEXT,
	"imageUrl"	TEXT,
	"solver_id"	INTEGER NOT NULL,
	FOREIGN KEY("solver_id") REFERENCES "User"("id"),
	FOREIGN KEY("id") REFERENCES "Bug"("id")
);

COMMIT;
