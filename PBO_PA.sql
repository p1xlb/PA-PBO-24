-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2024 at 10:20 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pbo_pa`
--

-- --------------------------------------------------------

--
-- Table structure for table `digitalgames`
--

CREATE TABLE `digitalgames` (
  `ItemId` varchar(5) NOT NULL,
  `DownloadSize` double(10,2) NOT NULL,
  `Platform` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `digitalgames`
--

INSERT INTO `digitalgames` (`ItemId`, `DownloadSize`, `Platform`) VALUES
('200', 4.00, 'PC');

-- --------------------------------------------------------

--
-- Table structure for table `gameitems`
--

CREATE TABLE `gameitems` (
  `ItemId` varchar(5) NOT NULL,
  `ItemName` varchar(255) NOT NULL,
  `Year` int(4) NOT NULL,
  `Stock` int(5) NOT NULL,
  `Price` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gameitems`
--

INSERT INTO `gameitems` (`ItemId`, `ItemName`, `Year`, `Stock`, `Price`) VALUES
('100', 'test1', 2002, 21, 12.00),
('111', 'Elden Ring', 2022, 50, 600.00),
('121', 'Smash Bros Tee', 2023, 20, 50.00),
('1211', 'Pokemon Red', 1999, 20, 100.00),
('1212', 'test', 2022, 21, 12.00),
('132', 'GameMoney', 2020, 11, 100.00),
('200', 'Fortnite', 2016, 100, 4.00),
('DG001', 'Valheim', 2023, 21, 129.99),
('DG002', 'Minecraft', 2007, 12, 29.99);

-- --------------------------------------------------------

--
-- Table structure for table `gamevouchers`
--

CREATE TABLE `gamevouchers` (
  `ItemId` varchar(5) NOT NULL,
  `MarketPlatform` varchar(50) NOT NULL,
  `Quantity` int(50) NOT NULL,
  `ValidUntil` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gamevouchers`
--

INSERT INTO `gamevouchers` (`ItemId`, `MarketPlatform`, `Quantity`, `ValidUntil`) VALUES
('132', 'PC', 100, '20 Juni');

-- --------------------------------------------------------

--
-- Table structure for table `merchandise`
--

CREATE TABLE `merchandise` (
  `ItemId` varchar(5) NOT NULL,
  `Game` varchar(155) NOT NULL,
  `Type` varchar(155) NOT NULL,
  `Dimension` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `merchandise`
--

INSERT INTO `merchandise` (`ItemId`, `Game`, `Type`, `Dimension`) VALUES
('121', 'Smash Bros Ultimate', 'T-Shirt', '1:1');

-- --------------------------------------------------------

--
-- Table structure for table `physicalgames`
--

CREATE TABLE `physicalgames` (
  `ItemId` varchar(5) NOT NULL,
  `Edition` varchar(155) NOT NULL,
  `Platform` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `physicalgames`
--

INSERT INTO `physicalgames` (`ItemId`, `Edition`, `Platform`) VALUES
('111', 'Black', 'Playstation 5'),
('1212', 'Special Edition', 'PC'),
('1211', 'Ori', 'Gameboy');

-- --------------------------------------------------------

--
-- Table structure for table `transactionitems`
--

CREATE TABLE `transactionitems` (
  `TransactionId` int(5) NOT NULL,
  `ItemId` varchar(5) NOT NULL,
  `Quantity` int(4) NOT NULL,
  `TotalPrice` double(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `TransactionId` int(11) NOT NULL,
  `TransactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`TransactionId`, `TransactionDate`) VALUES
(1, '2024-05-30'),
(2, '2024-05-30'),
(3, '2024-05-30'),
(4, '2024-05-30'),
(5, '2024-05-30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `digitalgames`
--
ALTER TABLE `digitalgames`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `gameitems`
--
ALTER TABLE `gameitems`
  ADD PRIMARY KEY (`ItemId`);

--
-- Indexes for table `gamevouchers`
--
ALTER TABLE `gamevouchers`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `merchandise`
--
ALTER TABLE `merchandise`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `physicalgames`
--
ALTER TABLE `physicalgames`
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `transactionitems`
--
ALTER TABLE `transactionitems`
  ADD KEY `TransactionId` (`TransactionId`),
  ADD KEY `ItemId` (`ItemId`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`TransactionId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `TransactionId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `digitalgames`
--
ALTER TABLE `digitalgames`
  ADD CONSTRAINT `DigitalGames_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `gameitems` (`ItemId`);

--
-- Constraints for table `gamevouchers`
--
ALTER TABLE `gamevouchers`
  ADD CONSTRAINT `GameVouchers_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `gameitems` (`ItemId`);

--
-- Constraints for table `merchandise`
--
ALTER TABLE `merchandise`
  ADD CONSTRAINT `Merchandise_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `gameitems` (`ItemId`);

--
-- Constraints for table `physicalgames`
--
ALTER TABLE `physicalgames`
  ADD CONSTRAINT `PhysicalGames_ibfk_1` FOREIGN KEY (`ItemId`) REFERENCES `gameitems` (`ItemId`);

--
-- Constraints for table `transactionitems`
--
ALTER TABLE `transactionitems`
  ADD CONSTRAINT `TransactionItems_ibfk_1` FOREIGN KEY (`TransactionId`) REFERENCES `transactions` (`TransactionId`),
  ADD CONSTRAINT `TransactionItems_ibfk_2` FOREIGN KEY (`ItemId`) REFERENCES `gameitems` (`ItemId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
