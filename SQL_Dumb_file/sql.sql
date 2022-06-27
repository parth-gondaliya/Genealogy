-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: test_db
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `divorce`
--

DROP TABLE IF EXISTS `divorce`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `divorce` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `partner1` int NOT NULL,
  `partner2` int NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `partner1` (`partner1`),
  KEY `partner2` (`partner2`),
  CONSTRAINT `divorce_ibfk_1` FOREIGN KEY (`partner1`) REFERENCES `person` (`PersonId`),
  CONSTRAINT `divorce_ibfk_2` FOREIGN KEY (`partner2`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `divorce`
--

LOCK TABLES `divorce` WRITE;
/*!40000 ALTER TABLE `divorce` DISABLE KEYS */;
INSERT INTO `divorce` VALUES (1,4,5);
/*!40000 ALTER TABLE `divorce` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_attributes`
--

DROP TABLE IF EXISTS `file_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_attributes` (
  `fileId` int DEFAULT NULL,
  `keyOfAttribute` varchar(255) NOT NULL,
  `valueOfAttribute` varchar(255) DEFAULT NULL,
  KEY `fileId` (`fileId`),
  CONSTRAINT `file_attributes_ibfk_1` FOREIGN KEY (`fileId`) REFERENCES `files` (`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_attributes`
--

LOCK TABLES `file_attributes` WRITE;
/*!40000 ALTER TABLE `file_attributes` DISABLE KEYS */;
INSERT INTO `file_attributes` VALUES (4,'state','gujarat'),(4,'no','3');
/*!40000 ALTER TABLE `file_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `files` (
  `fileId` int NOT NULL AUTO_INCREMENT,
  `fileLocation` varchar(250) NOT NULL,
  `date` varchar(50) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`fileLocation`),
  KEY `fileId` (`fileId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (6,'AWS_10',NULL,NULL),(7,'AWS_11','2000-02-02','USA'),(8,'AWS_12','1998-02-18',NULL),(9,'AWS_13','1998-12','USA'),(10,'AWS_14','1998',NULL),(1,'AWS_6','1998-10-21',NULL),(2,'AWS_7','2016','USA'),(4,'AWS_8','1998-12','USA'),(11,'AWS_85',NULL,NULL),(5,'AWS_9',NULL,NULL),(3,'inside json bag','2017','USA');
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filewithperson`
--

DROP TABLE IF EXISTS `filewithperson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filewithperson` (
  `fileId` int NOT NULL,
  `PersonId` int NOT NULL,
  KEY `fileId` (`fileId`),
  KEY `PersonId` (`PersonId`),
  CONSTRAINT `filewithperson_ibfk_1` FOREIGN KEY (`fileId`) REFERENCES `files` (`fileId`),
  CONSTRAINT `filewithperson_ibfk_2` FOREIGN KEY (`PersonId`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filewithperson`
--

LOCK TABLES `filewithperson` WRITE;
/*!40000 ALTER TABLE `filewithperson` DISABLE KEYS */;
INSERT INTO `filewithperson` VALUES (8,12),(8,13),(8,14),(8,15),(8,16),(7,16),(8,18),(8,18),(6,16),(3,25),(5,26),(11,26);
/*!40000 ALTER TABLE `filewithperson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marriage`
--

DROP TABLE IF EXISTS `marriage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marriage` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `partner1` int NOT NULL,
  `partner2` int NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `partner1` (`partner1`),
  KEY `partner2` (`partner2`),
  CONSTRAINT `marriage_ibfk_1` FOREIGN KEY (`partner1`) REFERENCES `person` (`PersonId`),
  CONSTRAINT `marriage_ibfk_2` FOREIGN KEY (`partner2`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marriage`
--

LOCK TABLES `marriage` WRITE;
/*!40000 ALTER TABLE `marriage` DISABLE KEYS */;
INSERT INTO `marriage` VALUES (3,2,3);
/*!40000 ALTER TABLE `marriage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parent`
--

DROP TABLE IF EXISTS `parent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parent` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `ParentId` int NOT NULL,
  `ChildId` int NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `ParentId` (`ParentId`),
  KEY `ChildId` (`ChildId`),
  CONSTRAINT `parent_ibfk_1` FOREIGN KEY (`ParentId`) REFERENCES `person` (`PersonId`),
  CONSTRAINT `parent_ibfk_2` FOREIGN KEY (`ChildId`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parent`
--

LOCK TABLES `parent` WRITE;
/*!40000 ALTER TABLE `parent` DISABLE KEYS */;
INSERT INTO `parent` VALUES (1,3,8),(2,12,13),(3,13,14),(4,14,16),(5,14,17),(6,14,18),(7,19,14),(8,19,20),(9,20,21),(10,21,22),(11,20,23),(12,23,24),(13,27,20),(14,12,15),(15,15,25),(16,25,26),(17,27,28),(18,28,29);
/*!40000 ALTER TABLE `parent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `PersonId` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`PersonId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'swatiii'),(2,'swatiii'),(3,'parth'),(4,'bakari1'),(5,'bakari2'),(6,'bakari3'),(7,'bakari4'),(8,'keyur1'),(9,'keyur2'),(10,'keyur3'),(11,'keyur3'),(12,'a'),(13,'b'),(14,'c'),(15,'d'),(16,'e'),(17,'f'),(18,'g'),(19,'h'),(20,'i'),(21,'j'),(22,'k'),(23,'l'),(24,'m'),(25,'n'),(26,'o'),(27,'p'),(28,'q'),(29,'r'),(30,'s');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attributes`
--

DROP TABLE IF EXISTS `person_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_attributes` (
  `PersonId` int DEFAULT NULL,
  `keyOfAttribute` varchar(255) DEFAULT NULL,
  `valueOfAttribute` varchar(255) DEFAULT NULL,
  KEY `PersonId` (`PersonId`),
  CONSTRAINT `person_attributes_ibfk_1` FOREIGN KEY (`PersonId`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attributes`
--

LOCK TABLES `person_attributes` WRITE;
/*!40000 ALTER TABLE `person_attributes` DISABLE KEYS */;
INSERT INTO `person_attributes` VALUES (3,'ocupation','public speaking'),(3,'phone','iphone14'),(3,'wife','swati');
/*!40000 ALTER TABLE `person_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_note`
--

DROP TABLE IF EXISTS `person_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_note` (
  `PersonId` int NOT NULL,
  `Note` varchar(255) NOT NULL,
  KEY `PersonId` (`PersonId`),
  CONSTRAINT `person_note_ibfk_1` FOREIGN KEY (`PersonId`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_note`
--

LOCK TABLES `person_note` WRITE;
/*!40000 ALTER TABLE `person_note` DISABLE KEYS */;
INSERT INTO `person_note` VALUES (3,'first note');
/*!40000 ALTER TABLE `person_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_reference`
--

DROP TABLE IF EXISTS `person_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_reference` (
  `PersonId` int NOT NULL,
  `refer` varchar(255) NOT NULL,
  KEY `PersonId` (`PersonId`),
  CONSTRAINT `person_reference_ibfk_1` FOREIGN KEY (`PersonId`) REFERENCES `person` (`PersonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_reference`
--

LOCK TABLES `person_reference` WRITE;
/*!40000 ALTER TABLE `person_reference` DISABLE KEYS */;
INSERT INTO `person_reference` VALUES (3,'first reference');
/*!40000 ALTER TABLE `person_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `fileId` int NOT NULL AUTO_INCREMENT,
  `tag` varchar(250) DEFAULT NULL,
  KEY `fileId` (`fileId`),
  CONSTRAINT `tags_ibfk_1` FOREIGN KEY (`fileId`) REFERENCES `files` (`fileId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (3,'tag1'),(3,'jaimis first tag'),(3,'sona\'s reer tag'),(1,'jaimi second refer tag'),(3,'sona\'s sec reer tag'),(2,'jamii thrid tag'),(8,'la'),(9,'s\'s reer tag'),(10,'jrefer tag'),(1,'soreer tag'),(9,'jatag'),(8,'la'),(8,'la'),(8,'la'),(8,'la'),(8,'la'),(9,'asd'),(10,'asd'),(2,'asd'),(8,'la'),(9,'asd'),(6,'asd'),(4,'lalalal'),(4,'tag1'),(4,'parth');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-14 18:44:04
