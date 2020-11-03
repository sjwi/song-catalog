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

INSERT INTO SongCategories (NAME) values ('CWM'),('Hymns'),('Seasonal');


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
INSERT INTO AddressBook VALUES (2,'','Adams','James','jamesadams@test.com','(999) 999-9999');
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
INSERT INTO AddressBookGroup VALUES (1,'TEAM 1');
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
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table Organizations
--

/*!40000 ALTER TABLE Organizations DISABLE KEYS */;
INSERT INTO Organizations VALUES (1,'Test Org','Test Org');
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
  FILE LONGBLOB DEFAULT NULL,
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
INSERT INTO Services VALUES (1,'Church');
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

/*!40000 ALTER TABLE SetListArchive DISABLE KEYS */;
/*!40000 ALTER TABLE SetListArchive ENABLE KEYS */;


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
  ORGANIZATION int(11) DEFAULT NULL,
  CREATED_BY varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table SetListMaster
--

/*!40000 ALTER TABLE SetListMaster DISABLE KEYS */;
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
  FREQUENCY int(11) DEFAULT NULL,
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

/*!40000 ALTER TABLE Song ENABLE KEYS */;

-- Table structure for table SongArchive

DROP TABLE IF EXISTS SongArchive;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE SongArchive (
  ID int(11) NOT NULL,
  NAME varchar(255) DEFAULT NULL,
  BODY longtext,
  FREQUENCY int(11) DEFAULT NULL,
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