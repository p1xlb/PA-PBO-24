-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 28, 2024 at 03:08 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `PBO_PA`
--

-- --------------------------------------------------------

--
-- Table structure for table `DigitalGames`
--

CREATE TABLE `DigitalGames` (
  `ItemId` varchar(5) NOT NULL,
  `DownloadSize` double(3,2) NOT NULL,
  `Platform` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `GameItems`
--

CREATE TABLE `GameItems` (
  `ItemId` varchar(5) NOT NULL,
  `ItemName` varchar(255) NOT NULL,
  `Year` int(4) NOT NULL,
  `Stock` int(5) NOT NULL,
  `Price` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `GameVouchers`
--

CREATE TABLE `GameVouchers` (
  `ItemId` varchar(5) NOT NULL,
  `MarketPlatform` varchar(50) NOT NULL,
  `Quantity` int(50) NOT NULL,
  `ValidUntil` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Merchandise`
--

CREATE TABLE `Merchandise` (
  `ItemId` varchar(5) NOT NULL,
  `Game` varchar(155) NOT NULL,
  `Type` varchar(155) NOT NULL,
  `Dimension` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PhysicalGames`
--

CREATE TABLE `PhysicalGames` (
  `ItemId` varchar(5) NOT NULL,
  `Edition` varchar(155) NOT NULL,
  `Platform` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `TransactionItems`
--

CREATE TABLE `TransactionItems` (
  `TransactionId` int(5) NOT NULL,
  `ItemId` varchar(5) NOT NULL,
  `Quantity` int(4) NOT NULL,
  `TotalPrice` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Transactions`
--

CREATE TABLE `Transactions` (
  `TransactionId` int(5) NOT NULL,
  `TransactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `DigitalGames`
--
ALTER TABLE `DigitalGames`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `GameItems`
--
ALTER TABLE `GameItems`
  ADD PRIMARY KEY (`ItemId`);

--
-- Indexes for table `GameVouchers`
--
ALTER TABLE `GameVouchers`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `Merchandise`
--
ALTER TABLE `Merchandise`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `PhysicalGames`
--
ALTER TABLE `PhysicalGames`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `TransactionItems`
--
ALTER TABLE `TransactionItems`
  ADD KEY `TransactionId` (`TransactionId`),
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `Transactions`
--
ALTER TABLE `Transactions`
  ADD PRIMARY KEY (`TransactionId`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `DigitalGames`
--
ALTER TABLE `DigitalGames`
  ADD CONSTRAINT `DigitalGames_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `GameItems` (`ItemId`);

--
-- Constraints for table `GameVouchers`
--
ALTER TABLE `GameVouchers`
  ADD CONSTRAINT `GameVouchers_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `GameItems` (`ItemId`);

--
-- Constraints for table `Merchandise`
--
ALTER TABLE `Merchandise`
  ADD CONSTRAINT `Merchandise_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `GameItems` (`ItemId`);

--
-- Constraints for table `PhysicalGames`
--
ALTER TABLE `PhysicalGames`
  ADD CONSTRAINT `PhysicalGames_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `GameItems` (`ItemId`);

--
-- Constraints for table `TransactionItems`
--
ALTER TABLE `TransactionItems`
  ADD CONSTRAINT `TransactionItems_ibfk_1` FOREIGN KEY (`TransactionId`) REFERENCES `Transactions` (`TransactionId`),
  ADD CONSTRAINT `TransactionItems_ibfk_2` FOREIGN KEY (`ItemId`) REFERENCES `GameItems` (`ItemId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
