CREATE TYPE item_slot AS ENUM ('WEAPON', 'HELMET', 'ARMOR', 'RING', 'AMULET');

CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lvl INTEGER NOT NULL DEFAULT 1,
    xp INTEGER NOT NULL DEFAULT 0,
    gold INTEGER NOT NULL DEFAULT 1
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

