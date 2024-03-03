CREATE TABLE IF NOT EXISTS `data`.`base_data` (
    `idx`  INT(11) NOT NULL AUTO_INCREMENT,
    `type`  VARCHAR(45) NULL,
    `num` INT(11) NULL,
    `name` VARCHAR(45) NULL,
    `distance` INT(45) NULL,
    `time` INT(11) NULL,
    `speed` FLOAT(11,2) NULL,
    `basic` VARCHAR(45) NULL,
    `calc` INT(11) NULL,
    PRIMARY KEY (`idx`)
);