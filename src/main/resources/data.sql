    `idx`  INT(11) NOT NULL AUTO_INCREMENT,
    `type`  VARCHAR(45) NULL,
    `num` INT(11) NULL,
    `name` VARCHAR(45) NULL,
    `distance` INT(45) NULL,
    `time` INT(11) NULL,
    `speed` FLOAT(11,2) NOT NULL,
    `basic` VARCHAR(45) NOT NULL,
    `calc` INT(11) NOT NULL,
insert into `data`.`base_data` values(1, 'type-1', 1,'a' , 1,   1, 13.5, NULL, NULL);
insert into `data`.`base_data` values(2, 'type-2', 2,NULL, 2,   2, 12.4, 'B', NULL);
insert into `data`.`base_data` values(3, NULL,     3,'c' , NULL,3, 11.3, 'C', NULL);
insert into `data`.`base_data` values(4, 'type-4', 4,'d' , 4,  NULL,12.1, 'D', NULL);
insert into `data`.`base_data` values(5, 'type-5', 5,'e' , 5,   5,  NULL, 'E', 4);
