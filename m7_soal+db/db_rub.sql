-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 29, 2025 at 08:15 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_rub`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id_customer` varchar(6) NOT NULL,
  `nama_customer` varchar(50) DEFAULT NULL,
  `jk_customer` varchar(1) DEFAULT NULL,
  `alamat_customer` varchar(50) DEFAULT NULL,
  `noTelp_customer` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id_customer`, `nama_customer`, `jk_customer`, `alamat_customer`, `noTelp_customer`) VALUES
('CUS001', 'Malik Houghton', 'M', 'Jl. Daan Mogot Raya 36 Km 17', '089682079245'),
('CUS002', 'Calista Hodson', 'F', 'Jl. Sahari Raya 20', '081310401814'),
('CUS003', 'Elisha Dudley', 'F', 'Jl. Palmerah Brt 21 J/JA', '087870824992'),
('CUS004', 'Philip Hulme', 'M', 'Jl. Jatinegara Brt 195', '089742902044'),
('CUS005', 'Natalya Morton', 'F', 'Jl. Bandara Polonia 59', '089750058021'),
('CUS006', 'Qasim Mills', 'M', 'Jl. Terate 61 A', '089649956926'),
('CUS007', 'Sean Roth', 'M', 'Jl. Fatmawati 39', '089748205113'),
('CUS008', 'Ridwan Mercer', 'M', 'Jl. Ngagel Madya 79', '087847922428'),
('CUS009', 'Marius Adamson', 'M', 'Jl. Pemuda 85', '089928407451'),
('CUS010', 'Darren Guzman', 'M', 'Jl. KH Moh Mansyur 162 A', '087898876482'),
('CUS011', 'Amaya Lindsay', 'F', 'Jl. Girilaya 68', '089647275561'),
('CUS012', 'Agatha Morrow', 'F', 'Jl. Pandanaran No. 92', '089940018419'),
('CUS013', 'Fraya Wang', 'F', 'Jl. Kopi 47', '089774959176'),
('CUS014', 'Rick Goodwin', 'M', 'Jl. Sadang Rahayu 38', '081377501583'),
('CUS015', 'Billy Greaves', 'M', 'Jl. Dr Cipto 48 A', '081358958028'),
('CUS016', 'Luci Tran', 'F', 'Jl. Terboyo Industri XII/9', '089695902842'),
('CUS017', 'Ibraheem Humphrey', 'M', 'Jl. Taman Kusumabangsa No.2', '089924927495'),
('CUS018', 'Virgil Harper', 'M', 'Jl. Pulo Sidik Bl R/30', '087842787428'),
('CUS019', 'Mathias Magana', 'M', 'Jl. Tegal Parang Utr 14', '081317348173'),
('CUS020', 'Rohit Todd', 'M', 'Jl. Pintu Besar Selatan E/1', '089917366456');

-- --------------------------------------------------------

--
-- Table structure for table `d_beli`
--

CREATE TABLE `d_beli` (
  `nota_beli` varchar(13) NOT NULL,
  `id_produk` varchar(6) NOT NULL,
  `harga_beli` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `d_beli`
--

INSERT INTO `d_beli` (`nota_beli`, `id_produk`, `harga_beli`, `quantity`) VALUES
('PB20201220001', 'RUB03', 5200, 3),
('PB20201220002', 'RUB02', 118000, 3),
('PB20201220003', 'RUB07', 25000, 4),
('PB20210105001', 'RUB01', 760000, 1),
('PB20210105001', 'RUB13', 145000, 2),
('PB20210105001', 'RUB15', 525000, 1),
('PB20210117001', 'RUB20', 41000, 5),
('PB20210117001', 'RUB21', 22000, 3),
('PB20210201001', 'RUB56', 1000, 20),
('PB20210310001', 'RUB38', 17000, 2),
('PB20210310002', 'RUB11', 35000, 4),
('PB20210414001', 'RUB15', 530000, 3),
('PB20210414001', 'RUB16', 200000, 2),
('PB20210414001', 'RUB20', 40000, 2),
('PB20210502001', 'RUB11', 25000, 1),
('PB20210628001', 'RUB14', 230000, 1),
('PB20210729001', 'RUB30', 5000, 3),
('PB20210729001', 'RUB35', 20000, 2),
('PB20210729002', 'RUB37', 30000, 2),
('PB20210729003', 'RUB21', 25000, 1),
('PB20210817001', 'RUB09', 115000, 3),
('PB20210817001', 'RUB22', 15000, 4),
('PB20210817001', 'RUB32', 220000, 5),
('PB20210912001', 'RUB40', 25000, 1),
('PB20210912001', 'RUB44', 75000, 1),
('PB20210925001', 'RUB18', 45000, 3),
('PB20210925002', 'RUB50', 30000, 2),
('PB20211102001', 'RUB23', 200000, 1),
('PB20211220001', 'RUB17', 12000, 2),
('PB20211220001', 'RUB19', 110000, 3);

-- --------------------------------------------------------

--
-- Table structure for table `d_jual`
--

CREATE TABLE `d_jual` (
  `nota_jual` varchar(13) NOT NULL,
  `id_produk` varchar(6) NOT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `d_jual`
--

INSERT INTO `d_jual` (`nota_jual`, `id_produk`, `quantity`) VALUES
('PJ20210110001', 'RUB03', 1),
('PJ20210110001', 'RUB08', 2),
('PJ20210110002', 'RUB11', 1),
('PJ20210112001', 'RUB10', 2),
('PJ20210112001', 'RUB33', 3),
('PJ20210212002', 'RUB19', 2),
('PJ20210215001', 'RUB30', 1),
('PJ20210302001', 'RUB25', 1),
('PJ20210302001', 'RUB31', 1),
('PJ20210302001', 'RUB51', 2),
('PJ20210303001', 'RUB22', 3),
('PJ20210303001', 'RUB48', 3),
('PJ20210303002', 'RUB21', 1),
('PJ20210415001', 'RUB07', 1),
('PJ20210415001', 'RUB10', 2),
('PJ20210509001', 'RUB01', 1),
('PJ20210509002', 'RUB47', 2),
('PJ20210509003', 'RUB11', 1),
('PJ20210509003', 'RUB51', 2),
('PJ20210520001', 'RUB14', 3),
('PJ20210610001', 'RUB24', 2),
('PJ20210611001', 'RUB18', 2),
('PJ20210612001', 'RUB22', 1),
('PJ20210612001', 'RUB29', 1),
('PJ20210708001', 'RUB37', 1),
('PJ20210730001', 'RUB27', 2),
('PJ20210730001', 'RUB28', 1),
('PJ20210803001', 'RUB12', 2),
('PJ20210804001', 'RUB04', 1),
('PJ20210804001', 'RUB11', 1),
('PJ20210804001', 'RUB56', 5),
('PJ20210805001', 'RUB05', 3),
('PJ20210805001', 'RUB56', 5),
('PJ20210805002', 'RUB05', 2),
('PJ20210805002', 'RUB09', 1),
('PJ20210805003', 'RUB34', 1),
('PJ20210921001', 'RUB31', 2),
('PJ20211005001', 'RUB28', 3),
('PJ20211005002', 'RUB35', 1),
('PJ20211005002', 'RUB41', 2),
('PJ20211020001', 'RUB49', 5),
('PJ20211103001', 'RUB11', 1),
('PJ20211103001', 'RUB43', 1),
('PJ20211115001', 'RUB59', 2),
('PJ20211202001', 'RUB57', 1),
('PJ20211202001', 'RUB58', 3),
('PJ20211210001', 'RUB51', 4),
('PJ20211220001', 'RUB44', 2),
('PJ20220105001', 'RUB26', 2),
('PJ20220105001', 'RUB31', 1),
('PJ20220107001', 'RUB09', 1),
('PJ20220108001', 'RUB15', 1);

-- --------------------------------------------------------

--
-- Table structure for table `h_beli`
--

CREATE TABLE `h_beli` (
  `nota_beli` varchar(13) NOT NULL,
  `id_karyawan` varchar(6) DEFAULT NULL,
  `id_supplier` varchar(5) DEFAULT NULL,
  `subtotal_beli` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `h_beli`
--

INSERT INTO `h_beli` (`nota_beli`, `id_karyawan`, `id_supplier`, `subtotal_beli`) VALUES
('PB20201220001', 'KAR001', 'SUP01', 156000),
('PB20201220002', 'KAR002', 'SUP02', 354000),
('PB20201220003', 'KAR002', 'SUP03', 100000),
('PB20210105001', 'KAR001', 'SUP01', 1575000),
('PB20210117001', 'KAR003', 'SUP04', 271000),
('PB20210201001', 'KAR004', 'SUP05', 20000),
('PB20210310001', 'KAR003', 'SUP03', 34000),
('PB20210310002', 'KAR002', 'SUP05', 140000),
('PB20210414001', 'KAR004', 'SUP06', 1010000),
('PB20210502001', 'KAR001', 'SUP07', 50000),
('PB20210628001', 'KAR005', 'SUP08', 230000),
('PB20210729001', 'KAR001', 'SUP09', 55000),
('PB20210729002', 'KAR003', 'SUP01', 60000),
('PB20210729003', 'KAR005', 'SUP10', 75000),
('PB20210817001', 'KAR002', 'SUP11', 395000),
('PB20210912001', 'KAR002', 'SUP12', 100000),
('PB20210925001', 'KAR003', 'SUP13', 135000),
('PB20210925002', 'KAR001', 'SUP14', 60000),
('PB20211102001', 'KAR004', 'SUP07', 200000),
('PB20211220001', 'KAR003', 'SUP15', 354000);

-- --------------------------------------------------------

--
-- Table structure for table `h_jual`
--

CREATE TABLE `h_jual` (
  `nota_jual` varchar(13) NOT NULL,
  `id_customer` varchar(6) DEFAULT NULL,
  `id_karyawan` varchar(6) DEFAULT NULL,
  `harga_total` int(11) DEFAULT NULL,
  `kode_promo` varchar(15) DEFAULT NULL,
  `subtotal_jual` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `h_jual`
--

INSERT INTO `h_jual` (`nota_jual`, `id_customer`, `id_karyawan`, `harga_total`, `kode_promo`, `subtotal_jual`) VALUES
('PJ20210110001', 'CUS001', 'KAR001', 170000, 'MEMBERBARU', 145000),
('PJ20210110002', 'CUS002', 'KAR002', 39000, NULL, 39000),
('PJ20210112001', 'CUS003', 'KAR003', 1030000, 'MEMBERBARU', 1005000),
('PJ20210212002', 'CUS004', 'KAR002', 270000, 'RUBIKMURAH', 230000),
('PJ20210215001', 'CUS005', 'KAR004', 8900, NULL, 8900),
('PJ20210302001', 'CUS002', 'KAR001', 1285000, 'BELITERUS', 1235000),
('PJ20210303001', 'CUS003', 'KAR002', 316500, 'BELITERUS', 284850),
('PJ20210303002', 'CUS006', 'KAR004', 33000, NULL, 33000),
('PJ20210415001', 'CUS007', 'KAR003', 150000, 'MEMBERBARU', 125000),
('PJ20210509001', 'CUS001', 'KAR005', 820000, 'RUBIKMURAH', 780000),
('PJ20210509002', 'CUS008', 'KAR001', 59000, NULL, 59000),
('PJ20210509003', 'CUS009', 'KAR005', 569000, 'MEMBERBARU', 544000),
('PJ20210520001', 'CUS010', 'KAR006', 780000, 'MEMBERBARU', 755000),
('PJ20210610001', 'CUS003', 'KAR007', 76000, NULL, 76000),
('PJ20210611001', 'CUS011', 'KAR006', 134000, 'RUBIKMURAH', 94000),
('PJ20210612001', 'CUS004', 'KAR007', 68500, NULL, 68500),
('PJ20210708001', 'CUS012', 'KAR007', 48000, NULL, 48000),
('PJ20210730001', 'CUS013', 'KAR008', 435000, 'DISKONMANTAP', 415000),
('PJ20210803001', 'CUS014', 'KAR008', 57000, 'JAJANRUBIK', 42750),
('PJ20210804001', 'CUS015', 'KAR008', 100000, NULL, 100000),
('PJ20210805001', 'CUS005', 'KAR008', 153000, 'RUBIKMURAH', 113000),
('PJ20210805002', 'CUS003', 'KAR006', 151000, 'RUBIKMURAH', 111000),
('PJ20210805003', 'CUS009', 'KAR007', 70000, NULL, 70000),
('PJ20210921001', 'CUS016', 'KAR009', 280000, 'BORONGRUBIK', 240000),
('PJ20211005001', 'CUS007', 'KAR006', 675000, 'DISKONGEDE', 575000),
('PJ20211005002', 'CUS017', 'KAR006', 393000, NULL, 393000),
('PJ20211020001', 'CUS012', 'KAR009', 190000, 'JAJANRUBIK', 175000),
('PJ20211103001', 'CUS008', 'KAR007', 84000, 'JAJANRUBIK', 69000),
('PJ20211115001', 'CUS011', 'KAR008', 48000, NULL, 48000),
('PJ20211202001', 'CUS002', 'KAR008', 185500, 'DISKONMANTAP', 166950),
('PJ20211210001', 'CUS017', 'KAR009', 1060000, 'DISKONGEDE', 960000),
('PJ20211220001', 'CUS018', 'KAR009', 198000, NULL, 198000),
('PJ20220105001', 'CUS015', 'KAR010', 171600, NULL, 171600),
('PJ20220107001', 'CUS019', 'KAR007', 55000, NULL, 55000),
('PJ20220108001', 'CUS020', 'KAR006', 575000, 'BORONGRUBIK', 535000);

-- --------------------------------------------------------

--
-- Table structure for table `jenis`
--

CREATE TABLE `jenis` (
  `id_jenis` varchar(4) NOT NULL,
  `nama_jenis` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jenis`
