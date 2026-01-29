-- Insert sample users for testing

-- Driver 1
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('driver1', 'driver1@carpooling.com', '$2a$10$GRLdNijSQMUvlfvHcmusC.O9MhOFKT3GfWmKGUC9qFp6kCsJhCRwi', 'DRIVER', NOW(), NOW());

-- Driver 2
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('driver2', 'driver2@carpooling.com', '$2a$10$GRLdNijSQMUvlfvHcmusC.O9MhOFKT3GfWmKGUC9qFp6kCsJhCRwi', 'DRIVER', NOW(), NOW());

-- Passenger 1
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('passenger1', 'passenger1@carpooling.com', '$2a$10$GRLdNijSQMUvlfvHcmusC.O9MhOFKT3GfWmKGUC9qFp6kCsJhCRwi', 'PASSENGER', NOW(), NOW());

-- Passenger 2
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('passenger2', 'passenger2@carpooling.com', '$2a$10$GRLdNijSQMUvlfvHcmusC.O9MhOFKT3GfWmKGUC9qFp6kCsJhCRwi', 'PASSENGER', NOW(), NOW());

-- Passenger 3
INSERT INTO users (username, email, password, role, created_at, updated_at) VALUES
('passenger3', 'passenger3@carpooling.com', '$2a$10$GRLdNijSQMUvlfvHcmusC.O9MhOFKT3GfWmKGUC9qFp6kCsJhCRwi', 'PASSENGER', NOW(), NOW());

-- Insert sample rides

-- Ride 1: Bangalore to Udupi (4 seats available)
INSERT INTO rides (driver_id, source, destination, ride_date, ride_time, total_seats, available_seats, price_per_seat, created_at, updated_at) VALUES
(1, 'Bangalore', 'Udupi', CURRENT_DATE + INTERVAL 1 DAY, '06:00', 4, 4, 300.00, NOW(), NOW());

-- Ride 2: Bangalore to Manipal (3 seats available)
INSERT INTO rides (driver_id, source, destination, ride_date, ride_time, total_seats, available_seats, price_per_seat, created_at, updated_at) VALUES
(1, 'Bangalore', 'Manipal', CURRENT_DATE + INTERVAL 2 DAY, '07:30', 5, 5, 350.00, NOW(), NOW());

-- Ride 3: Bangalore to Udupi (2 seats available after 1 confirmed)
INSERT INTO rides (driver_id, source, destination, ride_date, ride_time, total_seats, available_seats, price_per_seat, created_at, updated_at) VALUES
(2, 'Bangalore', 'Udupi', CURRENT_DATE + INTERVAL 1 DAY, '08:00', 3, 2, 280.00, NOW(), NOW());

-- Insert sample bookings

-- Booking 1: Passenger 1 -> Ride 1 (REQUESTED)
INSERT INTO bookings (ride_id, passenger_id, status, booked_at, updated_at) VALUES
(1, 3, 'REQUESTED', NOW(), NOW());

-- Booking 2: Passenger 2 -> Ride 1 (CONFIRMED - seat taken)
INSERT INTO bookings (ride_id, passenger_id, status, booked_at, updated_at) VALUES
(1, 4, 'CONFIRMED', NOW(), NOW());

-- Booking 3: Passenger 3 -> Ride 3 (CONFIRMED - seat taken)
INSERT INTO bookings (ride_id, passenger_id, status, booked_at, updated_at) VALUES
(3, 5, 'CONFIRMED', NOW(), NOW());

-- Note: The password hash above is BCrypt for 'password123'
-- To generate your own: Use BCryptPasswordEncoder in Java or https://www.bcryptcalculator.com/
