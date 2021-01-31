-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2020 at 08:07 PM
-- Server version: 8.0.18
-- PHP Version: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ankete2`
--
DROP DATABASE IF EXISTS `ankete2`;
CREATE DATABASE IF NOT EXISTS `ankete2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `ankete2`;

-- --------------------------------------------------------

--
-- Table structure for table `anketa`
--

CREATE TABLE `anketa` (
  `idAnketa` int(11) NOT NULL,
  `naziv` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `datum_pocetka` date NOT NULL,
  `datum_kraja` date NOT NULL,
  `autor` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `anonimna` tinyint(4) NOT NULL COMMENT '0-nije,1-jeste',
  `broj_strana` smallint(6) NOT NULL DEFAULT '1',
  `blokirana` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `anketa`
--

INSERT INTO `anketa` (`idAnketa`, `naziv`, `datum_pocetka`, `datum_kraja`, `autor`, `anonimna`, `broj_strana`, `blokirana`) VALUES
(51, 'Anketa 1', '2020-09-02', '2021-03-12', 'bojan7777', 0, 3, 0),
(56, 'Predmeti', '2020-12-01', '2021-01-01', 'bojan7777', 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `anketa_pitanja`
--

CREATE TABLE `anketa_pitanja` (
  `idAnketa` int(11) NOT NULL,
  `idPitanja` int(11) NOT NULL,
  `obavezno` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-nije, 1-jeste',
  `red` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `anketa_pitanja`
--

INSERT INTO `anketa_pitanja` (`idAnketa`, `idPitanja`, `obavezno`, `red`) VALUES
(51, 1, 1, 3),
(51, 2, 1, 1),
(51, 4, 1, 2),
(51, 5, 1, 7),
(51, 6, 1, 5),
(51, 7, 1, 6),
(51, 46, 1, 4),
(56, 59, 1, 1),
(56, 60, 1, 2),
(56, 61, 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `autor`
--

CREATE TABLE `autor` (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `prezime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `jmbg` varchar(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `datum_rodjenja` date NOT NULL,
  `mesto_rodjenja` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `telefon` varchar(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `autor`
--

INSERT INTO `autor` (`username`, `ime`, `prezime`, `jmbg`, `datum_rodjenja`, `mesto_rodjenja`, `telefon`) VALUES
('bojan7777', 'Bojan', 'Selic', '0', '2019-08-17', 'Bg', '0');

-- --------------------------------------------------------

--
-- Table structure for table `ispitanik`
--

CREATE TABLE `ispitanik` (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `prezime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `jmbg` varchar(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `datum_rodjenja` date NOT NULL,
  `mesto_rodjenja` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `telefon` varchar(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `ispitanik`
--

INSERT INTO `ispitanik` (`username`, `ime`, `prezime`, `jmbg`, `datum_rodjenja`, `mesto_rodjenja`, `telefon`) VALUES
('petar4444', 'Petar', 'Jandric', '0706996710327', '1996-06-07', 'Beograd', '+381644475572');

-- --------------------------------------------------------

--
-- Table structure for table `ispitanik_anketa`
--

CREATE TABLE `ispitanik_anketa` (
  `idPopunjavanje` int(11) NOT NULL,
  `idAnketa` int(11) NOT NULL,
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `rezultat` json NOT NULL,
  `popunjena` int(11) NOT NULL COMMENT '0-nije , 1-popunio',
  `tipPopunjavanja` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0-anonimna, 1-personalizovana'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `ispitanik_anketa`
--

INSERT INTO `ispitanik_anketa` (`idPopunjavanje`, `idAnketa`, `username`, `rezultat`, `popunjena`, `tipPopunjavanja`) VALUES
(12, 51, 'petar4444', '[{\"ob\": \"1\", \"pp\": [{\"o\": \"1\", \"idPP\": \"1\", \"tekst_pp\": \"Potpitanje 1\"}, {\"o\": \"1\", \"idPP\": \"2\", \"tekst_pp\": \"Potpitanje 2\"}], \"tp\": \"1\", \"idP\": \"1\", \"tekst_p\": \"Pitanje 1\"}, {\"ob\": \"1\", \"pp\": [{\"o\": \"2\", \"idPP\": \"3\", \"tekst_pp\": \"Potpitanje 3\"}], \"tp\": \"2\", \"idP\": \"2\", \"tekst_p\": \"Pitanje 2\"}, {\"ob\": \"1\", \"pp\": [{\"o\": true, \"idPP\": \"10\", \"tekst_pp\": \"Signali\"}, {\"o\": false, \"idPP\": \"11\", \"tekst_pp\": \"RTI\"}], \"tp\": \"4\", \"idP\": \"4\", \"tekst_p\": \"Pitanje 4\"}, {\"ob\": \"1\", \"pp\": [{\"o\": true, \"idPP\": \"5\", \"tekst_pp\": \"Oceni Kvasceva\"}, {\"o\": null, \"idPP\": \"6\", \"tekst_pp\": \"Oceni Djurovica\"}], \"tp\": \"5\", \"idP\": \"5\", \"tekst_p\": \"Pitanje 5\"}, {\"ob\": \"1\", \"pp\": [{\"o\": [{\"val\": true, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": false, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"7\", \"tekst_pp\": \"Potpitanje 6\"}, {\"o\": [{\"val\": true, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": false, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"9\", \"tekst_pp\": \"Potpitanje 6-1\"}], \"tp\": \"6\", \"idP\": \"6\", \"tekst_p\": \"Pitanje 6\"}, {\"ob\": \"1\", \"pp\": [{\"o\": [{\"val\": true, \"idIzb\": \"8\", \"teskt_izb\": \"DA\"}, {\"val\": true, \"idIzb\": \"9\", \"teskt_izb\": \"NE\"}], \"idPP\": \"8\", \"tekst_pp\": \"Potpitanje 7\"}], \"tp\": \"7\", \"idP\": \"7\", \"tekst_p\": \"Pitanje 7\"}, {\"ob\": \"1\", \"pp\": [{\"o\": \"1\", \"idPP\": \"108\", \"tekst_pp\": \"Potpitanje 31\"}], \"tp\": \"1\", \"idP\": \"46\", \"tekst_p\": \"Pitanje 3\"}]', 1, 0),
(18, 51, 'bojan7777', '[{\"ob\": \"1\", \"pp\": [{\"o\": \"1\", \"idPP\": \"1\", \"tekst_pp\": \"Potpitanje 1\"}, {\"o\": \"1\", \"idPP\": \"2\", \"tekst_pp\": \"Potpitanje 2\"}], \"tp\": \"1\", \"idP\": \"1\", \"tekst_p\": \"Pitanje 1\"}, {\"ob\": \"1\", \"pp\": [{\"o\": \"3\", \"idPP\": \"3\", \"tekst_pp\": \"Potpitanje 3\"}], \"tp\": \"2\", \"idP\": \"2\", \"tekst_p\": \"Pitanje 2\"}, {\"ob\": \"1\", \"pp\": [{\"o\": true, \"idPP\": \"10\", \"tekst_pp\": \"Signali\"}, {\"o\": false, \"idPP\": \"11\", \"tekst_pp\": \"RTI\"}], \"tp\": \"4\", \"idP\": \"4\", \"tekst_p\": \"Pitanje 4\"}, {\"ob\": \"1\", \"pp\": [{\"o\": true, \"idPP\": \"5\", \"tekst_pp\": \"Oceni Kvasceva\"}, {\"o\": null, \"idPP\": \"6\", \"tekst_pp\": \"Oceni Djurovica\"}], \"tp\": \"5\", \"idP\": \"5\", \"tekst_p\": \"Pitanje 5\"}, {\"ob\": \"1\", \"pp\": [{\"o\": [{\"val\": true, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": false, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"7\", \"tekst_pp\": \"Potpitanje 6\"}, {\"o\": [{\"val\": false, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": true, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"9\", \"tekst_pp\": \"Potpitanje 6-1\"}], \"tp\": \"6\", \"idP\": \"6\", \"tekst_p\": \"Pitanje 6\"}, {\"ob\": \"1\", \"pp\": [{\"o\": [{\"val\": true, \"idIzb\": \"8\", \"teskt_izb\": \"DA\"}, {\"val\": null, \"idIzb\": \"9\", \"teskt_izb\": \"NE\"}], \"idPP\": \"8\", \"tekst_pp\": \"Potpitanje 7\"}], \"tp\": \"7\", \"idP\": \"7\", \"tekst_p\": \"Pitanje 7\"}, {\"ob\": \"1\", \"pp\": [{\"o\": \"5\", \"idPP\": \"108\", \"tekst_pp\": \"Potpitanje 31\"}], \"tp\": \"1\", \"idP\": \"46\", \"tekst_p\": \"Pitanje 3\"}]', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `izbor`
--

CREATE TABLE `izbor` (
  `idIzbora` int(11) NOT NULL,
  `idPitanja` int(11) NOT NULL,
  `izbor` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `izbor`
--

INSERT INTO `izbor` (`idIzbora`, `idPitanja`, `izbor`) VALUES
(1, 6, 'DA'),
(2, 6, 'NE'),
(8, 7, 'DA'),
(9, 7, 'NE'),
(51, 61, 'DA'),
(52, 61, 'NE');

-- --------------------------------------------------------

--
-- Table structure for table `korisnik`
--

CREATE TABLE `korisnik` (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `tip` tinyint(4) NOT NULL,
  `reg` tinyint(4) NOT NULL COMMENT '0-na cekanju,1-jeste'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `korisnik`
--

INSERT INTO `korisnik` (`username`, `password`, `email`, `tip`, `reg`) VALUES
('admin', 'e714f5e09b26f37bb36f63f24789a3b5', 'admin@etf.com', 4, 1),
('bojan7777', '8cc5af5f7516e429ff3c135e85eafc73', 'bojan@bojan.com', 3, 1),
('Luka1996', '8cc5af5f7516e429ff3c135e85eafc73', 'petar@petar.com', 2, 1),
('Pera', '8cc5af5f7516e429ff3c135e85eafc73', 'petar@petar.com', 2, 0),
('petar4444', '8cc5af5f7516e429ff3c135e85eafc73', 'petar@petar.com', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `listic_anketa`
--

CREATE TABLE `listic_anketa` (
  `idListic` int(11) NOT NULL,
  `idAnketa` int(11) NOT NULL,
  `ime` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `rezultat` json NOT NULL,
  `prezime` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `anonimna` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `listic_anketa`
--

INSERT INTO `listic_anketa` (`idListic`, `idAnketa`, `ime`, `rezultat`, `prezime`, `anonimna`) VALUES
(8, 51, 'Petar', '[{\"pp\": [{\"o\": \"1\", \"idPP\": \"1\", \"tekst_pp\": \"Potpitanje 1\"}, {\"o\": \"2\", \"idPP\": \"2\", \"tekst_pp\": \"Potpitanje 2\"}], \"tp\": \"1\", \"idP\": \"1\", \"tekst_p\": \"Pitanje 1\"}, {\"pp\": [{\"o\": \"aa\", \"idPP\": \"3\", \"tekst_pp\": \"Potpitanje 3\"}], \"tp\": \"2\", \"idP\": \"2\", \"tekst_p\": \"Pitanje 2\"}, {\"pp\": [{\"o\": false, \"idPP\": \"10\", \"tekst_pp\": \"Signali\"}, {\"o\": true, \"idPP\": \"11\", \"tekst_pp\": \"RTI\"}], \"tp\": \"4\", \"idP\": \"4\", \"tekst_p\": \"Pitanje 4\"}, {\"pp\": [{\"o\": true, \"idPP\": \"5\", \"tekst_pp\": \"Oceni Kvasceva\"}, {\"o\": null, \"idPP\": \"6\", \"tekst_pp\": \"Oceni Djurovica\"}], \"tp\": \"5\", \"idP\": \"5\", \"tekst_p\": \"Pitanje 5\"}, {\"pp\": [{\"o\": [{\"val\": false, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": true, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"7\", \"tekst_pp\": \"Potpitanje 6\"}, {\"o\": [{\"val\": false, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": true, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"9\", \"tekst_pp\": \"Potpitanje 6-1\"}], \"tp\": \"6\", \"idP\": \"6\", \"tekst_p\": \"Pitanje 6\"}, {\"pp\": [{\"o\": [{\"val\": null, \"idIzb\": \"8\", \"teskt_izb\": \"DA\"}, {\"val\": true, \"idIzb\": \"9\", \"teskt_izb\": \"NE\"}], \"idPP\": \"8\", \"tekst_pp\": \"Potpitanje 7\"}], \"tp\": \"7\", \"idP\": \"7\", \"tekst_p\": \"Pitanje 7\"}, {\"pp\": [{\"o\": \"3\", \"idPP\": \"108\", \"tekst_pp\": \"Potpitanje 31\"}], \"tp\": \"1\", \"idP\": \"46\", \"tekst_p\": \"Pitanje 3\"}]', 'Jandric', 0),
(9, 51, 'Petar', '[{\"pp\": [{\"o\": \"1\", \"idPP\": \"1\", \"tekst_pp\": \"Potpitanje 1\"}, {\"o\": \"2\", \"idPP\": \"2\", \"tekst_pp\": \"Potpitanje 2\"}], \"tp\": \"1\", \"idP\": \"1\", \"tekst_p\": \"Pitanje 1\"}, {\"pp\": [{\"o\": \"3\", \"idPP\": \"3\", \"tekst_pp\": \"Potpitanje 3\"}], \"tp\": \"2\", \"idP\": \"2\", \"tekst_p\": \"Pitanje 2\"}, {\"pp\": [{\"o\": true, \"idPP\": \"10\", \"tekst_pp\": \"Signali\"}, {\"o\": false, \"idPP\": \"11\", \"tekst_pp\": \"RTI\"}], \"tp\": \"4\", \"idP\": \"4\", \"tekst_p\": \"Pitanje 4\"}, {\"pp\": [{\"o\": true, \"idPP\": \"5\", \"tekst_pp\": \"Oceni Kvasceva\"}, {\"o\": null, \"idPP\": \"6\", \"tekst_pp\": \"Oceni Djurovica\"}], \"tp\": \"5\", \"idP\": \"5\", \"tekst_p\": \"Pitanje 5\"}, {\"pp\": [{\"o\": [{\"val\": true, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": false, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"7\", \"tekst_pp\": \"Potpitanje 6\"}, {\"o\": [{\"val\": true, \"idIzb\": \"1\", \"teskt_izb\": \"DA\"}, {\"val\": false, \"idIzb\": \"2\", \"teskt_izb\": \"NE\"}], \"idPP\": \"9\", \"tekst_pp\": \"Potpitanje 6-1\"}], \"tp\": \"6\", \"idP\": \"6\", \"tekst_p\": \"Pitanje 6\"}, {\"pp\": [{\"o\": [{\"val\": null, \"idIzb\": \"8\", \"teskt_izb\": \"DA\"}, {\"val\": true, \"idIzb\": \"9\", \"teskt_izb\": \"NE\"}], \"idPP\": \"8\", \"tekst_pp\": \"Potpitanje 7\"}], \"tp\": \"7\", \"idP\": \"7\", \"tekst_p\": \"Pitanje 7\"}, {\"pp\": [{\"o\": \"3\", \"idPP\": \"108\", \"tekst_pp\": \"Potpitanje 31\"}], \"tp\": \"1\", \"idP\": \"46\", \"tekst_p\": \"Pitanje 3\"}]', 'Jandric', 0);

-- --------------------------------------------------------

--
-- Table structure for table `pitanja`
--

CREATE TABLE `pitanja` (
  `idPitanja` int(11) NOT NULL,
  `tekst` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `tipOdgovora` tinyint(4) NOT NULL,
  `pom` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `pitanja`
--

INSERT INTO `pitanja` (`idPitanja`, `tekst`, `tipOdgovora`, `pom`) VALUES
(1, 'Pitanje 1', 1, 0),
(2, 'Pitanje 2', 2, 0),
(4, 'Pitanje 4', 4, 0),
(5, 'Pitanje 5', 5, 0),
(6, 'Pitanje 6', 6, 0),
(7, 'Pitanje 7', 7, 0),
(46, 'Pitanje 3', 1, 0),
(59, 'Ocene u 4.godini', 1, 0),
(60, 'Najtezi predmet u 4.godini?', 4, 0),
(61, 'Koristan predmet?', 6, 0);

-- --------------------------------------------------------

--
-- Table structure for table `potpitanja`
--

CREATE TABLE `potpitanja` (
  `idPotpitanja` int(11) NOT NULL,
  `idPitanja` int(11) NOT NULL,
  `potpitanje` varchar(80) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `redpotp` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `potpitanja`
--

INSERT INTO `potpitanja` (`idPotpitanja`, `idPitanja`, `potpitanje`, `redpotp`) VALUES
(1, 1, 'Potpitanje 1', 1),
(2, 1, 'Potpitanje 2', 2),
(3, 2, 'Potpitanje 3', 1),
(5, 5, 'Oceni Kvasceva', 1),
(6, 5, 'Oceni Djurovica', 2),
(7, 6, 'Potpitanje 6', 1),
(8, 7, 'Potpitanje 7', 1),
(9, 6, 'Potpitanje 6-1', 2),
(10, 4, 'Signali', 1),
(11, 4, 'RTI', 2),
(108, 46, 'Potpitanje 31', 1),
(121, 59, 'VI', 1),
(122, 59, 'UIP', 2),
(123, 59, 'PO', 3),
(124, 60, 'UIP', 1),
(125, 60, 'VI', 2),
(126, 60, 'TRS', 3),
(127, 61, 'PO', 1),
(128, 61, 'UIP', 2),
(129, 61, 'NSU', 3);

-- --------------------------------------------------------

--
-- Table structure for table `sluzbenik`
--

CREATE TABLE `sluzbenik` (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `prezime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `jmbg` varchar(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `datum_rodjenja` date NOT NULL,
  `mesto_rodjenja` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `telefon` varchar(13) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `sluzbenik`
--

INSERT INTO `sluzbenik` (`username`, `ime`, `prezime`, `jmbg`, `datum_rodjenja`, `mesto_rodjenja`, `telefon`) VALUES
('Luka1996', 'Luka', 'Lukic', '0706996710327', '1996-06-07', 'Beograd', '+381644475572'),
('Pera', 'Pera', 'Detlic', '0706996710327', '1996-06-07', 'Beograd', '+381644475572');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anketa`
--
ALTER TABLE `anketa`
  ADD PRIMARY KEY (`idAnketa`),
  ADD UNIQUE KEY `naziv` (`naziv`),
  ADD KEY `autor` (`autor`);

--
-- Indexes for table `anketa_pitanja`
--
ALTER TABLE `anketa_pitanja`
  ADD UNIQUE KEY `idAnketa` (`idAnketa`,`idPitanja`),
  ADD KEY `idPitanja` (`idPitanja`);

--
-- Indexes for table `autor`
--
ALTER TABLE `autor`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `ispitanik`
--
ALTER TABLE `ispitanik`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `ispitanik_anketa`
--
ALTER TABLE `ispitanik_anketa`
  ADD PRIMARY KEY (`idPopunjavanje`),
  ADD KEY `idAnketa` (`idAnketa`),
  ADD KEY `ispitanik_anketa_ibfk_1` (`username`);

--
-- Indexes for table `izbor`
--
ALTER TABLE `izbor`
  ADD PRIMARY KEY (`idIzbora`),
  ADD KEY `izbor_ibfk_1` (`idPitanja`);

--
-- Indexes for table `korisnik`
--
ALTER TABLE `korisnik`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `listic_anketa`
--
ALTER TABLE `listic_anketa`
  ADD PRIMARY KEY (`idListic`);

--
-- Indexes for table `pitanja`
--
ALTER TABLE `pitanja`
  ADD PRIMARY KEY (`idPitanja`);

--
-- Indexes for table `potpitanja`
--
ALTER TABLE `potpitanja`
  ADD PRIMARY KEY (`idPotpitanja`),
  ADD KEY `idPitanja` (`idPitanja`);

--
-- Indexes for table `sluzbenik`
--
ALTER TABLE `sluzbenik`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `anketa`
--
ALTER TABLE `anketa`
  MODIFY `idAnketa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `ispitanik_anketa`
--
ALTER TABLE `ispitanik_anketa`
  MODIFY `idPopunjavanje` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `izbor`
--
ALTER TABLE `izbor`
  MODIFY `idIzbora` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `listic_anketa`
--
ALTER TABLE `listic_anketa`
  MODIFY `idListic` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `pitanja`
--
ALTER TABLE `pitanja`
  MODIFY `idPitanja` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `potpitanja`
--
ALTER TABLE `potpitanja`
  MODIFY `idPotpitanja` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=130;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `anketa`
--
ALTER TABLE `anketa`
  ADD CONSTRAINT `anketa_ibfk_1` FOREIGN KEY (`autor`) REFERENCES `autor` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `anketa_pitanja`
--
ALTER TABLE `anketa_pitanja`
  ADD CONSTRAINT `anketa_pitanja_ibfk_1` FOREIGN KEY (`idAnketa`) REFERENCES `anketa` (`idAnketa`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `anketa_pitanja_ibfk_2` FOREIGN KEY (`idPitanja`) REFERENCES `pitanja` (`idPitanja`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `autor`
--
ALTER TABLE `autor`
  ADD CONSTRAINT `autor_ibfk_1` FOREIGN KEY (`username`) REFERENCES `korisnik` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ispitanik`
--
ALTER TABLE `ispitanik`
  ADD CONSTRAINT `ispitanik_ibfk_1` FOREIGN KEY (`username`) REFERENCES `korisnik` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ispitanik_anketa`
--
ALTER TABLE `ispitanik_anketa`
  ADD CONSTRAINT `ispitanik_anketa_ibfk_1` FOREIGN KEY (`username`) REFERENCES `korisnik` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ispitanik_anketa_ibfk_2` FOREIGN KEY (`idAnketa`) REFERENCES `anketa` (`idAnketa`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `izbor`
--
ALTER TABLE `izbor`
  ADD CONSTRAINT `izbor_ibfk_1` FOREIGN KEY (`idPitanja`) REFERENCES `pitanja` (`idPitanja`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `potpitanja`
--
ALTER TABLE `potpitanja`
  ADD CONSTRAINT `potpitanja_ibfk_1` FOREIGN KEY (`idPitanja`) REFERENCES `pitanja` (`idPitanja`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sluzbenik`
--
ALTER TABLE `sluzbenik`
  ADD CONSTRAINT `sluzbenik_ibfk_1` FOREIGN KEY (`username`) REFERENCES `korisnik` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
