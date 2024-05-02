-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 02-05-2024 a las 12:39:07
-- Versión del servidor: 8.2.0
-- Versión de PHP: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `agencia`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `is_removed` bit(1) DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `nif` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `surname` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_fbbrqvnknokaylxj4pcawy25p` (`nif`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `client`
--

INSERT INTO `client` (`id`, `email`, `is_removed`, `name`, `nif`, `surname`) VALUES
(1, 'john.doe@example.com', b'0', 'John', '12345678A', 'Doe'),
(2, 'jane.doe@example.com', b'0', 'Jane', '23456789B', 'Doe'),
(3, 'michael.smith@example.com', b'0', 'Michael', '34567890C', 'Smith'),
(4, 'emily.johnson@example.com', b'0', 'Emily', '45678901D', 'Johnson'),
(5, 'william.brown@example.com', b'0', 'William', '56789012E', 'Brown'),
(6, 'olivia.williams@example.com', b'0', 'Olivia', '67890123F', 'Williams'),
(7, 'james.jones@example.com', b'0', 'James', '78901234G', 'Jones'),
(8, 'emma.taylor@example.com', b'0', 'Emma', '89012345H', 'Taylor'),
(9, 'benjamin.evans@example.com', b'0', 'Benjamin', '90123456I', 'Evans'),
(10, 'sophia.thomas@example.com', b'0', 'Sophia', '01234567J', 'Thomas');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight`
--

DROP TABLE IF EXISTS `flight`;
CREATE TABLE IF NOT EXISTS `flight` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available_seats` int NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `departure_date` date NOT NULL,
  `destination` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `is_removed` bit(1) DEFAULT NULL,
  `origin` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mh8wxtmpqqb2nydefbnnivlmi` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `flight`
--

