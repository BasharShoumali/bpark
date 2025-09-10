-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bpark
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `parkinghistory`
--

DROP TABLE IF EXISTS `parkinghistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parkinghistory` (
  `ParkingNumber` int NOT NULL,
  `ParkingCar` varchar(50) DEFAULT NULL,
  `SubscriptionNumber` int DEFAULT NULL,
  `ParkingDate` date DEFAULT NULL,
  `ParkingDuration` varchar(50) DEFAULT NULL,
  `StartTime` time DEFAULT NULL,
  `EndTime` time DEFAULT NULL,
  PRIMARY KEY (`ParkingNumber`),
  KEY `SubscriptionNumber` (`SubscriptionNumber`),
  CONSTRAINT `parkinghistory_ibfk_1` FOREIGN KEY (`SubscriptionNumber`) REFERENCES `users` (`SubscriptionNumber`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parkinghistory`
--

LOCK TABLES `parkinghistory` WRITE;
/*!40000 ALTER TABLE `parkinghistory` DISABLE KEYS */;
INSERT INTO `parkinghistory` VALUES (1,'Toyota',2001,'2025-05-01','2 hours','08:00:00','10:00:00'),(2,'Honda',2002,'2025-05-02','3 hours','09:00:00','12:00:00'),(3,'Ford',2001,'2025-05-03','1.5 hours','10:00:00','11:30:00'),(4,'Mazda',2001,'2025-05-04','2 hours','12:00:00','14:00:00'),(5,'BMW',2001,'2025-05-05','2.5 hours','15:00:00','17:30:00'),(6,'Chevy',2001,'2025-05-06','3 hours','09:00:00','12:00:00'),(7,'Tesla',2001,'2025-05-07','1 hour','08:00:00','09:00:00');
/*!40000 ALTER TABLE `parkinghistory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-08 22:24:44
