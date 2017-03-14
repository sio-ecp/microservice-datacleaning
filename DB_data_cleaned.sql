# Configuration items for DataCleaning MicroService
# -> For example: last_cleaned_row
CREATE TABLE `MS_DataCleaning_conf` (
  `name` varchar(45) NOT NULL,
  `value` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# Cleaned city list
CREATE TABLE `DW_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


# Cleaned static stations infos
CREATE TABLE `DW_station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `station_number` int(11) DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `station_name` varchar(45) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `banking` tinyint(4) DEFAULT NULL,
  `bonus` tinyint(4) DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `elevation` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `city_id_idx` (`city_id`),
  CONSTRAINT `city_id` FOREIGN KEY (`city_id`) REFERENCES `DW_city` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


# Cleaned weather
CREATE TABLE `DW_weather` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` int(11) DEFAULT NULL,
  `weather_group` varchar(45) DEFAULT NULL,
  `pressure` int(11) DEFAULT NULL,
  `humidity_percentage` float DEFAULT NULL,
  `temperature` float DEFAULT NULL,
  `min_temperature` float DEFAULT NULL,
  `max_temperature` float DEFAULT NULL,
  `wind_speed` float DEFAULT NULL,
  `wind_direction` float DEFAULT NULL,
  `cloudiness_percentage` int(11) DEFAULT NULL,
  `rain_quantity` int(11) DEFAULT NULL,
  `snow_quantity` int(11) DEFAULT NULL,
  `sun_set` int(11) DEFAULT NULL,
  `sun_rise` int(11) DEFAULT NULL,
  `calculation_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `city_id_idx` (`city_id`),
  CONSTRAINT `city_id2` FOREIGN KEY (`city_id`) REFERENCES `DW_city` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


# Cleaned states per station
CREATE TABLE `DW_station_state` (
  `id` int(11) NOT NULL  AUTO_INCREMENT,
  `id_station` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `operational_bike_stands` int(11) DEFAULT NULL,
  `available_bike_stands` int(11) DEFAULT NULL,
  `available_bikes` int(11) DEFAULT NULL,
  `last_update` bigint(20) DEFAULT NULL,
  `movements` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX station_time (id_station,last_update),
  KEY `id_station_idx` (`id_station`),
  CONSTRAINT `id_st` FOREIGN KEY (`id_station`) REFERENCES `DW_station` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


# Global means for each station, divided by day-time ranges
CREATE TABLE `DW_station_means` (
  `id` int(11) NOT NULL,
  `id_station` int(11) NOT NULL,
  `week_day` int(3) DEFAULT NULL,
  `range_start` int(11) DEFAULT NULL,
  `range_end` int(11) DEFAULT NULL,
  `movement_mean` float DEFAULT NULL,
  `availability_mean` float DEFAULT NULL,
  `velib_nb_mean` float DEFAULT NULL,
  `movement_mean_rain` float DEFAULT NULL,
  `movement_mean_sun` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_station_idx` (`id_station`),
  CONSTRAINT `id_st2` FOREIGN KEY (`id_station`) REFERENCES `DW_station` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


# Sampled data table, generated from DW_station_state
CREATE TABLE `DW_station_sampled` (
  `id` int(11) NOT NULL,
  `id_station` int(11) NOT NULL,
  `timestamp_start` int(11) DEFAULT NULL,
  `timestamp_end` int(11) DEFAULT NULL,
  `movement_mean` float DEFAULT NULL,
  `availability_mean` float DEFAULT NULL,
  `velib_nb_mean` float DEFAULT NULL,
  `weather` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX station_time (id_station,timestamp_start),
  KEY `id_station_idx` (`id_station`),
  CONSTRAINT `id_st3` FOREIGN KEY (`id_station`) REFERENCES `DW_station` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


#
# Data Lake Tables
#

CREATE TABLE `station` (
  `id_station` int(11) NOT NULL AUTO_INCREMENT,
  `station_number` int(11) NOT NULL,
  `station_name` varchar(255) NOT NULL,
  `contract_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `banking` tinyint(1) NOT NULL,
  `bonus` tinyint(1) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `operational_bike_stands` int(11) NOT NULL,
  `available_bike_stands` int(11) NOT NULL,
  `available_bikes` int(11) NOT NULL,
  `last_update` bigint(20) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `city_name` varchar(255) NOT NULL,
  `country_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id_station`)
) ENGINE=InnoDB AUTO_INCREMENT=4908 DEFAULT CHARSET=latin1;

CREATE TABLE `stationelevation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `station_number` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `elevation` float NOT NULL,
  `resolution` float NOT NULL,
  `contract_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

CREATE TABLE `weather` (
  `id_weather` int(11) NOT NULL AUTO_INCREMENT,
  `weather_group` varchar(255) NOT NULL,
  `pressure` int(11) NOT NULL,
  `humidity_percentage` float NOT NULL,
  `temperature` float NOT NULL,
  `min_temperature` float NOT NULL,
  `max_temperature` float NOT NULL,
  `wind_speed` float NOT NULL,
  `wind_direction` float NOT NULL,
  `cloudiness_percentage` int(11) NOT NULL,
  `rain_quantity` int(11) NOT NULL,
  `snow_quantity` int(11) NOT NULL,
  `sun_set` bigint(20) NOT NULL,
  `sun_rise` bigint(20) NOT NULL,
  `calculation_time` bigint(20) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `city_name` varchar(255) NOT NULL,
  `country_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id_weather`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;