--

INSERT INTO `jenis` (`id_jenis`, `nama_jenis`) VALUES
('BB01', 'Black Base'),
('CA01', 'Carbon'),
('DN01', 'DNA'),
('MI01', 'Mirror'),
('SL01', 'Stickerless'),
('WB01', 'White Base');

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `id_karyawan` varchar(6) NOT NULL,
  `nama_karyawan` varchar(50) DEFAULT NULL,
  `jk_karyawan` varchar(1) DEFAULT NULL,
  `alamat_karyawan` varchar(50) DEFAULT NULL,
  `noTelp_karyawan` varchar(12) DEFAULT NULL,
  `dob_karyawan` date DEFAULT NULL,
  `tgl_masuk` date DEFAULT NULL,
  `status_karyawan` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `karyawan`
--

INSERT INTO `karyawan` (`id_karyawan`, `nama_karyawan`, `jk_karyawan`, `alamat_karyawan`, `noTelp_karyawan`, `dob_karyawan`, `tgl_masuk`, `status_karyawan`) VALUES
('KAR001', 'Mac Bob', 'M', 'Jl. Putro Agung II/6', '087855317837', '1991-07-11', '2020-12-18', '1'),
('KAR002', 'Kimora Reilly', 'F', 'Jl. Pangrango Terusan 19', '087818391799', '1978-01-01', '2020-12-18', '0'),
('KAR003', 'Giovanni Velez', 'M', 'Jl. Kayu Besar II 1B', '081309226949', '1993-04-03', '2021-01-10', '0'),
('KAR004', 'Ravi Mill', 'M', 'Jl. TSS Raya 7', '089674857292', '1994-07-28', '2021-01-17', '1'),
('KAR005', 'Georgina Spence', 'F', 'Jl. Margaguna 1', '089718408208', '1991-02-15', '2021-05-09', '1'),
('KAR006', 'Nabeela Collins', 'F', 'Jl. KH Samanhudi 19', '081313784813', '1998-01-18', '2021-05-05', '1'),
('KAR007', 'Dustin Broadhurst', 'M', 'Jl. Sakti Raya 1 Kemanggisan Slipi', '089642001986', '1999-12-07', '2021-06-01', '0'),
('KAR008', 'Katharine Strickland', 'F', 'Jl. Sutrisno 149 C-D', '089965789402', '1992-11-05', '2021-07-17', '1'),
('KAR009', 'Rahma Hayward', 'F', 'Jl. Industri IV/6', '088944802079', '1991-10-31', '2021-10-10', '1'),
('KAR010', 'Nathanael Hamer', 'M', 'Jl. Ibrahim Umar 11', '089765381333', '1990-08-10', '2021-12-22', '1');

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` varchar(5) NOT NULL,
  `nama_kategori` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
('KAT01', '2 x 2'),
('KAT02', '3 x 3'),
('KAT03', '4 x 4'),
('KAT04', 'Pyraminx'),
('KAT05', 'Megaminx'),
('KAT06', 'Skewb'),
('KAT07', 'Square-1'),
('KAT08', 'Stand'),
('KAT09', 'Lube'),
('KAT10', 'Timer'),
('KAT11', 'Accessories'),
('KAT12', 'Others');

-- --------------------------------------------------------

--
-- Table structure for table `merk`
--

CREATE TABLE `merk` (
  `id_merk` varchar(4) NOT NULL,
  `nama_merk` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `merk`
--

INSERT INTO `merk` (`id_merk`, `nama_merk`) VALUES
('DS01', 'DianSheng'),
('DY01', 'DaYan'),
('GC01', 'GAN Cube'),
('LF01', 'Lefun'),
('MY01', 'MoYu'),
('QJ01', 'QJ'),
('QY01', 'QiYi'),
('SS01', 'ShengShou'),
('YJ01', 'YongJun');

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id_produk` varchar(5) NOT NULL,
  `nama_produk` varchar(50) DEFAULT NULL,
  `id_kategori` varchar(5) DEFAULT NULL,
  `id_jenis` varchar(5) DEFAULT NULL,
  `id_merk` varchar(4) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`id_produk`, `nama_produk`, `id_kategori`, `id_jenis`, `id_merk`, `stok`, `harga`) VALUES
