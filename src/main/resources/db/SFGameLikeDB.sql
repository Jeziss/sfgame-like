DROP TABLE IF EXISTS player_items CASCADE;
DROP TABLE IF EXISTS item_templates CASCADE;
DROP TABLE IF EXISTS weapon_stats CASCADE;
DROP TABLE IF EXISTS guild_members CASCADE;
DROP TABLE IF EXISTS guilds CASCADE;
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS legacy_leaderboard CASCADE;


DROP TYPE IF EXISTS item_slot;
CREATE TYPE item_slot AS ENUM ('WEAPON', 'HAT', 'ARMOR', 'LEGGINS', 'AMULET', 'BOOTS');


CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lvl INTEGER NOT NULL DEFAULT 1,
    xp INTEGER NOT NULL DEFAULT 0,
    gold INTEGER NOT NULL DEFAULT 1,
    base_strength INTEGER NOT NULL DEFAULT 15,
    base_constitution INTEGER NOT NULL DEFAULT 15,
    base_luck INTEGER NOT NULL DEFAULT 0
);


CREATE TABLE guilds (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    xp_bonus_percent INTEGER DEFAULT 0,
    gold_bonus_percent INTEGER DEFAULT 0,
    dmg_bonus_percent INTEGER DEFAULT 0,
    hp_bonus_percent INTEGER DEFAULT 0
);

CREATE TABLE guild_members (
    member_id INTEGER PRIMARY KEY REFERENCES players(id) ON DELETE CASCADE,
    guild_id INTEGER NOT NULL REFERENCES guilds(id) ON DELETE CASCADE
);

CREATE TABLE item_templates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slot item_slot NOT NULL,
    icon VARCHAR(255)
);

CREATE TABLE player_items (
    id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    template_id INTEGER NOT NULL REFERENCES item_templates(id),
    strength INTEGER DEFAULT 0,
    constitution INTEGER DEFAULT 0,
    luck INTEGER DEFAULT 0,
    equipped_slot item_slot,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (player_id, equipped_slot)
);

CREATE TABLE weapon_stats (
    player_item_id INTEGER PRIMARY KEY
        REFERENCES player_items(id) ON DELETE CASCADE,
    min_damage INTEGER NOT NULL,
    max_damage INTEGER NOT NULL
);

CREATE TABLE legacy_leaderboard (
    player_id INTEGER PRIMARY KEY REFERENCES players(id),
    position INTEGER NOT NULL DEFAULT 0
);


INSERT INTO players (name, lvl) VALUES
('Basic Hero 1', 1),
('Basic Hero 2', 2),
('Basic Hero 3', 3);

INSERT INTO legacy_leaderboard (player_id, position) VALUES
(1, 1),
(2, 2),
(3, 3);

INSERT INTO guilds (name, xp_bonus_percent, gold_bonus_percent, dmg_bonus_percent, hp_bonus_percent) VALUES
('Knights of Valor', 10, 5, 7, 8),
('Mages Guild', 15, 3, 10, 5);

INSERT INTO guild_members (member_id, guild_id) VALUES
(1, 1),
(2, 2),
(3, 1);


INSERT INTO item_templates (name, slot, icon) VALUES
('Rusty Sword', 'WEAPON', 'sword_rusty.png'),
('Oak Staff', 'WEAPON', 'staff_oak.png'),
('Hunter Bow', 'WEAPON', 'bow_hunter.png'),
('Leather Helmet', 'HAT', 'helmet_leather.png'),
('Iron Armor', 'ARMOR', 'armor_iron.png'),
('Boots of Strength', 'BOOTS', 'ring_strength.png'),
('Amulet of Wisdom', 'AMULET', 'amulet_wisdom.png'),
('Shitting trousers', 'LEGGINS', 'amulet_wisdom.png');


INSERT INTO player_items
(player_id, template_id, strength, constitution, luck, equipped_slot)
VALUES
(1, 1, 2, 1, 0, 'WEAPON'),
(1, 4, 0, 2, 0, 'HAT'),
(1, 5, 1, 3, 0, 'ARMOR'),
(1, 6, 3, 0, 0, 'BOOTS'),
(2, 2, 0, 0, 3, 'WEAPON'),
(2, 7, 0, 0, 4, 'AMULET'),
(3, 3, 1, 0, 2, 'WEAPON'),
(3, 6, 2, 0, 0, 'BOOTS'),
(3, 8, 5, 0, 1, 'LEGGINS');


INSERT INTO weapon_stats (player_item_id, min_damage, max_damage)
SELECT id, 3, 6
FROM player_items
WHERE player_id = 1 AND equipped_slot = 'WEAPON';

INSERT INTO weapon_stats (player_item_id, min_damage, max_damage)
SELECT id, 2, 5
FROM player_items
WHERE player_id = 2 AND equipped_slot = 'WEAPON';

INSERT INTO weapon_stats (player_item_id, min_damage, max_damage)
SELECT id, 4, 7
FROM player_items
WHERE player_id = 3 AND equipped_slot = 'WEAPON';