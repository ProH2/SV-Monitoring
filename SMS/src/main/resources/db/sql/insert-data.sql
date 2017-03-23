INSERT INTO user (persname, username, password, phonenr, email) VALUES ('jens', 'DarkHell2', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'jens@gmail.com');
INSERT INTO user (persname, username, password, phonenr, email) VALUES ('martin', 'Dogist', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'mart@yahoo.com');
INSERT INTO user (persname, username, password, phonenr, email) VALUES ('sebastian', 'Irish', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'seppl@gmail.com');
INSERT INTO user (persname, username, password, phonenr, email) VALUES ('Manuel', 'Kant', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'Manu@gmail.com');
INSERT INTO user (persname, username, password, phonenr, email) VALUES ('Andreas', 'Hoche', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'Andi@gmail.com');
INSERT INTO user (persname, username, password, phonenr, email) VALUES ('Seppl', 'Klaris', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'sepplKlaris@gmail.com');

INSERT INTO duty (userid, starttime, endtime) VALUES (1, '2016-03-03 00:00:00', '2016-03-04 20:00:00');
INSERT INTO duty (userid, starttime, endtime) VALUES (1, '2016-03-05 00:00:00', '2016-03-06 20:00:00');
INSERT INTO duty (userid, starttime, endtime) VALUES (5, '2016-03-07 00:00:00', '2016-03-08 20:00:00');
INSERT INTO duty (userid, starttime, endtime) VALUES (5, '2016-03-09 00:00:00', '2016-03-10 20:00:00');
INSERT INTO duty (userid, starttime, endtime) VALUES (2, '2016-03-11 00:00:00', '2016-03-12 20:00:00');
INSERT INTO duty (userid, starttime, endtime) VALUES (3, '2016-03-13 00:00:00', '2016-03-14 20:00:00');
INSERT INTO duty (userid, starttime, endtime) VALUES (4, '2016-03-15 00:00:00', '2016-03-16 20:00:00'); 

/*
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_01', 'Comment to TestComment_01', 1, '2017-03-03');
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_02', 'Comment to TestComment_02', 1, '2017-03-03');
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_03', 'Comment to TestComment_03', 2, '2017-03-05');
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_04', 'Comment to TestComment_04', 3, '2017-03-04');
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_05', 'Comment to TestComment_05', 6, '2017-03-07');
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_06', 'Comment to TestComment_06', 5, '2017-03-11');
INSERT INTO comment (comment, commentto, author, lastchanged) VALUES ('TestComment_07', 'Comment to TestComment_07', 5, '2017-03-11');

INSERT INTO hostgroup (name, hostlist) VALUES ('HostGroup01', 'PC01;PC02;PC03;PC04');
INSERT INTO hostgroup (name, hostlist) VALUES ('HostGroup02', 'PC05;PC06;PC07;PC08');
INSERT INTO hostgroup (name, hostlist) VALUES ('HostGroup03', 'PC09;PC10;PC11;PC12');
INSERT INTO hostgroup (name, hostlist) VALUES ('HostGroup04', 'PC13;PC14;PC15;PC16');
INSERT INTO hostgroup (name, hostlist) VALUES ('HostGroup05', 'PC17;PC18;PC19;PC20');
INSERT INTO hostgroup (name, hostlist) VALUES ('HostGroup06', 'PC21;PC22;PC23;PC24');

INSERT INTO log (timestamp, logcause, logentry) VALUES ('2017-03-04 20:00:00', 'Temperature high!', 'Temperature reached 70!');
INSERT INTO log (timestamp, logcause, logentry) VALUES ('2017-03-05 20:00:00', 'Temperature high!', 'Temperature reached 90!');
INSERT INTO log (timestamp, logcause, logentry) VALUES ('2017-03-05 20:00:00', 'Temperature high!', 'Temperature reached 80!');
INSERT INTO log (timestamp, logcause, logentry) VALUES ('2017-03-06 20:00:00', 'Temperature high!', 'Temperature reached 20!');
INSERT INTO log (timestamp, logcause, logentry) VALUES ('2017-03-11 20:00:00', 'Temperature high!', 'Temperature reached 40!');
INSERT INTO log (timestamp, logcause, logentry) VALUES ('2017-03-11 20:00:00', 'Temperature high!', 'Temperature reached 60!');*/