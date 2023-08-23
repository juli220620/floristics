--liquibase formatted sql
--changeset Yan:init-dml failOnError:true

insert into floristics.achievement (id, name, description)
values  ('BOLSHIE_NADEZHDY', 'Большие надежды', 'Десять — отличное число для старта.'),
        ('MAMIN_SADOVOD', 'Мамин садовод', 'Десять цветов? Ты молоец.');

insert into floristics.base_flower_dict (id, name, growth_time, water_consumption, nutrient_consumption, price, tier, max_cycles, ideal_humidity, ideal_light)
values  ('AFRICAN_VIOLET', 'Violet', 3, 10, 5, 100, 1, 5, 0.5, 0.4),
        ('BALSAM', 'Balsam', 30, 30, 15, 5000, 2, null, 0.6, 0.8),
        ('BEGONIA', 'Begonia', 10, 30, 20, 500, 1, 3, 0.9, 0.5),
        ('CACTUS', 'Cactus', 3, 5, 15, 100, 1, 5, 0.3, 0.8),
        ('CHAMOMILE', 'Chamomie', 1, 15, 10, 10, 1, 3, 0.7, 0.8),
        ('CORNFLOWER', 'Cornflower', 1, 0, 0, 0, 0, null, 0.3, 0.9),
        ('GERANIUM', 'Geranium', 5, 30, 20, 2000, 2, 3, 0.6, 0.8),
        ('SPATHIPHYLLUM', 'Spathiphyllum', 10, 20, 30, 1000, 2, 5, 0.9, 0.7),
        ('TEST', 'Test', 1, 0, 0, 1, 0, null, 0.5, 0.5),
        ('VERBENA', 'Verbena', 10, 15, 20, 1000, 2, 5, 0.6, 0.7);

insert into floristics.currency_dict (id, name, factor_to_cash)
values  ('BLUE', 'Blue', 10),
        ('CASH', 'Cash', 1),
        ('DELORIAN', 'Delorian', 0),
        ('GEMSTONES', 'Gemstones', 0),
        ('GREEN', 'Green', 10),
        ('RED', 'Red', 50),
        ('STONES_AND_BOARDS', 'Stones and Boards', 0);

insert into floristics.user (id, username, password)
values  (1, 'julia-flower-girl', 'i-made-this'),
        (2, 'cvazer', '1319875656');

insert into floristics.game_system_dict (id, name)
values  ('AUTO_HARVEST', 'Auto Harvest'),
        ('PERENNIAL_FLOWERS', 'Perennial Flowers'),
        ('POT_CASHBACK', 'Pot Cashback'),
        ('ROOM_CONDITIONS', 'Room Conditions'),
        ('ROOM_ENLARGEMENT', 'Room Enlargement'),
        ('TEST_SYSTEM', 'Test System'),
        ('TIME_SKIP', 'Time Skip');

insert into floristics.pot_dict (id, name, capacity, price, size)
values  ('CLAY_POT', 'Clay pot', 500, 50, 1),
        ('COFFEE_CUP', 'Coffee cup', 100, 0, 1),
        ('PLASTIC_POT', 'Plastic pot', 300, 30, 1),
        ('TEST_POT', 'Test pot', 1000000, 0, 1);

insert into floristics.flower_harvest (flower_id, currency_id, amount)
values  ('AFRICAN_VIOLET', 'BLUE', 90),
        ('BALSAM', 'RED', 250),
        ('BEGONIA', 'BLUE', 450),
        ('BEGONIA', 'GREEN', 300),
        ('CACTUS', 'GREEN', 135),
        ('CHAMOMILE', 'BLUE', 30),
        ('CHAMOMILE', 'GREEN', 20),
        ('CORNFLOWER', 'BLUE', 5),
        ('CORNFLOWER', 'GREEN', 5),
        ('GERANIUM', 'RED', 60),
        ('SPATHIPHYLLUM', 'GREEN', 900),
        ('TEST', 'BLUE', 1),
        ('VERBENA', 'BLUE', 450);

insert into floristics.flower_harvest_bonus_info (flower_id, count, currency_id, multiplier, flat_bonus, free_offspring_chance)
values  ('CHAMOMILE', 10, 'BLUE', 2, 100000, 0);

insert into floristics.user_room (id, user_id, area, name, humidity, light)
values  (1, 1, 7, 'julias-room', 0.6, 0.8),
        (2, 2, 5, 'yans-room', 0.6, 0.8);

insert into floristics.room_flower (id, room_id, flower_id, pot_id, water, nutrient, growth, updated, status, death_ticks, planted, current_cycle, cycles, auto_harvest)
values  (89, 2, 'BALSAM', 'CLAY_POT', 799940, 35425, 3047, '2023-07-14 17:50:08', 'DEAD', 1254, '2023-07-11 18:04:29', null, null, false),
        (107, 1, 'BALSAM', 'CLAY_POT', 0, 0, 3332, '2023-08-22 17:28:38', 'DEAD', 8227, '2023-08-14 16:45:32', null, null, true),
        (108, 1, 'BALSAM', 'CLAY_POT', 0, 0, 332, '2023-08-22 17:28:38', 'DEAD', 11227, '2023-08-14 16:45:32', null, null, true),
        (109, 1, 'BALSAM', 'CLAY_POT', 0, 0, 3332, '2023-08-22 17:28:38', 'DEAD', 8227, '2023-08-14 16:45:32', null, null, false);

insert into floristics.user_achievement (user_id, achievement_id, achieved, date, meta)
values  (1, 'BOLSHIE_NADEZHDY', 0, null, '{"count": 8}'),
        (1, 'MAMIN_SADOVOD', 1, '2023-06-26', '{"count": 10}'),
        (2, 'BOLSHIE_NADEZHDY', 0, null, '{"count": 4}');

insert into floristics.user_currency (user_id, currency_id, amount)
values  (1, 'BLUE', 102638),
        (1, 'CASH', 1000036),
        (1, 'DELORIAN', 94000),
        (1, 'GEMSTONES', 7),
        (1, 'GREEN', 1815),
        (1, 'RED', 316149),
        (1, 'STONES_AND_BOARDS', 80),
        (2, 'BLUE', 99450),
        (2, 'CASH', 84260),
        (2, 'GREEN', 118955),
        (2, 'RED', 100000);

insert into floristics.user_flower_count (user_id, flower_id, count)
values  (1, 'BALSAM', 5),
        (1, 'BEGONIA', 4),
        (1, 'CHAMOMILE', 11),
        (1, 'CORNFLOWER', 1),
        (1, 'GERANIUM', 4),
        (1, 'TEST', 3);

insert into floristics.user_game_system (user_id, system_id, system_level)
values  (1, 'AUTO_HARVEST', 1),
        (1, 'PERENNIAL_FLOWERS', 3),
        (1, 'POT_CASHBACK', 2),
        (1, 'ROOM_CONDITIONS', 3),
        (1, 'ROOM_ENLARGEMENT', 1),
        (1, 'TEST_SYSTEM', 2),
        (1, 'TIME_SKIP', 2);