INSERT INTO `flight` (`id`, `available_seats`, `code`, `departure_date`, `destination`, `is_removed`, `origin`, `price`) VALUES
(1, 150, 'NYBAR15062024', '2024-06-01', 'Barcelona', NULL, 'New York', 500.00),
(2, 200, 'TOPAR02062024', '2024-06-02', 'Paris', NULL, 'Tokyo', 700.00),
(3, 180, 'MADLOS03062024', '2024-06-03', 'Los Angeles', NULL, 'Madrid', 600.00),
(4, 170, 'BARRON04062024', '2024-06-04', 'Rome', NULL, 'Barcelona', 400.00),
(5, 190, 'LONSYD05062024', '2024-06-05', 'Sydney', NULL, 'London', 800.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight_booking`
--

DROP TABLE IF EXISTS `flight_booking`;
CREATE TABLE IF NOT EXISTS `flight_booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `seat_type` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `client_id` bigint NOT NULL,
  `flight_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9hw3hp3gu9rnxtcsnlrp4k2s7` (`client_id`),
  KEY `FK3uiklmjy1d7ba6rrjp6iq08kq` (`flight_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `flight_booking`
--

INSERT INTO `flight_booking` (`id`, `code`, `seat_type`, `client_id`, `flight_id`) VALUES
(1, 'RESV001', 'Economy', 1, 1),
(2, 'RESV002', 'Business', 2, 1),
(3, 'RESV003', 'Economy', 3, 2),
(4, 'RESV004', 'Economy', 4, 2),
(5, 'RESV005', 'First Class', 5, 3),
(6, 'RESV006', 'Economy', 6, 3),
(7, 'RESV007', 'Economy', 7, 4),
(8, 'RESV008', 'Business', 8, 4),
(9, 'RESV009', 'Economy', 9, 5),
(10, 'RESV010', 'Economy', 10, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel`
--

DROP TABLE IF EXISTS `hotel`;
CREATE TABLE IF NOT EXISTS `hotel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `code` varchar(8) COLLATE utf8mb4_spanish_ci NOT NULL,
  `is_removed` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1xn7ab34hsjhogmpsi8aqh1x4` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `hotel`
--

INSERT INTO `hotel` (`id`, `city`, `code`, `is_removed`, `name`) VALUES
(1, 'Barcelona', 'HOBA0001', NULL, 'Hotel Catalunya'),
(2, 'Madrid', 'HOMA0001', NULL, 'Hotel Reina Sofia'),
(3, 'Paris', 'HOPA0001', NULL, 'Hotel Eiffel'),
(4, 'New York', 'HONY0001', NULL, 'Times Square Hotel'),
(5, 'Tokyo', 'HOTO0001', NULL, 'Sakura Hotel');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `room`
--

DROP TABLE IF EXISTS `room`;
CREATE TABLE IF NOT EXISTS `room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available_from` date NOT NULL,
  `available_to` date NOT NULL,
  `code` varchar(11) COLLATE utf8mb4_spanish_ci NOT NULL,
  `is_removed` bit(1) DEFAULT NULL,
  `price` decimal(7,2) NOT NULL,
  `type` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `hotel_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdosq3ww4h9m2osim6o0lugng8` (`hotel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `room`
--

INSERT INTO `room` (`id`, `available_from`, `available_to`, `code`, `is_removed`, `price`, `type`, `hotel_id`) VALUES
(1, '2024-05-10', '2024-05-15', 'HOBA0001I01', NULL, 100.00, 'Individual', 1),
(2, '2024-05-10', '2024-05-15', 'HOBA0001I02', NULL, 150.00, 'Individual', 1),
(3, '2024-05-10', '2024-05-15', 'HOBA0001S01', NULL, 200.00, 'Suite', 1),
(4, '2024-05-10', '2024-05-15', 'HOMA0001I01', NULL, 120.00, 'Individual', 2),
(5, '2024-05-10', '2024-05-15', 'HOMA0001D01', NULL, 180.00, 'Doble', 2),
(6, '2024-05-10', '2024-05-15', 'HOMA0001S01', NULL, 220.00, 'Suite', 2),
(7, '2024-05-10', '2024-05-15', 'HOPA0001I01', NULL, 110.00, 'Individual', 3),
(8, '2024-05-10', '2024-05-15', 'HOPA0001D01', NULL, 160.00, 'Doble', 3),
(9, '2024-05-10', '2024-05-15', 'HOPA0001S01', NULL, 210.00, 'Suite', 3),
(10, '2024-05-10', '2024-05-15', 'HOPA0001I01', NULL, 130.00, 'Individual', 4),
(11, '2024-05-10', '2024-05-15', 'HONY0001D01', NULL, 170.00, 'Doble', 4),
(12, '2024-05-10', '2024-05-15', 'HONY0001S01', NULL, 230.00, 'Suite', 4),
(13, '2024-05-10', '2024-05-15', 'HONY0001I01', NULL, 140.00, 'Individual', 5),
(14, '2024-05-10', '2024-05-15', 'HOTO0001D01', NULL, 190.00, 'Doble', 5),
(15, '2024-05-10', '2024-05-15', 'HOTO0001S01', NULL, 250.00, 'Suite', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `room_booking`
--

DROP TABLE IF EXISTS `room_booking`;
CREATE TABLE IF NOT EXISTS `room_booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiwt0ws97ta91ukd4xonewjbxl` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `room_booking_client`
--

DROP TABLE IF EXISTS `room_booking_client`;
CREATE TABLE IF NOT EXISTS `room_booking_client` (
  `room_booking_id` bigint NOT NULL,
  `client_id` bigint NOT NULL,
  KEY `FKjqstdfl7kfguaprlrydybkuds` (`client_id`),
  KEY `FKjx8ivy7bl3ucuq6k8b894weeh` (`room_booking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `flight_booking`
--
ALTER TABLE `flight_booking`
  ADD CONSTRAINT `FK3uiklmjy1d7ba6rrjp6iq08kq` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`id`),
  ADD CONSTRAINT `FK9hw3hp3gu9rnxtcsnlrp4k2s7` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`);

--
-- Filtros para la tabla `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `FKdosq3ww4h9m2osim6o0lugng8` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`);

--
-- Filtros para la tabla `room_booking`
--
ALTER TABLE `room_booking`
  ADD CONSTRAINT `FKiwt0ws97ta91ukd4xonewjbxl` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`);

--
-- Filtros para la tabla `room_booking_client`
--
ALTER TABLE `room_booking_client`
  ADD CONSTRAINT `FKjqstdfl7kfguaprlrydybkuds` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  ADD CONSTRAINT `FKjx8ivy7bl3ucuq6k8b894weeh` FOREIGN KEY (`room_booking_id`) REFERENCES `room_booking` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
