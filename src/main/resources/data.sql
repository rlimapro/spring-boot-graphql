INSERT INTO `departments` (`dep_name`) VALUES
('Technology'), -- id: 1
('Human Resources'), -- id: 2
('Marketing'), -- id: 3
('Finance'), -- id: 4
('Sales'); -- id: 5

-- An employee cannot be his own supervisor
INSERT INTO employees (first_name, last_name, salary, dep_id, supervisor_id) VALUES
-- Technology Department (id: 1)
('Kylian', 'Mbappé', 15000.00, 1, NULL),
('Cristiano', 'Ronaldo', 9500.00, 1, 1),
('Robert', 'Lewandowski', 8500.00, 1, 1),
('Kevin', 'De Bruyne', 8200.00, 1, 1),
('Erling', 'Haaland', 7800.00, 1, 2),
('Vinícius', 'Junior', 7500.00, 1, 2),
('Pedri', 'González', 7200.00, 1, 3),
('Jude', 'Bellingham', 7000.00, 1, 3),
('Phil', 'Foden', 6800.00, 1, 4),
('Bukayo', 'Saka', 6500.00, 1, 4),
('Jamal', 'Musiala', 6200.00, 1, 5),
('Eduardo', 'Camavinga', 6000.00, 1, 5),

-- Human Resources Department (id: 2)
('Lionel', 'Messi', 12000.00, 2, NULL),
('Neymar', 'Junior', 6500.00, 2, 13),
('Luka', 'Modrić', 6200.00, 2, 13),
('Karim', 'Benzema', 6000.00, 2, 13),
('Sadio', 'Mané', 5800.00, 2, 14),
('Mohamed', 'Salah', 5600.00, 2, 14),
('Virgil', 'van Dijk', 5400.00, 2, 15),
('Alisson', 'Becker', 5200.00, 2, 15),
('Fabinho', 'Tavares', 5000.00, 2, 16),
('Jordan', 'Henderson', 4800.00, 2, 16),

-- Marketing Department (id: 3)
('Sergio', 'Ramos', 10000.00, 3, NULL),
('Thibaut', 'Courtois', 5500.00, 3, 23),
('Casemiro', 'Santos', 5300.00, 3, 23),
('Toni', 'Kroos', 5100.00, 3, 23),
('Marco', 'Verratti', 4900.00, 3, 24),
('Gianluigi', 'Donnarumma', 4700.00, 3, 24),
('Marquinhos', 'Corrêa', 4500.00, 3, 25),
('Achraf', 'Hakimi', 4300.00, 3, 25),
('Presnel', 'Kimpembe', 4100.00, 3, 26),
('Idrissa', 'Gueye', 3900.00, 3, 26),
('Ander', 'Herrera', 3700.00, 3, 27),
('Leandro', 'Paredes', 3500.00, 3, 27),

-- Finance Department (id: 4)
('Harry', 'Kane', 9000.00, 4, NULL),
('Son', 'Heung-min', 5200.00, 4, 35),
('Dejan', 'Kulusevski', 5000.00, 4, 35),
('Pierre-Emile', 'Højbjerg', 4800.00, 4, 35),
('Rodrigo', 'Bentancur', 4600.00, 4, 36),
('Yves', 'Bissouma', 4400.00, 4, 36),
('Ivan', 'Perišić', 4200.00, 4, 37),
('Richarlison', 'Andrade', 4000.00, 4, 37),
('Emerson', 'Royal', 3800.00, 4, 38),
('Davinson', 'Sánchez', 3600.00, 4, 38),
('Eric', 'Dier', 3400.00, 4, 39),
('Ben', 'Davies', 3200.00, 4, 39),

-- Sales Department (id: 5)
('Bruno', 'Fernandes', 8500.00, 5, NULL),
('Marcus', 'Rashford', 5100.00, 5, 47),
('Jadon', 'Sancho', 4900.00, 5, 47),
('Anthony', 'Martial', 4700.00, 5, 47),
('Mason', 'Greenwood', 4500.00, 5, 48),
('Paul', 'Pogba', 4300.00, 5, 48),
('Fred', 'Santos', 4100.00, 5, 49),
('Scott', 'McTominay', 3900.00, 5, 49),
('Luke', 'Shaw', 3700.00, 5, 50),
('Aaron', 'Wan-Bissaka', 3500.00, 5, 50);

-- An employee can only be a manager of one department
UPDATE departments SET manager_id = 1 WHERE id = 1; -- Kylian Mbappé manages Technology
UPDATE departments SET manager_id = 13 WHERE id = 2; -- Lionel Messi manages Human Resources
UPDATE departments SET manager_id = 23 WHERE id = 3; -- Sergio Ramos manages Marketing
UPDATE departments SET manager_id = 35 WHERE id = 4; -- Harry Kane manages Finance
UPDATE departments SET manager_id = 47 WHERE id = 5; -- Bruno Fernandes manages Sales