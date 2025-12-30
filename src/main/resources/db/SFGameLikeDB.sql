DROP TABLE IF EXISTS player_items CASCADE;
DROP TABLE IF EXISTS item_templates CASCADE;
DROP TABLE IF EXISTS weapon_stats CASCADE;
DROP TABLE IF EXISTS guild_members CASCADE;
DROP TABLE IF EXISTS guilds CASCADE;
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS legacy_leaderboard CASCADE;


DROP TYPE IF EXISTS item_slot;
CREATE TYPE item_slot AS ENUM ('WEAPON', 'HELMET', 'ARMOR', 'RING', 'AMULET');

DROP TYPE IF EXISTS player_classes;
CREATE TYPE player_classes AS ENUM ('WARRIOR', 'MAGE', 'SCOUT');


CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lvl INTEGER NOT NULL DEFAULT 1,
    xp INTEGER NOT NULL DEFAULT 0,
    gold INTEGER NOT NULL DEFAULT 1,
    base_strength INTEGER NOT NULL DEFAULT 15,
    base_dexterity INTEGER NOT NULL DEFAULT 15,
    base_intelligence INTEGER NOT NULL DEFAULT 15,
    base_constitution INTEGER NOT NULL DEFAULT 15,
    player_class player_classes NOT NULL
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
    dexterity INTEGER DEFAULT 0,
    intelligence INTEGER DEFAULT 0,
    constitution INTEGER DEFAULT 0,
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


INSERT INTO players (name, player_class, lvl) VALUES
('Basic Warrior', 'WARRIOR', 1),
('Basic Mage', 'MAGE', 2),
('Basic Scout', 'SCOUT', 3);

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