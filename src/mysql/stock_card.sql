-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1:3306
-- Üretim Zamanı: 31 Mar 2021, 19:27:53
-- Sunucu sürümü: 5.7.31
-- PHP Sürümü: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `stock_card`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` (
  `stock_code` varchar(50) COLLATE utf8_turkish_ci NOT NULL,
  `stock_name` varchar(100) COLLATE utf8_turkish_ci NOT NULL,
  `stock_type` int(2) NOT NULL,
  `unit` varchar(10) COLLATE utf8_turkish_ci NOT NULL,
  `barcode` varchar(30) COLLATE utf8_turkish_ci NOT NULL,
  `VAT_type` double NOT NULL,
  `creation_date` datetime NOT NULL,
  `description` text COLLATE utf8_turkish_ci NOT NULL,
  PRIMARY KEY (`stock_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `product`
--

INSERT INTO `product` (`stock_code`, `stock_name`, `stock_type`, `unit`, `barcode`, `VAT_type`, `creation_date`, `description`) VALUES
('2S0012', 'Sosis', 2, 'Kilogram', '1456659875236', 0.08, '2021-03-30 00:00:00', '-'),
('2S0007', 'Somon', 2, 'Kilogram', '1145697852631', 0.01, '2021-03-30 00:00:00', '-'),
('2S0008', 'Karides', 2, 'Kilogram', '4569756321562', 0.01, '2021-03-30 00:00:00', '-'),
('2S0009', 'Kalamar', 2, 'Kilogram', '4785691452367', 0.01, '2021-03-30 00:00:00', '-'),
('2S0010', 'Barbun', 2, 'Kilogram', '7745698562163', 0.01, '2021-03-30 00:00:00', '-'),
('2S0011', 'Kefal', 2, 'Kilogram', '4589615236777', 0.01, '2021-03-30 00:00:00', '-'),
('2S0006', 'Levrek', 2, 'Kilogram', '4456985621365', 0.01, '2021-03-30 00:00:00', '-'),
('2S0003', 'Pirzola', 2, 'Kilogram', '3314562189654', 0.08, '2021-03-30 00:00:00', '-'),
('2S0004', 'Tavuk Pirzola', 2, 'Kilogram', '4596222156987', 0.08, '2021-03-30 00:00:00', '-'),
('2S0005', 'Tavuk Bütün', 2, 'Kilogram', '4456985632156', 0.08, '2021-03-30 00:00:00', '-'),
('2S0001', 'Biftek', 2, 'Kilogram', '1145698756312', 0.08, '2021-03-30 00:00:00', '-'),
('2S0002', 'Antrikot', 2, 'Kilogram', '4451265789234', 0.08, '2021-03-30 00:00:00', '-'),
('2S0000', 'Kıyma', 2, 'Kilogram', '3326598715469', 0.08, '2021-03-30 00:00:00', '-'),
('1S0009', 'Kaşar Peyniri', 1, 'Kilogram', '4459621756326', 0.08, '2021-03-31 00:00:00', '-'),
('1S0002', 'İnek Peyniri', 1, 'Kilogram', '2172614771118', 0.08, '2021-03-31 00:00:00', '-'),
('1S0007', 'Tereyağ', 1, 'Kilogram', '6635974155666', 0.08, '2021-03-31 00:00:00', '-'),
('1S0006', 'Lor Peyniri', 1, 'Kilogram', '6635984526599', 0.08, '2021-03-31 00:00:00', '-'),
('1S0005', 'Labne', 1, 'Kilogram', '3365986120026', 0.08, '2021-03-31 00:00:00', '-'),
('1S0004', 'Ayran', 1, 'Litre', '3659826551146', 0.08, '2021-03-31 00:00:00', '-'),
('1S0003', 'Yoğurt', 1, 'Kilogram', '2879654836251', 0.08, '2021-03-31 00:00:00', '-'),
('1S0008', 'Kefir', 1, 'Litre', '3324555714569', 0.08, '2021-03-31 00:00:00', '-'),
('1S0001', 'Krema', 1, 'Litre', '1619718198954', 0.08, '2021-03-31 00:00:00', '-'),
('1S0000', 'Süt', 1, 'Litre', '1238217421187', 0.08, '2021-03-31 00:00:00', '-'),
('2S0013', 'Salam', 2, 'Kilogram', '7458621365984', 0.08, '2021-03-30 00:00:00', '-'),
('2S0014', 'Sucuk', 2, 'Kilogram', '4561598765321', 0.08, '2021-03-30 00:00:00', '-'),
('3Y0000', 'Ayçiçek Yağ', 3, 'Litre', '4589632156478', 0.08, '2021-03-29 00:00:00', '-'),
('3Y0001', 'Zeytinyağ', 3, 'Litre', '7569846321115', 0.08, '2021-03-29 00:00:00', '-'),
('3Y0002', 'Sızma Zeytinyağ', 3, 'Litre', '5698886321569', 0.08, '2021-03-29 00:00:00', '-'),
('4MS000', 'Mantar', 4, 'Kilogram', '1000236598745', 0.08, '2021-03-28 00:00:00', '-'),
('4MS001', 'Salatalık', 4, 'Kilogram', '4756986321562', 0.08, '2021-03-28 00:00:00', '-'),
('4MS002', 'Biber', 4, 'Kilogram', '1320078520026', 0.08, '2021-03-28 00:00:00', '-'),
('4MS003', 'Karpuz', 4, 'Kilogram', '1145226689123', 0.08, '2021-03-28 00:00:00', '-'),
('4MS004', 'Avakado', 4, 'Kilogram', '3362541598756', 0.08, '2021-03-28 00:00:00', '-'),
('4MS005', 'Arpacık Soğan', 4, 'Kilogram', '4512669856321', 0.08, '2021-03-28 00:00:00', '-'),
('4MS006', 'Kereviz', 4, 'Kilogram', '4562315688956', 0.08, '2021-03-28 00:00:00', '-'),
('4MS007', 'Patates', 4, 'Kilogram', '6325448952156', 0.08, '2021-03-28 00:00:00', '-'),
('4MS008', 'Kuru Soğan', 4, 'Kilogram', '6698523154569', 0.08, '2021-03-28 00:00:00', '-'),
('4MS009', 'Roka', 4, 'Adet', '3365985612456', 0.08, '2021-03-28 00:00:00', '-'),
('4MS010', 'Maydanoz', 4, 'Adet', '1145687962365', 0.08, '2021-03-28 00:00:00', '-'),
('5K0000', 'Salça', 5, 'Kilogram', '4451236598758', 0.08, '2021-03-27 00:00:00', '-'),
('5K0001', 'Garnitür', 5, 'Kilogram', '6593214578555', 0.08, '2021-03-27 00:00:00', '-'),
('5K0002', 'Tursu', 5, 'Kilogram', '5236149855563', 0.08, '2021-03-27 00:00:00', '-'),
('6D0002', 'Pizza', 6, 'Kilogram', '1452223659874', 0.08, '2021-03-10 00:00:00', '-'),
('6D0000', 'Elma Dilim Patates', 6, 'Kilogram', '1452368456921', 0.08, '2021-03-11 00:00:00', '-'),
('6D0001', 'Mantı', 6, 'Kilogram', '2563144459632', 0.08, '2021-03-11 00:00:00', '-'),
('6D0003', 'Pizza Tost', 6, 'Kilogram', '5233654125896', 0.08, '2021-03-03 00:00:00', '-'),
('7K0000', 'Pirinç', 7, 'Kilogram', '1423651178596', 0.08, '2021-03-04 00:00:00', '-'),
('7K0001', 'Nohut', 7, 'Kilogram', '3652214865932', 0.08, '2021-03-10 00:00:00', '-'),
('7K0004', 'Makarna', 7, 'Kilogram', '3362599651125', 0.08, '2021-03-07 00:00:00', '-'),
('7K0003', 'Mercimek', 7, 'Kilogram', '4452315698563', 0.08, '2021-03-12 00:00:00', '-'),
('7K0002', 'Fasulye', 7, 'Kilogram', '1125648563214', 0.08, '2021-03-16 00:00:00', '-'),
('8U0000', 'Bulgur', 8, 'Kilogram', '4415263895422', 0.08, '2021-03-23 00:00:00', '-'),
('8U0001', 'Gofret', 8, 'Adet', '4452368795236', 0.08, '2021-03-09 00:00:00', '-'),
('8U0002', 'Lavaş', 8, 'Adet', '7458899565145', 0.01, '2021-03-22 00:00:00', '-'),
('9S0000', 'Su', 9, 'Litre', '6659526310002', 0.01, '2021-03-09 00:00:00', '-');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