('RUB01', 'GAN 11 M Pro', 'KAT02', 'SL01', 'GC01', 10, 820000),
('RUB02', 'MoYu RS3M 2021 Maglev Edition', 'KAT02', 'SL01', 'MY01', 11, 130000),
('RUB03', 'YongJun YuLong V2 m', 'KAT02', 'BB01', 'YJ01', 20, 68000),
('RUB04', 'MoYu MeiLong 3M', 'KAT02', 'SL01', 'MY01', 10, 52000),
('RUB05', 'YongJun YuLong Pyraminx V2 m', 'KAT04', 'BB01', 'YJ01', 15, 48000),
('RUB06', 'DaYan GuHong V4 m', 'KAT02', 'SL01', 'DY01', 7, 144000),
('RUB07', 'QiYi Ivy', 'KAT11', 'SL01', 'QY01', 5, 30000),
('RUB08', 'YongJun Yupo V2 m', 'KAT01', 'BB01', 'YJ01', 17, 51300),
('RUB09', 'Lefun Megaminx', 'KAT05', 'CA01', 'LF01', 10, 55000),
('RUB10', 'Lefun Diamond', 'KAT11', 'BB01', 'LF01', 9, 60000),
('RUB11', 'QiYi Mirror 3x3', 'KAT02', 'MI01', 'QY01', 11, 39000),
('RUB12', 'QiYi DNA Pyraminx', 'KAT04', 'DN01', 'QY01', 11, 57000),
('RUB13', 'GAN 356 RS', 'KAT02', 'SL01', 'GC01', 5, 160000),
('RUB14', 'GAN 356 RS m', 'KAT02', 'SL01', 'GC01', 5, 260000),
('RUB15', 'GAN 356 XS', 'KAT02', 'SL01', 'GC01', 3, 575000),
('RUB16', 'QiYi MP 3x3', 'KAT02', 'WB01', 'QY01', 8, 210000),
('RUB17', 'ShengShou Shengso Legend 3x3', 'KAT02', 'BB01', 'SS01', 10, 17500),
('RUB18', 'QiYi ms 2x2 Magnetic', 'KAT01', 'SL01', 'QY01', 15, 67000),
('RUB19', 'DianSheng RS4M', 'KAT03', 'SL01', 'DS01', 9, 135000),
('RUB20', 'QiYi Pyraminx Black Carbon Fiber', 'KAT04', 'CA01', 'QY01', 10, 47500),
('RUB21', 'MoYu MeiLong 3x3 Mirror – Black Silver', 'KAT02', 'MI01', 'MY01', 10, 33000),
('RUB22', 'QiYi QiDi 2x2', 'KAT01', 'BB01', 'QY01', 5, 19500),
('RUB23', 'GAN 249 V2 M', 'KAT01', 'SL01', 'GC01', 3, 229000),
('RUB24', 'YongJun FengHouLun Fisher', 'KAT11', 'BB01', 'YJ01', 11, 38000),
('RUB25', 'GAN Megaminx M', 'KAT05', 'SL01', 'GC01', 12, 615000),
('RUB26', 'YongJun GuanLong 3x3 V3', 'KAT02', 'BB01', 'YJ01', 20, 15800),
('RUB27', 'DianSheng RS6', 'KAT11', 'SL01', 'DS01', 10, 105000),
('RUB28', 'YongJun MGC 4x4 Magnetic', 'KAT03', 'BB01', 'YJ01', 15, 225000),
('RUB29', 'MoYu MoFang Pandora 3x3', 'KAT11', 'BB01', 'MY01', 6, 49000),
('RUB30', 'MoYu Lube V2', 'KAT09', NULL, 'MY01', 19, 8900),
('RUB31', 'MoYu RS4M 2020', 'KAT03', 'SL01', 'MY01', 10, 140000),
('RUB32', 'GAN Pyraminx M Standard Edition', 'KAT04', 'SL01', 'GC01', 7, 255000),
('RUB33', 'GAN Pyraminx M Enhanced Edition', 'KAT04', 'SL01', 'GC01', 3, 310000),
('RUB34', 'QiYi DNA 3x3 Concave', 'KAT02', 'DN01', 'QY01', 15, 70000),
('RUB35', 'YongJun YiLeng Windmill', 'KAT11', 'BB01', 'YJ01', 11, 33000),
('RUB36', 'QJ Timer V3', 'KAT10', NULL, 'QJ01', 3, 170000),
('RUB37', 'QJ QunJia Master Pillow Morphix', 'KAT11', 'WB01', 'QJ01', 5, 48000),
('RUB38', 'QiYi Mirror 2x2 Gold', 'KAT01', 'MI01', 'QY01', 18, 25000),
('RUB39', 'MeiLong 4x4 Stickerless', 'KAT03', 'SL01', 'MY01', 11, 125000),
('RUB40', 'QiYi QiMeng 3x3 Black Carbon Fiber', 'KAT02', 'CA01', 'QY01', 12, 32500),
('RUB41', 'YongJun MGC Megaminx M', 'KAT05', 'SL01', 'YJ01', 2, 180000),
('RUB42', 'MoYu MeiLong 3x3 Black Carbon Fiber', 'KAT02', 'CA01', 'MY01', 10, 24000),
('RUB43', 'QiYi M-2 Lube', 'KAT09', NULL, 'QY01', 17, 45000),
('RUB44', 'ShengShou Shengso Magic Clock 3x3 Magnetic', 'KAT11', NULL, 'SS01', 10, 99000),
('RUB45', 'QiYi Magic Clock Magnetic', 'KAT11', NULL, 'QY01', 11, 309000),
('RUB46', 'Logo Sticker MoYu 3x3', 'KAT12', NULL, 'MY01', 15, 1000),
('RUB47', 'DianSheng DNA Snake Puzzle 24', 'KAT11', 'DN01', 'DS01', 5, 29500),
('RUB48', 'MoYu MoFang Fisher Cube 3x3', 'KAT11', 'SL01', 'MY01', 14, 129000),
('RUB49', 'YongJun GuanLong Square-1', 'KAT07', 'BB01', 'YJ01', 9, 38000),
('RUB50', 'MoFang Mirror Windmill 3x3', 'KAT11', 'MI01', 'MY01', 10, 35000),
('RUB51', 'GAN Skewb M Standard Version', 'KAT06', 'SL01', 'GC01', 8, 265000),
('RUB52', 'QiYi Windmill Black Carbon Fiber', 'KAT11', 'CA01', 'QY01', 10, 52000),
('RUB53', 'YongJun GuanLong Skewb', 'KAT06', 'BB01', 'YJ01', 11, 33000),
('RUB54', 'QiYi QiHeng Skewb', 'KAT06', 'BB01', 'QY01', 7, 39000),
('RUB55', 'QiYi QiFa S Square-1', 'KAT07', 'SL01', 'QY01', 8, 85000),
('RUB56', 'QiYi DNA Cube Stand', 'KAT08', 'DN01', 'QY01', 32, 1800),
('RUB57', 'YongJun YuHu Megaminx V2 m', 'KAT05', 'SL01', 'YJ01', 3, 142000),
('RUB58', 'Sticker Rubik 3x3 Oracal Vinyl', 'KAT12', NULL, NULL, 15, 14500),
('RUB59', 'GAN Standard Lube', 'KAT09', NULL, 'GC01', 7, 24000);

