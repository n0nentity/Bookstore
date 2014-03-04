-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 04. Mrz 2014 um 18:01
-- Server Version: 5.6.16
-- PHP-Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `book`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `book`
--

CREATE TABLE IF NOT EXISTS `book` (
  `uuid` varchar(1000) NOT NULL,
  `title` varchar(1000) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `book`
--

INSERT INTO `book` (`uuid`, `title`, `quantity`) VALUES
('c6592b7f-de32-4d59-b47a-24bd0fccea51', '"Bibel"', 1),
('95ee3c22-a1a8-480a-9c7c-d1248b9beef5', '"Hamlet"', 1),
('692ba891-2633-4723-895d-3ca47b3b87de', '"Die Odyssee"', 1),
('da9c06b0-2aca-45e0-b106-1f3d3be1ed58', '"Homo faber"', 1),
('9ad9ab09-bd22-490e-b730-494c2754322a', '"Der Herr der Ringe"', 1),
('bb53b891-b1cc-48f3-9648-f3fb9f75ca4e', '"Das Parfum"', 1),
('46bf7f27-511c-4ccf-a7c7-645f1a9b358c', '"Algorithmen und Datenstrukturen"', 1),
('a6c45309-b605-40f6-ba87-0db68925d60a', '"C von A bis Z"', 1),
('3de961f7-dc5d-4c3e-bca9-9037f180583b', '"Mathematik für Ingenieure und Naturwissenschaftler"', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
