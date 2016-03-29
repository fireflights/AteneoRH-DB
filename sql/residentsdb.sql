#Database: residentsdb
#Username: sirtim
#Password: dormdb

DROP DATABASE residentsdb;
CREATE DATABASE residentsdb;
USE residentsdb;

CREATE TABLE residenceHall_t (
	hallID varchar(255) not null,
	residenceHall varchar(255) not null,
	PRIMARY KEY (hallID)
);

CREATE TABLE semester_t (
	schoolYear varchar(255) not null,
	semester int not null,
	PRIMARY KEY (schoolYear, semester)
);

CREATE TABLE residents_t (
	idNumber varchar(255) not null,
	lastName varchar(255) not null,
	firstName varchar(255) not null,
	middleInitial varchar (255),
	course varchar(255) not null,
	birthday date,
	gender varchar(255) not null,
	address varchar(400) not null,
	cityProvince varchar (255) not null,
	fatherName varchar(255),
	motherName varchar(255),
	telephoneNum varchar (255), 
	contactNum varchar(255),
	email varchar(255),
	isScholar boolean not null, 
	PRIMARY KEY (idNumber)
);


#One entry per resident per semester that he/she stayed in the dorm.
CREATE TABLE attendance_t (
	idNumber varchar (255) not null,
	schoolYear varchar(255) not null,
	semester int,
	hallID varchar (255) not null,
	roomNumber varchar (255) not null,
	PRIMARY KEY (idNumber, schoolYear, semester),
	FOREIGN KEY (hallID) REFERENCES residenceHall_t (hallID),
	FOREIGN KEY (schoolYear, semester) REFERENCES semester_t (schoolYear, semester),
	FOREIGN KEY (idNumber) REFERENCES residents_t (idNumber)
);


INSERT INTO residenceHall_t (hallID, residenceHall)
VALUES ('UDS','University Dorm-South');
INSERT INTO residenceHall_t (hallID, residenceHall)
VALUES ('uDN','University Dorm-North');
INSERT INTO residenceHall_t (hallID, residenceHall)
VALUES ('EH','Eliazo');
INSERT INTO residenceHall_t (hallID, residenceHall)
VALUES ('CH','Cervini');

INSERT INTO semester_t (schoolYear, semester)
VALUES ('2014-2015', 0);
INSERT INTO semester_t (schoolYear, semester)
VALUES ('2014-2015', 1);
INSERT INTO semester_t (schoolYear, semester)
VALUES ('2014-2015', 2);
INSERT INTO semester_t (schoolYear, semester)
VALUES ('2015-2016', 0);
INSERT INTO semester_t (schoolYear, semester)
VALUES ('2015-2016', 1);

INSERT INTO residents_t (idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, 
	fatherName, motherName, telephoneNum, contactNum, email, isScholar)
VALUES ('122141','Laddaran','Janine','T','BSMIS-MSCS','1996-08-18','Female','California Avenue, San Vicente','San Fernando City, La Union',
	'Romeo Laddaran','Juanita Laddaran','4266001','09156498638','janine.laddaran@obf.ateneo.edu','0');
INSERT INTO residents_t (idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, 
	fatherName, motherName, telephoneNum, contactNum, email, isScholar)
VALUES ('123609','Saw','Vermille Ann','T','BSCS','1995-07-27','Female','Berlin St., Mejia Subdivision','Ormoc City, Leyte',
	'Virgilio Saw','Portia Saw','4266001','09173062945','vermille.saw@obf.ateneo.edu','0');
INSERT INTO residents_t (idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, 
	fatherName, motherName, telephoneNum, contactNum, email, isScholar)
VALUES ('288263','Network','ARSA Team','T','BS Troubleshooting','2007-01-01','Male','AdMU, Katipunan Avenue','Quezon City',
	'Internet','ARSANeT','4266001','09001122334','arsanet@facebook.com','1');
INSERT INTO residents_t (idNumber, lastName, firstName, course, gender, address, cityProvince, 
	contactNum, email, isScholar)
VALUES ('333333','Nullfield','Tester','BS NULL','Male','AdMU, Katipunan Avenue','Quezon City',
	'09001122334','arsanet@facebook.com','1');

INSERT INTO attendance_t (idNumber, schoolYear, semester, hallID, roomNumber)
VALUES ('122141', '2014-2015', 1, 'EH', '304');
INSERT INTO attendance_t (idNumber, schoolYear, semester, hallID, roomNumber)
VALUES ('122141', '2014-2015', 2, 'EH', '304');
INSERT INTO attendance_t (idNumber, schoolYear, semester, hallID, roomNumber)
VALUES ('122141', '2015-2016', 0, 'UDS', '512');
INSERT INTO attendance_t (idNumber, schoolYear, semester, hallID, roomNumber)
VALUES ('123609', '2015-2016', 0, 'UDS', '401');
INSERT INTO attendance_t (idNumber, schoolYear, semester, hallID, roomNumber)
VALUES ('122141', '2015-2016', 1, 'UDS', '401');