-- --------------------------------------------------------

--
-- Table structure for table `promo`
--

CREATE TABLE `promo` (
  `kode_promo` varchar(15) NOT NULL,
  `besar_potongan` int(11) DEFAULT NULL,
  `maks_potongan` int(11) DEFAULT NULL,
  `min_pembelian` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promo`
--

INSERT INTO `promo` (`kode_promo`, `besar_potongan`, `maks_potongan`, `min_pembelian`) VALUES
('BELITERUS', 10, 50000, 300000),
('BORONGRUBIK', 50, 40000, 200000),
('DISKONGEDE', 75, 20000, 100000),
('DISKONMANTAP', 10, 20000, 150000),
('DISKONULTAH', 20, 25000, 100000),
('JAJANRUBIK', 25, 15000, 50000),
('MEMBERBARU', 20, 25000, 50000),
('PROMOABADI', 10, 10000, 50000),
('RUBIKMURAH', 30, 40000, 100000);

-- --------------------------------------------------------

--
-- Table structure for table `rating_produk`
--

CREATE TABLE `rating_produk` (
  `nota_jual` varchar(13) NOT NULL,
  `id_produk` varchar(6) NOT NULL,
  `rating` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rating_produk`
--

INSERT INTO `rating_produk` (`nota_jual`, `id_produk`, `rating`) VALUES
('PJ20210110001', 'RUB03', 5),
('PJ20210110001', 'RUB08', 5),
('PJ20210110002', 'RUB11', 4),
('PJ20210112001', 'RUB10', 3),
('PJ20210112001', 'RUB33', 5),
('PJ20210212002', 'RUB19', 3),
('PJ20210215001', 'RUB30', 4),
('PJ20210302001', 'RUB25', 3),
('PJ20210302001', 'RUB31', 5),
('PJ20210302001', 'RUB51', 5),
('PJ20210303001', 'RUB22', 5),
('PJ20210303001', 'RUB48', 5),
('PJ20210303002', 'RUB21', 5),
('PJ20210415001', 'RUB07', 2),
('PJ20210415001', 'RUB10', 4),
('PJ20210509001', 'RUB01', 5),
('PJ20210509002', 'RUB47', 4),
('PJ20210509003', 'RUB11', 5),
('PJ20210509003', 'RUB51', 5),
('PJ20210520001', 'RUB14', 3),
('PJ20210610001', 'RUB24', 5),
('PJ20210611001', 'RUB18', 3),
('PJ20210612001', 'RUB22', 5),
('PJ20210612001', 'RUB29', 5),
('PJ20210708001', 'RUB37', 3),
('PJ20210730001', 'RUB27', 4),
('PJ20210730001', 'RUB28', 4),
('PJ20210803001', 'RUB12', 4),
('PJ20210804001', 'RUB04', 5),
('PJ20210804001', 'RUB11', 4),
('PJ20210804001', 'RUB56', 5),
('PJ20210805001', 'RUB05', 4),
('PJ20210805001', 'RUB56', 3),
('PJ20210805002', 'RUB05', 4),
('PJ20210805002', 'RUB09', 5),
('PJ20210805003', 'RUB34', 5),
('PJ20210921001', 'RUB31', 5),
('PJ20211005001', 'RUB28', 5),
('PJ20211005002', 'RUB35', 5),
('PJ20211005002', 'RUB41', 4),
('PJ20211020001', 'RUB49', 3),
('PJ20211103001', 'RUB11', 5),
('PJ20211103001', 'RUB43', 5),
('PJ20211115001', 'RUB59', 5),
('PJ20211202001', 'RUB57', 5),
('PJ20211202001', 'RUB58', 4),
('PJ20211210001', 'RUB51', 5),
('PJ20211220001', 'RUB44', 5),
('PJ20220105001', 'RUB26', 4),
('PJ20220105001', 'RUB31', 3),
('PJ20220107001', 'RUB09', 5),
('PJ20220108001', 'RUB15', 5);

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `id_supplier` varchar(5) NOT NULL,
  `nama_supplier` varchar(50) DEFAULT NULL,
  `cp_supplier` varchar(20) DEFAULT NULL,
  `pn_supplier` varchar(12) DEFAULT NULL,
  `alamat_supplier` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `cp_supplier`, `pn_supplier`, `alamat_supplier`) VALUES
