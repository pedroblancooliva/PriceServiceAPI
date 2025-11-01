-- Datos específicos para tests
-- Estos datos representan los casos de test clásicos del ejercicio Inditex

-- Limpiar datos existentes (solo en tests)
DELETE FROM prices WHERE brand_id = 1;

-- Precio base para todo el periodo (prioridad 0)
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 1, 35455, 0, 35.50, 'EUR');

-- Precio promocional día 14 de 15:00 a 18:30 (prioridad 1)
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2020-06-14T15:00:00', '2020-06-14T18:30:00', 2, 35455, 1, 25.45, 'EUR');

-- Precio especial día 15 de 00:00 a 11:00 (prioridad 1)
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2020-06-15T00:00:00', '2020-06-15T11:00:00', 3, 35455, 1, 30.50, 'EUR');

-- Precio premium desde día 15 16:00 hasta fin de año (prioridad 1)
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2020-06-15T16:00:00', '2020-12-31T23:59:59', 4, 35455, 1, 38.95, 'EUR');

-- Datos adicionales para tests edge cases
-- Producto diferente para tests de no encontrado
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 1, 12345, 0, 99.99, 'EUR');

-- Marca diferente para tests de no encontrado
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(2, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 1, 35455, 0, 45.00, 'EUR');

-- Precio con fechas futuras para tests de rango
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2025-01-01T00:00:00', '2025-12-31T23:59:59', 5, 35455, 0, 50.00, 'EUR');

-- Precio con fechas pasadas para tests de rango
INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2019-01-01T00:00:00', '2019-12-31T23:59:59', 6, 35455, 0, 25.00, 'EUR');