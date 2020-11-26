CREATE TABLE `register` (
  `ID` int(128) NOT NULL,
  `Name` varchar(64) NOT NULL,
  `Password` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `register` (`ID`, `Name`, `Password`) VALUES
(1, 'admin', 'admin');

ALTER TABLE `register`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Name` (`Name`);

ALTER TABLE `register`
  MODIFY `ID` int(128) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;