('SUP01', 'PT. Cubesy', 'Darius', '087871836180', 'Jl. Percetakan Negara 566 E'),
('SUP02', 'PT. Angolare', 'Reynolds', '081318360837', 'Jl. HOS Cokroaminoto 1'),
('SUP03', 'PT. Quark Cube', 'Edward', '089711183717', 'Jl. Prof Dr Supomo 141'),
('SUP04', 'PT. Cubico', 'Kerry', '087871890189', 'Jl. Bendungan Hilir Raya 56'),
('SUP05', 'PT. Square Object', 'Murphy', '089639813971', 'Jl. Dewi Sartika Gg Masjid 2'),
('SUP06', 'PT. Zeus Cube', 'Alina', '089610281902', 'Jl. Kayu Putih Selatan II 34'),
('SUP07', 'PT. Swirl Cube', 'Eva', '087820192838', 'Jl. Kwitang Raya No. 8'),
('SUP08', 'PT. Pentagono', 'Dorothy', '089918201911', 'Jl. Asem Baris 6/18'),
('SUP09', 'PT. Cube Layer', 'Essie', '089619371034', 'Jl. Pahlawan Revolusi 11'),
('SUP10', 'PT. Impilate', 'Kimberley', '087856028402', 'Jl. Ciateul 40'),
('SUP11', 'PT. Circon Scrit', 'Cora', '089710191913', 'Jl. Sunter Muara 8'),
('SUP12', 'PT. Eliptica', 'Forster', '087819883019', 'Jl. Gajah Mada 19-26'),
('SUP13', 'PT. Lunar Cube', 'Noble', '081301933301', 'Jl. Pelajar 44'),
('SUP14', 'PT. Cubezilla', 'Frank', '089728163717', 'Jl. Amal Raya 2'),
('SUP15', 'PT. Team Cube', 'Patrick', '089711038477', 'Jl. Cikini Raya 60');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id_customer`);

--
-- Indexes for table `d_beli`
--
ALTER TABLE `d_beli`
  ADD PRIMARY KEY (`nota_beli`,`id_produk`);

--
-- Indexes for table `d_jual`
--
ALTER TABLE `d_jual`
  ADD PRIMARY KEY (`nota_jual`,`id_produk`);

--
-- Indexes for table `h_beli`
--
ALTER TABLE `h_beli`
  ADD PRIMARY KEY (`nota_beli`);

--
-- Indexes for table `h_jual`
--
ALTER TABLE `h_jual`
  ADD PRIMARY KEY (`nota_jual`);

--
-- Indexes for table `jenis`
--
ALTER TABLE `jenis`
  ADD PRIMARY KEY (`id_jenis`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indexes for table `merk`
--
ALTER TABLE `merk`
  ADD PRIMARY KEY (`id_merk`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- Indexes for table `promo`
--
ALTER TABLE `promo`
  ADD PRIMARY KEY (`kode_promo`);

--
-- Indexes for table `rating_produk`
--
ALTER TABLE `rating_produk`
  ADD PRIMARY KEY (`nota_jual`,`id_produk`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id_supplier`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
