INSERT INTO users VALUES (1, 'jens', 'DarkHell2', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'jens@gmail.com');
INSERT INTO users VALUES (2, 'martin', 'Dogist', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'mart@yahoo.com');
INSERT INTO users VALUES (3, 'sebastian', 'Irish', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'seppl@gmail.com');
INSERT INTO users VALUES (4, 'Manuel', 'Kant', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'Manu@gmail.com');
INSERT INTO users VALUES (5, 'Andreas', 'Hoche', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'Andi@gmail.com');
INSERT INTO users VALUES (6, 'Seppl', 'Klaris', 'b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86', '0664 / 921 17 95', 'sepplKlaris@gmail.com');

INSERT INTO duty VALUES (1, 1, '2016-03-03', '2016-03-04', 0);
INSERT INTO duty VALUES (2, 1, '2016-03-05', '2016-03-06', 1);
INSERT INTO duty VALUES (3, 5, '2016-03-07', '2016-03-08', 1);
INSERT INTO duty VALUES (4, 6, '2016-03-09', '2016-03-10', 1);
INSERT INTO duty VALUES (5, 2, '2016-03-11', '2016-03-12', 0);
INSERT INTO duty VALUES (6, 3, '2016-03-13', '2016-03-14', 1);
INSERT INTO duty VALUES (7, 4, '2016-03-15', '2016-03-16', 0);

INSERT INTO comment VALUES (1, 'TestComment_01', 'Comment to TestComment_01', 1, '2017-03-03');
INSERT INTO comment VALUES (2, 'TestComment_02', 'Comment to TestComment_02', 1, '2017-03-03');
INSERT INTO comment VALUES (3, 'TestComment_03', 'Comment to TestComment_03', 2, '2017-03-05');
INSERT INTO comment VALUES (4, 'TestComment_04', 'Comment to TestComment_04', 3, '2017-03-04');
INSERT INTO comment VALUES (5, 'TestComment_05', 'Comment to TestComment_05', 6, '2017-03-07');
INSERT INTO comment VALUES (6, 'TestComment_06', 'Comment to TestComment_06', 5, '2017-03-11');
INSERT INTO comment VALUES (7, 'TestComment_07', 'Comment to TestComment_07', 5, '2017-03-11');

INSERT INTO hostgroup VALUES (1, 'HostGroup01', 'PC01;PC02;PC03;PC04');
INSERT INTO hostgroup VALUES (2, 'HostGroup02', 'PC05;PC06;PC07;PC08');
INSERT INTO hostgroup VALUES (3, 'HostGroup03', 'PC09;PC10;PC11;PC12');
INSERT INTO hostgroup VALUES (4, 'HostGroup04', 'PC13;PC14;PC15;PC16');
INSERT INTO hostgroup VALUES (5, 'HostGroup05', 'PC17;PC18;PC19;PC20');
INSERT INTO hostgroup VALUES (6, 'HostGroup06', 'PC21;PC22;PC23;PC24');

INSERT INTO log VALUES (1, '2017-03-04 20:00:00', 'Temperature high!', 'Temperature reached 70!');
INSERT INTO log VALUES (2, '2017-03-05 20:00:00', 'Temperature high!', 'Temperature reached 90!');
INSERT INTO log VALUES (3, '2017-03-05 20:00:00', 'Temperature high!', 'Temperature reached 80!');
INSERT INTO log VALUES (4, '2017-03-06 20:00:00', 'Temperature high!', 'Temperature reached 20!');
INSERT INTO log VALUES (5, '2017-03-11 20:00:00', 'Temperature high!', 'Temperature reached 40!');
INSERT INTO log VALUES (6, '2017-03-11 20:00:00', 'Temperature high!', 'Temperature reached 60!');