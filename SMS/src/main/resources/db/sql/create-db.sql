CREATE TABLE user (
  usernr INTEGER IDENTITY PRIMARY KEY,
  persname VARCHAR(30) NOT NULL,
  username varchar(30) NOT NULL,
  password varchar(128) NOT NULL,
  accounttype varchar(15) NOT NULL,
  email  VARCHAR(50),
  disabled BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE duty(
    dutyid INTEGER IDENTITY PRIMARY KEY,
    userid INTEGER NOT NULL,
    starttime TIMESTAMP NOT NULL,
    endtime TIMESTAMP NOT NULL,

    FOREIGN KEY (userid) REFERENCES user(usernr) on delete cascade
);


CREATE TABLE comment(
    commentid INTEGER IDENTITY PRIMARY KEY,
    comment varchar(100) NOT NULL,
    commentto varchar(80) NOT NULL,
    author INTEGER NOT NULL,
    lastchanged TIMESTAMP NOT NULL,

    FOREIGN KEY (author) REFERENCES user(usernr) on delete cascade
);


CREATE TABLE hostgroup(
    HostgroupId INTEGER IDENTITY PRIMARY KEY,
    HostgroupName varchar(45) NOT NULL,
    AssignedHosts varchar(2000) NOT NULL
);


CREATE TABLE log(
    logid INTEGER IDENTITY PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    logcause varchar(80) NOT NULL,
    logentry varchar(200) NOT NULL
);