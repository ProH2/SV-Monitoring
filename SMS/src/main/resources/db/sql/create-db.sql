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


CREATe TABLE comment(
    commentid INTEGER PRIMARY KEY,
    comment varchar(100) NOT NULL,
    commentto varchar(80) NOT NULL,
    author INTEGER NOT NULL,
    lastchanged DATE NOT NULL,

    FOREIGN KEY (author) REFERENCES users(userid) on delete cascade
);


CREATE TABLE hostgroup(
    hostgroupnr INTEGER PRIMARY KEY,
    name varchar(45) NOT NULL,
    hostlist varchar(2000) NOT NULL
);


CREATE TABLE log(
    logid INTEGER PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    logcause varchar(80) NOT NULL,
    logentry varchar(200) NOT NULL
);