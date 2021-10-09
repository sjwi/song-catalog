-- MySQL dump 10.16  Distrib 10.1.44-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: mysql.cfchurches.com    Database: Song
-- ------------------------------------------------------
-- Server version	5.7.29-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER,',','')_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS SongCategories;
CREATE TABLE SongCategories (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(50) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO SongCategories (NAME) values ('CWM'),('CCM'),('Hymns'),('Seasonal');

DROP TABLE IF EXISTS StoredLogins;
CREATE TABLE StoredLogins (
  ID int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  LOGIN_COOKIE varchar(5000) NOT NULL,
  CREATED_ON datetime NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS PasswordReset;
CREATE TABLE PasswordReset (
  ID int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  RESET_TOKEN varchar(5000) NOT NULL,
  CREATED_ON datetime NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS preferences;
CREATE TABLE preferences (
  username varchar(50) NOT NULL,
  PreferenceKey varchar(50) NOT NULL,
  PreferenceValue varchar(50) NOT NULL
);

--
-- Table structure for table AddressBook
--

DROP TABLE IF EXISTS AddressBook;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE AddressBook (
  ID int(11) NOT NULL AUTO_INCREMENT,
  username varchar(255) DEFAULT NULL,
  LastName varchar(255) NOT NULL,
  FirstName varchar(255) NOT NULL,
  Email varchar(255) NOT NULL,
  Phone varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=189 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table AddressBook
--

/*!40000 ALTER TABLE AddressBook DISABLE KEYS */;
INSERT INTO AddressBook VALUES 
(1,'','Adams','John','johnadams@mail.com','(999) 111-2222'),
(26,'admin','Administrator','System','admin@mail.com','(999) 111-3222'),
(2,'','Bell','Catherine','bellcatherine@mail.com','(999) 111-2222'),
(3,'','Castello','Ricky','rickycastello@mail.com','(999) 111-2222'),
(4,'','Deah','Benjamine','bendeah@mail.com','(999) 111-2224'),
(5,'','Evans','Marshall','marshallevans@mail.com','(999) 111-2225'),
(6,'','Freeman','George','georgefreeman@mail.com','(999) 111-2226'),
(7,'','Hahn','Janet','janethahn@mail.com','(999) 111-2227'),
(8,'','Ivy','Elizabeth','elizabethivy@mail.com','(999) 111-2228'),
(9,'','Jones','Bridget','bridgetjones@mail.com','(999) 111-2229'),
(10,'','Kirkland','John','johnkirkland@mail.com','(999) 111-2232'),
(11,'','Lestson','Matt','mattleston@mail.com','(999) 111-2242'),
(12,'','Maddow','Terry','terrymaddow@mail.com','(999) 111-2252'),
(13,'','Nail','Ann','annnail@mail.com','(999) 111-2262'),
(14,'','Olwin','Zeek','zeekolwin@mail.com','(999) 111-2272'),
(15,'','Perry','Thomas','thomasperry@mail.com','(999) 111-2282'),
(16,'','Quinn','Isaac','isaacquinn@mail.com','(999) 111-2292'),
(17,'','Rice','Becky','beckyrice@mail.com','(999) 111-2322'),
(18,'','Stephens','Ashley','ashleystephens@mail.com','(999) 111-2422'),
(19,'','Travis','Scott','scotttravis@mail.com','(999) 111-2522'),
(20,'','Umphrey','Brady','bradumphrey@mail.com','(999) 111-2622'),
(21,'','Vel','Mark','markvel@mail.com','(999) 111-2622'),
(22,'','Williams','Charles','charleswilliams@mail.com','(999) 111-2622'),
(23,'','Xue','Danny','dannyxue@mail.com','(999) 111-2622'),
(24,'','Yengo','Andrew','andrewyengo@mail.com','(999) 111-2622'),
(25,'','Zaragosa','Conner','connerzaragosa@mail.com','(999) 111-2622');
/*!40000 ALTER TABLE AddressBook ENABLE KEYS */;


--
-- Table structure for table AddressBookGroup
--

DROP TABLE IF EXISTS AddressBookGroup;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE AddressBookGroup (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table AddressBookGroup
--

/*!40000 ALTER TABLE AddressBookGroup DISABLE KEYS */;
INSERT INTO AddressBookGroup VALUES (1,'Team 1'),(2,'Team 2'),(3,'Team 3');
/*!40000 ALTER TABLE AddressBookGroup ENABLE KEYS */;


--
-- Table structure for table AddressBookRelated
--

DROP TABLE IF EXISTS AddressBookRelated;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE AddressBookRelated (
  ID int(11) NOT NULL AUTO_INCREMENT,
  GROUP_ID int(11) NOT NULL,
  ENTRY_ID int(11) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table AddressBookRelated
--

/*!40000 ALTER TABLE AddressBookRelated DISABLE KEYS */;
INSERT INTO AddressBookRelated VALUES 
(1,1,1),
(2,1,4),
(3,1,7),
(4,1,10),
(5,1,13),
(6,1,16),
(7,1,19),
(8,1,22),
(9,1,25),
(10,2,2),
(11,2,5),
(12,2,8),
(13,2,11),
(14,2,14),
(15,2,17),
(16,2,20),
(17,2,23),
(18,2,26),
(19,3,3),
(20,3,6),
(21,3,9),
(22,3,12),
(23,3,15),
(24,3,18),
(25,3,21),
(26,3,14);
/*!40000 ALTER TABLE AddressBookRelated ENABLE KEYS */;


--
-- Table structure for table EnrollmentToken
--

DROP TABLE IF EXISTS EnrollmentToken;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE EnrollmentToken (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TOKEN varchar(5000) DEFAULT NULL,
  EMAIL varchar(255) DEFAULT NULL,
  PRIVILEGE varchar(50) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table KeyMaster
--

DROP TABLE IF EXISTS KeyMaster;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE KeyMaster (
  keyId varchar(10) DEFAULT NULL,
  I varchar(5) DEFAULT NULL,
  isharp varchar(5) DEFAULT NULL,
  ii varchar(5) DEFAULT NULL,
  iii varchar(5) DEFAULT NULL,
  IV varchar(5) DEFAULT NULL,
  ivsharp varchar(5) DEFAULT NULL,
  V varchar(5) DEFAULT NULL,
  vi varchar(5) DEFAULT NULL,
  visharp varchar(5) DEFAULT NULL,
  vii varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table KeyMaster
--

/*!40000 ALTER TABLE KeyMaster DISABLE KEYS */;
INSERT INTO KeyMaster VALUES ('NumSys','I','1.5','ii','iii','IV','4.5','V','vi','6.5','vii'),('A','A','Bb','B','C#','D','D#','E','F#','G','G#'),('B','B','C','C#','D#','E','F','F#','G#','A','A#'),('C','C','C#','D','E','F','F#','G','A','Bb','B'),('D','D','D#','E','F#','G','G#','A','B','C','C#'),('E','E','F','F#','G#','A','Bb','B','C#','D','D#'),('F','F','F#','G','A','Bb','B','C','D','D#','E'),('G','G','G#','A','B','C','C#','D','E','F','F#');
/*!40000 ALTER TABLE KeyMaster ENABLE KEYS */;


--
-- Table structure for table Organizations
--

DROP TABLE IF EXISTS Organizations;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Organizations (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) NOT NULL,
  TITLE varchar(255) NOT NULL,
  LINK varchar(255),
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table Organizations
--

/*!40000 ALTER TABLE Organizations DISABLE KEYS */;
INSERT INTO Organizations VALUES (1,'Church1','Church 1','http://fakeurl1.com'),(2,'Church2','Church 2',null),(3,'Church3','Church 3','http://fakeurl1.com'),(4,'Church4','Church 4','http://fakeurl1.com');
/*!40000 ALTER TABLE Organizations ENABLE KEYS */;


--
-- Table structure for table Recording
--

DROP TABLE IF EXISTS Recording;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Recording (
  ID int(11) NOT NULL AUTO_INCREMENT,
  SONG_ID int(11) DEFAULT NULL,
  PATH varchar(255) DEFAULT NULL,
  EXT varchar(20) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table Recording
--

--INSERT INTO Recording (SONG_ID, PATH, EXT, FILE) Values
--()

/*!40000 ALTER TABLE Recording DISABLE KEYS */;
/*!40000 ALTER TABLE Recording ENABLE KEYS */;


--
-- Table structure for table Services
--

DROP TABLE IF EXISTS Services;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Services (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table Services
--

/*!40000 ALTER TABLE Services DISABLE KEYS */;
INSERT INTO Services VALUES (1,'Service'),(2,'Small Group'),(3,'Campus Ministry'),(4,'Worship Service');
/*!40000 ALTER TABLE Services ENABLE KEYS */;


--
-- Table structure for table SetList
--

DROP TABLE IF EXISTS SetList;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE SetList (
  ID int(11) NOT NULL AUTO_INCREMENT,
  SETLIST_ID int(11) NOT NULL,
  SONG_ID int(11) NOT NULL,
  DEFAULT_KEY varchar(25) DEFAULT NULL,
  SORT_ORDER int(11) DEFAULT NULL,
  ARTIST varchar(50) DEFAULT NULL,
  NOTES longtext,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table SetList
--

/*!40000 ALTER TABLE SetList DISABLE KEYS */;
INSERT INTO SetList VALUES 
(1,1,1,'G',1,NULL,''),
(2,1,5,'A',2,NULL,''),
(3,1,9,'B',3,NULL,''),
(4,1,13,'F',4,NULL,''),
(5,2,2,'G',1,NULL,''),
(6,2,6,'C',2,NULL,''),
(7,2,10,'B',3,NULL,''),
(8,2,14,'D',4,NULL,''),
(9,3,3,'D',1,NULL,''),
(10,3,7,'C',2,NULL,''),
(11,3,11,'A',3,NULL,''),
(12,3,15,'C',4,NULL,''),
(13,4,4,'E',1,NULL,''),
(14,4,8,'E',2,NULL,''),
(15,4,12,'G',3,NULL,''),
(16,4,16,'D',4,NULL,''),
(17,5,2,'G',1,NULL,''),
(18,5,6,'C',2,NULL,''),
(19,5,10,'B',3,NULL,''),
(20,5,14,'D',4,NULL,''),
(21,6,2,'G',1,NULL,''),
(22,6,6,'C',2,NULL,''),
(23,6,10,'B',3,NULL,''),
(24,6,14,'D',4,NULL,''),
(25,7,2,'G',1,NULL,''),
(26,7,6,'C',2,NULL,''),
(27,7,10,'B',3,NULL,''),
(28,7,14,'D',4,NULL,''),
(29,8,2,'G',1,NULL,''),
(30,8,6,'C',2,NULL,''),
(31,8,10,'B',3,NULL,''),
(32,8,14,'D',4,NULL,''),
(33,9,2,'G',1,NULL,''),
(34,9,6,'C',2,NULL,''),
(35,9,10,'B',3,NULL,''),
(36,9,14,'D',4,NULL,''),
(37,10,2,'G',1,NULL,''),
(38,10,6,'C',2,NULL,''),
(39,10,10,'B',3,NULL,''),
(40,10,14,'D',4,NULL,''),
(41,11,2,'G',1,NULL,''),
(42,11,6,'C',2,NULL,''),
(43,11,10,'B',3,NULL,''),
(44,11,14,'D',4,NULL,''),
(45,12,2,'G',1,NULL,''),
(46,12,6,'C',2,NULL,''),
(47,12,10,'B',3,NULL,''),
(48,12,14,'D',4,NULL,''),
(49,13,2,'G',1,NULL,''),
(50,13,6,'C',2,NULL,''),
(51,13,10,'B',3,NULL,''),
(52,13,14,'D',4,NULL,''),
(53,14,2,'G',1,NULL,''),
(54,14,6,'C',2,NULL,''),
(55,14,10,'B',3,NULL,''),
(56,14,14,'D',4,NULL,''),
(57,15,2,'G',1,NULL,''),
(58,15,6,'C',2,NULL,''),
(59,15,10,'B',3,NULL,''),
(60,15,14,'D',4,NULL,''),
(61,16,2,'G',1,NULL,''),
(62,16,6,'C',2,NULL,''),
(63,16,10,'B',3,NULL,''),
(64,16,14,'D',4,NULL,''),
(65,17,2,'G',1,NULL,''),
(66,17,6,'C',2,NULL,''),
(67,17,10,'B',3,NULL,''),
(68,17,14,'D',4,NULL,''),
(69,18,2,'G',1,NULL,''),
(70,18,6,'C',2,NULL,''),
(71,18,10,'B',3,NULL,''),
(72,18,14,'D',4,NULL,''),
(73,19,2,'G',1,NULL,''),
(74,19,6,'C',2,NULL,''),
(75,19,10,'B',3,NULL,''),
(76,19,14,'D',4,NULL,'');
/*!40000 ALTER TABLE SetList ENABLE KEYS */;


--
-- Table structure for table SetListArchive
--

DROP TABLE IF EXISTS SetListArchive;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE SetListArchive (
  ID int(11) NOT NULL,
  SETLIST_ID int(11) NOT NULL,
  SONG_ID int(11) NOT NULL,
  DEFAULT_KEY varchar(25) DEFAULT NULL,
  SORT_ORDER int(11) DEFAULT NULL,
  ARTIST varchar(50) DEFAULT NULL,
  NOTES longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table SetListArchive
--


--
-- Table structure for table SetListMaster
--

DROP TABLE IF EXISTS SetListMaster;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE SetListMaster (
  ID int(11) NOT NULL AUTO_INCREMENT,
  SETLIST_NAME longtext,
  CREATED_ON datetime DEFAULT NULL,
  LAST_UPDATED datetime DEFAULT NULL,
  FLAGGED_AS_MOST_RECENT_ON datetime DEFAULT NULL,
  ORGANIZATION int(11) DEFAULT NULL,
  SERVICE int(11) DEFAULT NULL,
  CREATED_BY varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table SetListMaster
--

/*!40000 ALTER TABLE SetListMaster DISABLE KEYS */;
INSERT INTO SetListMaster VALUES 
(1,'Church1 Service 06-22-2020','2019-08-23 17:40:55','2019-08-23 17:40:55','2019-08-23 18:40:55',1,1,'admin'),
(2,'Church2 Small Group 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-23 19:40:55',2,2,'admin'),
(3,'Church3 Campus Ministry 06-22-2020','2019-06-23 17:40:55','2019-06-23 17:40:55','2019-08-23 20:40:55',3,3,'admin'),
(4,'Church4 Worship 06-22-2020','2019-05-23 17:40:55','2019-05-23 17:40:55','2019-08-23 21:40:55',4,4,'admin'),
(5,'Church2 Worship 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-23 22:40:55',2,4,'admin'),
(6,'Church2 Worship 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-23 23:40:55',2,4,'admin'),
(7,'Church2 Worship 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-24 17:40:55',2,4,'admin'),
(8,'Church2 Worship 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-25 17:40:55',2,4,'admin'),
(9,'Church2 Campus Ministry 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-26 17:40:55',2,3,'admin'),
(10,'Church2 Campus Ministry 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-27 17:40:55',2,3,'admin'),
(11,'Church2 Campus Ministry 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-28 17:40:55',2,3,'admin'),
(12,'Church2 Campus Ministry 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-29 01:40:55',2,3,'admin'),
(13,'Church2 Campus Ministry 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-08-30 23:40:55',2,3,'admin'),
(14,'Church2 Service 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-09-23 22:40:55',2,1,'admin'),
(15,'Church2 Service 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-10-23 21:40:55',2,1,'admin'),
(16,'Church2 Service 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-11-23 20:40:55',2,1,'admin'),
(17,'Church2 Service 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2019-12-23 19:40:55',2,1,'admin'),
(18,'Church2 Small Group 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2020-08-23 18:40:55',2,2,'admin'),
(19,'Church2 Small Group 06-22-2020','2019-07-23 17:40:55','2019-07-23 17:40:55','2020-09-23 17:50:55',2,2,'admin');

/*!40000 ALTER TABLE SetListMaster ENABLE KEYS */;


--
-- Table structure for table Song
--

DROP TABLE IF EXISTS Song;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Song (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) DEFAULT NULL,
  BODY longtext,
  DEFAULT_KEY varchar(25) DEFAULT NULL,
  ARTIST varchar(50) DEFAULT NULL,
  NOTES longtext,
  CREATED_BY varchar(255) DEFAULT NULL,
  MODIFIED_BY varchar(255) DEFAULT NULL,
  CHANGED_ON datetime DEFAULT NULL,
  RELATED int(11) DEFAULT NULL,
  PRIVATE varchar(1) DEFAULT NULL,
  CATEGORY int(2) DEFAULT 1,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table Song
--

/*!40000 ALTER TABLE Song DISABLE KEYS */;
INSERT INTO Song (ID,NAME,BODY,DEFAULT_KEY,ARTIST,NOTES,CREATED_BY,MODIFIED_BY,CHANGED_ON,RELATED,PRIVATE,CATEGORY) VALUES 
(1,'Lorem Ipsum Dolor',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,1),
(21,'Lorem Ipsum Dolor',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',1,NULL,1),
(22,'Lorem Ipsum Dolor',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',1,NULL,1),
(2,'Labore et Dolore Magna Aliqua',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,2),
(23,'Labore et Dolore Magna Aliqua',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'G','',NULL,'admin','admin','2020-07-04 12:09:20',2,NULL,2),
(24,'Labore et Dolore Magna Aliqua',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',2,NULL,2),
(25,'Labore et Dolore Magna Aliqua',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',2,NULL,2),
(3,'Sit amet Consectetur',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,3),
(26,'Sit amet Consectetur',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',3,NULL,3),
(27,'Sit amet Consectetur',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',3,NULL,3),
(4,'Quisque id Diam',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'F','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,4),
(28,'Quisque id Diam',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'B','',NULL,'admin','admin','2020-07-04 12:09:20',4,NULL,4),
(5,'Velit Scelerisque',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,1),
(29,'Velit Scelerisque',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'G','',NULL,'admin','admin','2020-07-04 12:09:20',5,NULL,1),
(30,'Velit Scelerisque',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'G','',NULL,'admin','admin','2020-07-04 12:09:20',5,NULL,1),
(6,'Felis Donec et Odio',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,2),
(31,'Felis Donec et Odio',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',6,NULL,2),
(32,'Felis Donec et Odio',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',6,NULL,2),
(33,'Felis Donec et Odio',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',6,NULL,2),
(7,'Varius Sit Amet',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,3),
(34,'Varius Sit Amet',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'F','',NULL,'admin','admin','2020-07-04 12:09:20',7,NULL,3),
(35,'Varius Sit Amet',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',7,NULL,3),
(8,'Volutpat Blandit Aliquam',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'B','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,4),
(36,'Volutpat Blandit Aliquam',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',8,NULL,4),
(37,'Volutpat Blandit Aliquam',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',8,NULL,4),
(9,'Odio Euismod Lacinia',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'G','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,1),
(38,'Odio Euismod Lacinia',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',9,NULL,1),
(39,'Odio Euismod Lacinia',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'G','',NULL,'admin','admin','2020-07-04 12:09:20',9,NULL,1),
(10,'Viverra Nam Libero',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,2),
(40,'Viverra Nam Libero',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'B','',NULL,'admin','admin','2020-07-04 12:09:20',10,NULL,2),
(11,'Massa Sed Elementum',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,3),
(41,'Massa Sed Elementum',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',11,NULL,3),
(12,'Consectetur Lorem Donec',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,4),
(42,'Consectetur Lorem Donec',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',12,NULL,4),
(43,'Consectetur Lorem Donec',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',12,NULL,4),
(44,'Consectetur Lorem Donec',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'F','',NULL,'admin','admin','2020-07-04 12:09:20',12,NULL,4),
(13,'Elementum Sagittis',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'F','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,1),
(45,'Elementum Sagittis',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',13,NULL,1),
(14,'Quis Ipsum Suspendisse',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,2),
(46,'Quis Ipsum Suspendisse',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',14,NULL,2),
(47,'Quis Ipsum Suspendisse',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',14,NULL,2),
(15,'Consectetur a Erat',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'B','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,3),
(48,'Consectetur a Erat',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',15,NULL,3),
(16,'Mattis Aliquam Faucibus',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,4),
(49,'Mattis Aliquam Faucibus',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',16,NULL,4),
(17,'Arcu Dictum Varius',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'A','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,1),
(50,'Arcu Dictum Varius',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',17,NULL,1),
(18,'Morbi Tempus Iaculis',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'G','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,2),
(51,'Morbi Tempus Iaculis',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',18,NULL,2),
(19,'Dignissim Convallis',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,3),
(52,'Dignissim Convallis',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',19,NULL,3),
(20,'Tincidunt Arcu',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'D','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,4),
(53,'Tincidunt Arcu',STRINGDECODE('        I                    IV                       V7                    I\r\nSit amet, consectetur adipiscing elit\r\nIV                        I                     ii                           V\r\nSed do eiusmod tempor incididunt ut labore\r\nV7                              I                                                      iim7\r\nSit amet tellus cras adipiscing Eu nisl nunc\r\nIV                            I                              V               I\r\nNisl condimentum id venenatis a condimentum vitae sapien\r\n\r\nV                             I\r\nEu nisl nunc mi ipsum faucibus\r\nvi                            iim\r\nEu nisl nunc mi ipsum faucibus\r\nV7                       I                          ii                  V                           \r\nSit amet consectetur adipiscing elit pellentesque\r\nV7                   I                                                iim7                 \r\nQuisque id diam vel quam elementum pulvinar          \r\nIV                     I                                        V        I\r\nSem nulla pharetra diam sit amet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet\r\n\r\nIpsum dolor sit amet consectetur adipiscing elit duis\r\nNon tellus orci ac auctor augue mauris augue\r\nRisus viverra adipiscing at in tellus integer\r\nVarius sit amet mattis vulputate enim nulla aliquet'),'E','',NULL,'admin','admin','2020-07-04 12:09:20',20,NULL,4),
(54,'Is He Worthy?',STRINGDECODE('Do you feel the world is broken?\r\nWe do\r\nDo you feel the shadows deepen?\r\nWe do\r\nBut do you know that all the dark\r\nWont stop the light from getting through?\r\nWe do\r\nDo you wish that you could see it all made new?\r\nWe do\r\n\r\nIs all creation groaning?\r\nIt is\r\nIs a new creation coming?\r\nIt is\r\nIs the glory of the Lord\r\nTo be the light within our midst?\r\nIt is\r\nIs it good that we remind ourselves of this?\r\nIt is\r\n\r\nIs anyone worthy;\r\nIs anyone whole?\r\nIs anyone able to break the seal and open the scroll?\r\nThe Lion of Judah who conquered the grave\r\nHe is Davids Root\r\nAnd the Lamb who died to ransom the slave\r\n\r\nIs He worthy?\r\nIs He worthy\r\nOf all blessing and honor and glory?\r\nIs He worthy of this?\r\nHe is\r\n\r\nDoes the Father truly love us?\r\nHe does\r\nDoes the Spirit move among us?\r\nHe does\r\nAnd does Jesus our Messiah\r\nHold forever those He loves?\r\nHe does\r\nDoes our God intend to dwell again with us?\r\nHe does\r\n\r\nIs anyone worthy; Is anyone whole?\r\nIs anyone able to break the seal and open the scroll?\r\nThe Lion of Judah who conquered the grave\r\nHe is Davids Root\r\nAnd the Lamb who died to ransom the slave\r\nFrom evry people and tribe every nation and tongue\r\nHe has made us a kingdom and priests\r\nTo God to reign with the Son\r\n\r\nIs He worthy?\r\nIs He worthy\r\nOf all blessing and honor and glory?\r\nIs He worthy;\r\nIs He worthy?\r\nIs He worthy of this?\r\n\r\nHe is\r\nIs He worthy?\r\nIs He worthy?\r\nHe is!\r\nHe is!'),'C','',NULL,'admin','admin','2020-07-04 12:09:20',NULL,NULL,4);

--
-- Table structure for table SongArchive
--

DROP TABLE IF EXISTS SongArchive;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE SongArchive (
  ID int(11) NOT NULL,
  NAME varchar(255) DEFAULT NULL,
  BODY longtext,
  DEFAULT_KEY varchar(25) DEFAULT NULL,
  ARTIST varchar(50) DEFAULT NULL,
  NOTES longtext,
  CREATED_BY varchar(255) DEFAULT NULL,
  MODIFIED_BY varchar(255) DEFAULT NULL,
  CHANGED_ON datetime DEFAULT NULL,
  RELATED int(11) DEFAULT NULL,
  PRIVATE varchar(1) DEFAULT NULL,
  CATEGORY varchar(2) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table SongArchive
--

/*!40000 ALTER TABLE SongArchive DISABLE KEYS */;
/*!40000 ALTER TABLE SongArchive ENABLE KEYS */;


--
-- Table structure for table Version
--

DROP TABLE IF EXISTS Version;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE Version (
  ID int(11) NOT NULL AUTO_INCREMENT,
  SONG_ID int(11) DEFAULT NULL,
  VERSION_NAME int(11) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table Version
--

/*!40000 ALTER TABLE Version DISABLE KEYS */;
INSERT INTO Version VALUES 
(1,21,1),
(2,22,2),
(3,23,1),
(4,24,2),
(5,25,3),
(6,26,1),
(7,27,2),
(8,28,1),
(9,29,1),
(10,30,2),
(11,31,1),
(12,32,2),
(13,33,3),
(14,34,1),
(15,35,2),
(16,36,1),
(17,37,2),
(18,38,1),
(19,39,2),
(20,40,1),
(21,41,1),
(22,42,1),
(23,43,2),
(24,44,3),
(25,45,1),
(26,46,1),
(27,47,2),
(28,48,1),
(29,49,1),
(30,50,1),
(31,51,1),
(32,52,1),
(33,53,1);

/*!40000 ALTER TABLE Version ENABLE KEYS */;


--
-- Table structure for table VersionControl
--

DROP TABLE IF EXISTS VersionControl;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE VersionControl (
  ID int(11) NOT NULL,
  VERSION varchar(2) NOT NULL,
  NEXT_ID int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table VersionControl
--

/*!40000 ALTER TABLE VersionControl DISABLE KEYS */;
INSERT INTO VersionControl VALUES (0,'**',1),(1,'*A',2),(2,'*B',3),(3,'*C',4),(4,'*D',5),(5,'*E',6),(6,'*F',7),(7,'*G',8),(8,'*H',9),(9,'*I',10),(10,'*J',11),(11,'*K',12),(12,'*L',13),(13,'*M',14),(14,'*N',15),(15,'*O',16),(16,'*P',17),(17,'*Q',18),(18,'*R',19),(19,'*S',20),(20,'*T',21),(21,'*U',22),(22,'*V',23),(23,'*W',24),(24,'*X',25),(25,'*Y',26),(26,'*Z',27),(27,'AA',28),(28,'AB',29),(29,'AC',30),(30,'AD',31),(31,'AE',32),(32,'AF',33),(33,'AG',34),(34,'AH',35),(35,'AI',36),(36,'AJ',37),(37,'AK',38),(38,'AL',39),(39,'AM',40),(40,'AN',41),(41,'AO',42),(42,'AP',43),(43,'AQ',44),(44,'AR',45),(45,'AS',46),(46,'AT',47),(47,'AU',48),(48,'AV',49),(49,'AW',50),(50,'AX',51),(51,'AY',52),(52,'AZ',53),(53,'BA',54),(54,'BB',55),(55,'BC',56),(56,'BD',57),(57,'BE',58),(58,'BF',59),(59,'BG',60),(60,'BH',61),(61,'BI',62),(62,'BJ',63),(63,'BK',64),(64,'BL',65),(65,'BM',66),(66,'BN',67),(67,'BO',68),(68,'BP',69),(69,'BQ',70),(70,'BR',71),(71,'BS',72),(72,'BT',73),(73,'BU',74),(74,'BV',75),(75,'BW',76),(76,'BX',77),(77,'BY',78),(78,'BZ',79),(79,'CA',80),(80,'CB',81),(81,'CC',82),(82,'CD',83),(83,'CE',84),(84,'CF',85),(85,'CG',86),(86,'CH',87),(87,'CI',88),(88,'CJ',89),(89,'CK',90),(90,'CL',91),(91,'CM',92),(92,'CN',93),(93,'CO',94),(94,'CP',95),(95,'CQ',96),(96,'CR',97),(97,'CS',98),(98,'CT',99),(99,'CU',100),(100,'CV',101),(101,'CW',102),(102,'CX',103),(103,'CY',104),(104,'CZ',105),(105,'DA',106),(106,'DB',107),(107,'DC',108),(108,'DD',109),(109,'DE',110),(110,'DF',111),(111,'DG',112),(112,'DH',113),(113,'DI',114),(114,'DJ',115),(115,'DK',116),(116,'DL',117),(117,'DM',118),(118,'DN',119),(119,'DO',120),(120,'DP',121),(121,'DQ',122),(122,'DR',123),(123,'DS',124),(124,'DT',125),(125,'DU',126),(126,'DV',127),(127,'DW',128),(128,'DX',129),(129,'DY',130),(130,'DZ',131),(131,'EA',132),(132,'EB',133),(133,'EC',134),(134,'ED',135),(135,'EE',136),(136,'EF',137),(137,'EG',138),(138,'EH',139),(139,'EI',140),(140,'EJ',141),(141,'EK',142),(142,'EL',143),(143,'EM',144),(144,'EN',145),(145,'EO',146),(146,'EP',147),(147,'EQ',148),(148,'ER',149),(149,'ES',150),(150,'ET',151),(151,'EU',152),(152,'EV',153),(153,'EW',154),(154,'EX',155),(155,'EY',156),(156,'EZ',157),(157,'FA',158),(158,'FB',159),(159,'FC',160),(160,'FD',161),(161,'FE',162),(162,'FF',163),(163,'FG',164),(164,'FH',165),(165,'FI',166),(166,'FJ',167),(167,'FK',168),(168,'FL',169),(169,'FM',170),(170,'FN',171),(171,'FO',172),(172,'FP',173),(173,'FQ',174),(174,'FR',175),(175,'FS',176),(176,'FT',177),(177,'FU',178),(178,'FV',179),(179,'FW',180),(180,'FX',181),(181,'FY',182),(182,'FZ',183),(183,'GA',184),(184,'GB',185),(185,'GC',186),(186,'GD',187),(187,'GE',188),(188,'GF',189),(189,'GG',190),(190,'GH',191),(191,'GI',192),(192,'GJ',193),(193,'GK',194),(194,'GL',195),(195,'GM',196),(196,'GN',197),(197,'GO',198),(198,'GP',199),(199,'GQ',200),(200,'GR',201),(201,'GS',202),(202,'GT',203),(203,'GU',204),(204,'GV',205),(205,'GW',206),(206,'GX',207),(207,'GY',208),(208,'GZ',209),(209,'HA',210),(210,'HB',211),(211,'HC',212),(212,'HD',213),(213,'HE',214),(214,'HF',215),(215,'HG',216),(216,'HH',217),(217,'HI',218),(218,'HJ',219),(219,'HK',220),(220,'HL',221),(221,'HM',222),(222,'HN',223),(223,'HO',224),(224,'HP',225),(225,'HQ',226),(226,'HR',227),(227,'HS',228),(228,'HT',229),(229,'HU',230),(230,'HV',231),(231,'HW',232),(232,'HX',233),(233,'HY',234),(234,'HZ',235),(235,'IA',236),(236,'IB',237),(237,'IC',238),(238,'ID',239),(239,'IE',240),(240,'IF',241),(241,'IG',242),(242,'IH',243),(243,'II',244),(244,'IJ',245),(245,'IK',246),(246,'IL',247),(247,'IM',248),(248,'IN',249),(249,'IO',250),(250,'IP',251),(251,'IQ',252),(252,'IR',253),(253,'IS',254),(254,'IT',255),(255,'IU',256),(256,'IV',257),(257,'IW',258),(258,'IX',259),(259,'IY',260),(260,'IZ',261),(261,'JA',262),(262,'JB',263),(263,'JC',264),(264,'JD',265),(265,'JE',266),(266,'JF',267),(267,'JG',268),(268,'JH',269),(269,'JI',270),(270,'JJ',271),(271,'JK',272),(272,'JL',273),(273,'JM',274),(274,'JN',275),(275,'JO',276),(276,'JP',277),(277,'JQ',278),(278,'JR',279),(279,'JS',280),(280,'JT',281),(281,'JU',282),(282,'JV',283),(283,'JW',284),(284,'JX',285),(285,'JY',286),(286,'JZ',287),(287,'KA',288),(288,'KB',289),(289,'KC',290),(290,'KD',291),(291,'KE',292),(292,'KF',293),(293,'KG',294),(294,'KH',295),(295,'KI',296),(296,'KJ',297),(297,'KK',298),(298,'KL',299),(299,'KM',300),(300,'KN',301),(301,'KO',302),(302,'KP',303),(303,'KQ',304),(304,'KR',305),(305,'KS',306),(306,'KT',307),(307,'KU',308),(308,'KV',309),(309,'KW',310),(310,'KX',311),(311,'KY',312),(312,'KZ',313),(313,'LA',314),(314,'LB',315),(315,'LC',316),(316,'LD',317),(317,'LE',318),(318,'LF',319),(319,'LG',320),(320,'LH',321),(321,'LI',322),(322,'LJ',323),(323,'LK',324),(324,'LL',325),(325,'LM',326),(326,'LN',327),(327,'LO',328),(328,'LP',329),(329,'LQ',330),(330,'LR',331),(331,'LS',332),(332,'LT',333),(333,'LU',334),(334,'LV',335),(335,'LW',336),(336,'LX',337),(337,'LY',338),(338,'LZ',339),(339,'MA',340),(340,'MB',341),(341,'MC',342),(342,'MD',343),(343,'ME',344),(344,'MF',345),(345,'MG',346),(346,'MH',347),(347,'MI',348),(348,'MJ',349),(349,'MK',350),(350,'ML',351),(351,'MM',352),(352,'MN',353),(353,'MO',354),(354,'MP',355),(355,'MQ',356),(356,'MR',357),(357,'MS',358),(358,'MT',359),(359,'MU',360),(360,'MV',361),(361,'MW',362),(362,'MX',363),(363,'MY',364),(364,'MZ',365),(365,'NA',366),(366,'NB',367),(367,'NC',368),(368,'ND',369),(369,'NE',370),(370,'NF',371),(371,'NG',372),(372,'NH',373),(373,'NI',374),(374,'NJ',375),(375,'NK',376),(376,'NL',377),(377,'NM',378),(378,'NN',379),(379,'NO',380),(380,'NP',381),(381,'NQ',382),(382,'NR',383),(383,'NS',384),(384,'NT',385),(385,'NU',386),(386,'NV',387),(387,'NW',388),(388,'NX',389),(389,'NY',390),(390,'NZ',391),(391,'OA',392),(392,'OB',393),(393,'OC',394),(394,'OD',395),(395,'OE',396),(396,'OF',397),(397,'OG',398),(398,'OH',399),(399,'OI',400),(400,'OJ',401),(401,'OK',402),(402,'OL',403),(403,'OM',404),(404,'ON',405),(405,'OO',406),(406,'OP',407),(407,'OQ',408),(408,'OR',409),(409,'OS',410),(410,'OT',411),(411,'OU',412),(412,'OV',413),(413,'OW',414),(414,'OX',415),(415,'OY',416),(416,'OZ',417),(417,'PA',418),(418,'PB',419),(419,'PC',420),(420,'PD',421),(421,'PE',422),(422,'PF',423),(423,'PG',424),(424,'PH',425),(425,'PI',426),(426,'PJ',427),(427,'PK',428),(428,'PL',429),(429,'PM',430),(430,'PN',431),(431,'PO',432),(432,'PP',433),(433,'PQ',434),(434,'PR',435),(435,'PS',436),(436,'PT',437),(437,'PU',438),(438,'PV',439),(439,'PW',440),(440,'PX',441),(441,'PY',442),(442,'PZ',443),(443,'QA',444),(444,'QB',445),(445,'QC',446),(446,'QD',447),(447,'QE',448),(448,'QF',449),(449,'QG',450),(450,'QH',451),(451,'QI',452),(452,'QJ',453),(453,'QK',454),(454,'QL',455),(455,'QM',456),(456,'QN',457),(457,'QO',458),(458,'QP',459),(459,'QQ',460),(460,'QR',461),(461,'QS',462),(462,'QT',463),(463,'QU',464),(464,'QV',465),(465,'QW',466),(466,'QX',467),(467,'QY',468),(468,'QZ',469),(469,'RA',470),(470,'RB',471),(471,'RC',472),(472,'RD',473),(473,'RE',474),(474,'RF',475),(475,'RG',476),(476,'RH',477),(477,'RI',478),(478,'RJ',479),(479,'RK',480),(480,'RL',481),(481,'RM',482),(482,'RN',483),(483,'RO',484),(484,'RP',485),(485,'RQ',486),(486,'RR',487),(487,'RS',488),(488,'RT',489),(489,'RU',490),(490,'RV',491),(491,'RW',492),(492,'RX',493),(493,'RY',494),(494,'RZ',495),(495,'SA',496),(496,'SB',497),(497,'SC',498),(498,'SD',499),(499,'SE',500),(500,'SF',501),(501,'SG',502),(502,'SH',503),(503,'SI',504),(504,'SJ',505),(505,'SK',506),(506,'SL',507),(507,'SM',508),(508,'SN',509),(509,'SO',510),(510,'SP',511),(511,'SQ',512),(512,'SR',513),(513,'SS',514),(514,'ST',515),(515,'SU',516),(516,'SV',517),(517,'SW',518),(518,'SX',519),(519,'SY',520),(520,'SZ',521),(521,'TA',522),(522,'TB',523),(523,'TC',524),(524,'TD',525),(525,'TE',526),(526,'TF',527),(527,'TG',528),(528,'TH',529),(529,'TI',530),(530,'TJ',531),(531,'TK',532),(532,'TL',533),(533,'TM',534),(534,'TN',535),(535,'TO',536),(536,'TP',537),(537,'TQ',538),(538,'TR',539),(539,'TS',540),(540,'TT',541),(541,'TU',542),(542,'TV',543),(543,'TW',544),(544,'TX',545),(545,'TY',546),(546,'TZ',547),(547,'UA',548),(548,'UB',549),(549,'UC',550),(550,'UD',551),(551,'UE',552),(552,'UF',553),(553,'UG',554),(554,'UH',555),(555,'UI',556),(556,'UJ',557),(557,'UK',558),(558,'UL',559),(559,'UM',560),(560,'UN',561),(561,'UO',562),(562,'UP',563),(563,'UQ',564),(564,'UR',565),(565,'US',566),(566,'UT',567),(567,'UU',568),(568,'UV',569),(569,'UW',570),(570,'UX',571),(571,'UY',572),(572,'UZ',573),(573,'VA',574),(574,'VB',575),(575,'VC',576),(576,'VD',577),(577,'VE',578),(578,'VF',579),(579,'VG',580),(580,'VH',581),(581,'VI',582),(582,'VJ',583),(583,'VK',584),(584,'VL',585),(585,'VM',586),(586,'VN',587),(587,'VO',588),(588,'VP',589),(589,'VQ',590),(590,'VR',591),(591,'VS',592),(592,'VT',593),(593,'VU',594),(594,'VV',595),(595,'VW',596),(596,'VX',597),(597,'VY',598),(598,'VZ',599),(599,'WA',600),(600,'WB',601),(601,'WC',602),(602,'WD',603),(603,'WE',604),(604,'WF',605),(605,'WG',606),(606,'WH',607),(607,'WI',608),(608,'WJ',609),(609,'WK',610),(610,'WL',611),(611,'WM',612),(612,'WN',613),(613,'WO',614),(614,'WP',615),(615,'WQ',616),(616,'WR',617),(617,'WS',618),(618,'WT',619),(619,'WU',620),(620,'WV',621),(621,'WW',622),(622,'WX',623),(623,'WY',624),(624,'WZ',625),(625,'XA',626),(626,'XB',627),(627,'XC',628),(628,'XD',629),(629,'XE',630),(630,'XF',631),(631,'XG',632),(632,'XH',633),(633,'XI',634),(634,'XJ',635),(635,'XK',636),(636,'XL',637),(637,'XM',638),(638,'XN',639),(639,'XO',640),(640,'XP',641),(641,'XQ',642),(642,'XR',643),(643,'XS',644),(644,'XT',645),(645,'XU',646),(646,'XV',647),(647,'XW',648),(648,'XX',649),(649,'XY',650),(650,'XZ',651),(651,'YA',652),(652,'YB',653),(653,'YC',654),(654,'YD',655),(655,'YE',656),(656,'YF',657),(657,'YG',658),(658,'YH',659),(659,'YI',660),(660,'YJ',661),(661,'YK',662),(662,'YL',663),(663,'YM',664),(664,'YN',665),(665,'YO',666),(666,'YP',667),(667,'YQ',668),(668,'YR',669),(669,'YS',670),(670,'YT',671),(671,'YU',672),(672,'YV',673),(673,'YW',674),(674,'YX',675),(675,'YY',676),(676,'YZ',677),(677,'ZA',678),(678,'ZB',679),(679,'ZC',680),(680,'ZD',681),(681,'ZE',682),(682,'ZF',683),(683,'ZG',684),(684,'ZH',685),(685,'ZI',686),(686,'ZJ',687),(687,'ZK',688),(688,'ZL',689),(689,'ZM',690),(690,'ZN',691),(691,'ZO',692),(692,'ZP',693),(693,'ZQ',694),(694,'ZR',695),(695,'ZS',696),(696,'ZT',697),(697,'ZU',698),(698,'ZV',699),(699,'ZW',700),(700,'ZX',701),(701,'ZY',702),(702,'ZZ',703);
/*!40000 ALTER TABLE VersionControl ENABLE KEYS */;

--
-- Table structure for table users
--

DROP TABLE IF EXISTS users;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE users (
  username varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  enabled tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table users
--

/*!40000 ALTER TABLE users DISABLE KEYS */;
INSERT INTO users VALUES ('admin','$2a$10$KnRdXb09WIgf1gYwYAj/pO7mB7Rp0i0xejpncp2ZZnlqZW9sj4h/m',1);
/*!40000 ALTER TABLE users ENABLE KEYS */;
--
-- Table structure for table authorities
--

DROP TABLE IF EXISTS authorities;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  UNIQUE KEY ix_auth_username (username,authority),
  CONSTRAINT authorities_ibfk_1 FOREIGN KEY (username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table authorities
--

/*!40000 ALTER TABLE authorities DISABLE KEYS */;
INSERT INTO authorities VALUES 
('admin','USER'),
('admin','SUPERADMIN'),
('admin','ADMIN');

/*!40000 ALTER TABLE authorities ENABLE KEYS */;



/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-15  0:00:01

DROP TABLE IF EXISTS Log;
CREATE TABLE Log (
  ID int(11) NOT NULL AUTO_INCREMENT,
  ACTION_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  LEVEL varchar(50) NOT NULL DEFAULT 'INFO',
  USERNAME varchar(500) NOT NULL,
  DEVICE varchar(50),
  IP varchar(50),
  METHOD varchar(500),
  REQ_URL varchar(1000),
  STAND_ALONE_MODE BOOLEAN DEFAULT 0,
  PROTOCOL varchar(10),
  PARAMS varchar(2000),
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS AccountRequest;
CREATE TABLE AccountRequest (
  ID int(11) NOT NULL AUTO_INCREMENT,
  REQUESTED_ON TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  EMAIL varchar(500) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS AnonymousUser;
CREATE TABLE AnonymousUser (
  ID int(11) NOT NULL AUTO_INCREMENT,
  HASH varchar(1000) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;