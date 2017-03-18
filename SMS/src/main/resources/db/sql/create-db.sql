DROP TABLE users IF EXISTS;
DROP TABLE duty IF EXISTS;

CREATE TABLE users (
  userid INTEGER PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  username varchar(30) NOT NULL,
  password varchar(128) NOT NULL,
  phonenr varchar(25),
  email  VARCHAR(50)
);

CREATE TABLE duty(
    dutyid INTEGER PRIMARY KEY,
    userid INTEGER NOT NULL,
    starttime DATE NOT NULL,
    endtime DATE NOT NULL,
    notifyart INTEGER NOT NULL,

    FOREIGN KEY (userid) REFERENCES users(userid) on delete cascade
);

    