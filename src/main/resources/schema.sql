DROP DATABASE IF EXISTS libreria_v04;
CREATE DATABASE libreria_v04;
USE libreria_v04;

-- Resto de las instrucciones para crear tablas y esquemas dentro de la base de datos.

--
-- Table structure for table `foto`
--

DROP TABLE IF EXISTS `foto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `foto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contenido` mediumblob,
  `mime` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `editorial`
--

DROP TABLE IF EXISTS `editorial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `editorial` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ALTA` tinyint(1) DEFAULT '0',
  `NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `autor`
--

DROP TABLE IF EXISTS `autor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `autor` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ALTA` tinyint(1) DEFAULT '0',
  `NOMBRE` varchar(255) DEFAULT NULL,
  `BIOGRAFIA` varchar(1409) DEFAULT NULL,
  `FOTO_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK4elq9keq83xwnbdcjv8chh6kx` (`FOTO_ID`),
  CONSTRAINT `FK4elq9keq83xwnbdcjv8chh6kx` FOREIGN KEY (`FOTO_ID`) REFERENCES `foto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `libro`
--

DROP TABLE IF EXISTS `libro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libro` (
  `ISBN` bigint NOT NULL,
  `ALTA` tinyint(1) DEFAULT '1',
  `ANIO` int DEFAULT NULL,
  `EJEMDISP` int DEFAULT NULL,
  `EJEMPREST` int DEFAULT NULL,
  `TITULO` varchar(255) DEFAULT NULL,
  `AUTOR_ID` bigint DEFAULT NULL,
  `EDITORIAL_ID` bigint DEFAULT NULL,
  `RESUMEN` varchar(1500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `FOTO_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`ISBN`),
  KEY `FK_LIBRO_EDITORIAL_ID` (`EDITORIAL_ID`),
  KEY `FK_LIBRO_AUTOR_ID` (`AUTOR_ID`),
  KEY `FKdstl77x96a5cxo5sc01afalfb` (`FOTO_ID`),
  CONSTRAINT `FK_LIBRO_AUTOR_ID` FOREIGN KEY (`AUTOR_ID`) REFERENCES `autor` (`ID`),
  CONSTRAINT `FK_LIBRO_EDITORIAL_ID` FOREIGN KEY (`EDITORIAL_ID`) REFERENCES `editorial` (`ID`),
  CONSTRAINT `FKdstl77x96a5cxo5sc01afalfb` FOREIGN KEY (`FOTO_ID`) REFERENCES `foto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `DNI` varchar(255) DEFAULT NULL,
  `NOMBRE` varchar(255) DEFAULT NULL,
  `APELLIDOS` varchar(255) DEFAULT NULL,
  `SEXO` varchar(10) DEFAULT NULL,
  `TELEFONO` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `CLAVE` varchar(255) DEFAULT NULL,
  `CATEGORIA` varchar(45) DEFAULT 'Usuario',
  `ALTA` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prestamo`
--

DROP TABLE IF EXISTS `prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamo` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `FECHAPREST` date DEFAULT NULL,
  `FECHADEVOL` date DEFAULT NULL,
  `CLIENTE_ID` int DEFAULT NULL,
  `LIBRO_ISBN` bigint DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRESTAMO_LIBRO_ISBN` (`LIBRO_ISBN`),
  KEY `FK_PRESTAMO_CLIENTE_ID` (`CLIENTE_ID`),
  CONSTRAINT `FK_PRESTAMO_CLIENTE_ID` FOREIGN KEY (`CLIENTE_ID`) REFERENCES `cliente` (`ID`),
  CONSTRAINT `FK_PRESTAMO_LIBRO_ISBN` FOREIGN KEY (`LIBRO_ISBN`) REFERENCES `libro` (`ISBN`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



